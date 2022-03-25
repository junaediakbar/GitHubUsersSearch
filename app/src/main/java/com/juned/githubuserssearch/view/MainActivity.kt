package com.juned.githubuserssearch.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.juned.githubuserssearch.*
import com.juned.githubuserssearch.adapter.UserAdapter
import com.juned.githubuserssearch.api.ListUsersResponse
import com.juned.githubuserssearch.databinding.ActivityMainBinding
import com.juned.githubuserssearch.helper.visibility
import com.juned.githubuserssearch.model.User
import com.juned.githubuserssearch.viewmodel.MainViewModel


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        showRecyclerList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

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
        return true
    }

    private fun showErrorSnackBar(text: String) {
        Snackbar.make(binding.root, getString(R.string.error_message)+ text, Snackbar.LENGTH_SHORT)
            .setAction(R.string.try_again) { mainViewModel.searchUsers("") }.setDuration(10000)
            .show()
    }
    private fun showSnackBar(text: String) {
        Snackbar.make(binding.root,text, Snackbar.LENGTH_SHORT)
            .show()
    }

    private fun setUsersData(users: List<ListUsersResponse>) {
        val listUser = ArrayList<User>()
        for(user in users){
            listUser.add(User(login = user.login,avatarUrl = user.avatarUrl))
        }
        val adapter = UserAdapter(listUser)
        binding.rvUser.adapter = adapter
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
            binding.rvUser.layoutManager = GridLayoutManager(this,2)
        }
       else{
            binding.rvUser.layoutManager = LinearLayoutManager(this)
        }
        binding.rvUser.setHasFixedSize(true)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = visibility(true)
        } else {
            binding.progressBar.visibility = visibility(false)
        }
    }

    companion object

}
