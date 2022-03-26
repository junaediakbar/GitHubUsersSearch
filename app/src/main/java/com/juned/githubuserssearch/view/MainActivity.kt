package com.juned.githubuserssearch.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.juned.githubuserssearch.*
import com.juned.githubuserssearch.adapter.UserAdapter
import com.juned.githubuserssearch.api.ListUsersResponse
import com.juned.githubuserssearch.databinding.ActivityMainBinding
import com.juned.githubuserssearch.helper.SettingPreferences
import com.juned.githubuserssearch.helper.visibility
import com.juned.githubuserssearch.model.User
import com.juned.githubuserssearch.viewmodel.MainViewModel


class MainActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    private val mainViewModel by viewModels<MainViewModel>{
        MainViewModel.Factory(SettingPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        showRecyclerList()

        mainViewModel.apply {
            isLoading.observe(this@MainActivity) {
                showLoading(it)
            }
            listUsers.observe(this@MainActivity) {
                setUsersData(it)
            }
            totalCount.observe(
                this@MainActivity
            ){
                showSnackBar(getString(R.string.success_search,it))
            }
            error.observe(this@MainActivity) {
                it.getContentIfNotHandled()?.let { text ->
                    showErrorSnackBar(text)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.apply {
            maxWidth= Int.MAX_VALUE
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            queryHint = resources.getString(R.string.search_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String): Boolean {
                    Toast.makeText(this@MainActivity, "Mencari \"$query\"...", Toast.LENGTH_SHORT).show()
                    searchView.clearFocus()
                    mainViewModel.searchUsers(query)
                    return true
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })
        }


        val menuTheme = menu.findItem(R.id.theme_changer)

        mainViewModel.getThemeSettings().observe(this@MainActivity) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                menuTheme.apply {
                    isChecked = true
                    setIcon(R.drawable.ic_baseline_nights_24)
                }
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                menuTheme.apply {
                    isChecked = false
                    setIcon(R.drawable.ic_baseline_nights_off_24)
                }
            }
        }
        return true
    }

    private fun showErrorSnackBar(text: String) {
        binding?.root?.let {
            Snackbar.make(it, getString(R.string.error_message)+ text, Snackbar.LENGTH_SHORT)
                .setAction(R.string.try_again) { mainViewModel.searchUsers("") }.setDuration(10000)
                .show()
        }
    }
    private fun showSnackBar(text: String) {
        binding?.let {
            Snackbar.make(it.root,text, Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun setUsersData(users: List<ListUsersResponse?>?) {
        val listUser = ArrayList<User>()
        if (users != null) {
            for(user in users){
                if (user?.login !== null && user.avatarUrl !== null) {
                    listUser.add(User(user.login, user.avatarUrl))
                }
            }
        }
        val adapter = UserAdapter(listUser)
        binding?.rvUser?.adapter = adapter
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intentToDetail = Intent(this@MainActivity, DetailUserActivity::class.java)
                    .apply {
                    putExtra(DetailUserActivity.EXTRA_USER, data)
                }
                startActivity(intentToDetail)
            }
        })
    }

    private fun showRecyclerList() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding?.rvUser?.layoutManager = GridLayoutManager(this,2)
        }
       else{
            binding?.rvUser?.layoutManager = LinearLayoutManager(this)
        }
        binding?.rvUser?.setHasFixedSize(true)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.progressBar?.visibility = visibility(true)
        } else {
            binding?.progressBar?.visibility = visibility(false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {

            R.id.theme_changer -> {
                mainViewModel.saveThemeSetting(!item.isChecked)
                true
            }

            R.id.favorite -> {
                startActivity(Intent(this, FavoriteUserActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object

}
