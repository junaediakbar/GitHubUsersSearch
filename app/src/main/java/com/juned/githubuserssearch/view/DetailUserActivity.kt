package com.juned.githubuserssearch.view

import android.os.Bundle
import android.view.Gravity
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.juned.githubuserssearch.R
import com.juned.githubuserssearch.adapter.SectionsPagerAdapter
import com.juned.githubuserssearch.api.UserDetailsResponse
import com.juned.githubuserssearch.databinding.ActivityDetailUserBinding
import com.juned.githubuserssearch.helper.visibility
import com.juned.githubuserssearch.model.User
import com.juned.githubuserssearch.viewmodel.DetailUserViewModel

class DetailUserActivity : AppCompatActivity() {

    private var _binding: ActivityDetailUserBinding? = null
    private val binding get() = _binding!!

    private val detailUserViewModel by viewModels<DetailUserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        detailUserViewModel.apply {
            getUserDetails(user.login.toString())

            userDetails.observe(this@DetailUserActivity) { user->
                setUserDetailData(user)
            }

            isLoading.observe(this@DetailUserActivity) { checkLoading->
                showLoading(checkLoading)
            }

            error.observe(this@DetailUserActivity) {
                it.getContentIfNotHandled()?.let { text ->
                    showErrorSnackBar(text)
                }
            }
        }

        val viewPager = binding.followViewPager
        viewPager.adapter = SectionsPagerAdapter(this@DetailUserActivity, user.login.toString())

        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation= 0f
    }
    private fun setUserDetailData(userDetail: UserDetailsResponse){
        binding.apply{
            tvDetailUsername.text = userDetail.login
            Glide.with(this@DetailUserActivity).load(userDetail.avatarUrl)
                .into(imgDetailPhoto)
            tvFollower.text = getString(R.string.detail_followers, userDetail.followers.toString())
            tvFollowing.text =  getString(R.string.detail_following, userDetail.following.toString())
            tvFullname.text = userDetail.name.toString()
            tvRepositories.text =getString(R.string.detail_repositories,userDetail.publicRepos.toString())
            if (userDetail.company == null){
                tvCompany.text = getString(R.string.not_found)
            }else{
                tvCompany.text= userDetail.company.toString()
            }
            if (userDetail.location == null){
                tvLocation.text = getString(R.string.not_found)
            }else{
                tvLocation.text= userDetail.location.toString()
            }
        }
    }

    private fun showErrorSnackBar(text: String) {
        Snackbar.make(binding.root, getString(R.string.error_message)+ text, Snackbar.LENGTH_SHORT)
            .setDuration(15000).apply {
                val params = CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT )
                params.setMargins(2, 0, 2, 8)
                params.gravity = Gravity.BOTTOM
                params.anchorGravity = Gravity.BOTTOM

                view.layoutParams = params
                show()
            }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarDetail.visibility = visibility(true)
        } else {
            binding.progressBarDetail.visibility = visibility(false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}