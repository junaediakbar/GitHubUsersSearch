package com.juned.githubuserssearch.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.juned.githubuserssearch.R
import com.juned.githubuserssearch.adapter.UserAdapter
import com.juned.githubuserssearch.databinding.ActivityFavoriteUsersBinding
import com.juned.githubuserssearch.helper.visibility
import com.juned.githubuserssearch.model.User
import com.juned.githubuserssearch.viewmodel.FavoriteViewModel

class FavoriteUserActivity : AppCompatActivity() {

    private var _binding: ActivityFavoriteUsersBinding? = null
    private val binding get() = _binding

    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        FavoriteViewModel.Factory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteUsersBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        showRecyclerList()

        favoriteViewModel.apply {
            getFavorites().observe(this@FavoriteUserActivity) { users ->
                binding?.apply {
                    showLoading(true)
                    rvFavorites.adapter = UserAdapter(ArrayList(users)).apply {
                        setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                            override fun onItemClicked(data: User) {
                                val detailsIntent =
                                    Intent(
                                        this@FavoriteUserActivity,
                                        DetailUserActivity::class.java
                                    )
                                        .apply {
                                            putExtra(DetailUserActivity.EXTRA_USER, data)
                                        }
                                startActivity(detailsIntent)
                            }
                        })
                    }

                    tvFavEmpty.apply {
                        if (users.isEmpty()) {
                            visibility = visibility(true)
                            text = getString(R.string.not_found)
                        } else {
                            visibility = visibility(false)
                        }
                    }
                    showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.progressBarFav?.visibility = visibility(true)
        } else {
            binding?.progressBarFav?.visibility = visibility(false)
        }
    }

    private fun showRecyclerList() {
        binding?.apply {
            rvFavorites.layoutManager = LinearLayoutManager(this@FavoriteUserActivity)
            rvFavorites.setHasFixedSize(true)
        }
    }
}
