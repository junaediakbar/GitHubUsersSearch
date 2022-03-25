package com.juned.githubuserssearch.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.juned.githubuserssearch.R
import com.juned.githubuserssearch.adapter.UserAdapter
import com.juned.githubuserssearch.api.ListUsersResponse
import com.juned.githubuserssearch.databinding.FragmentFollowBinding
import com.juned.githubuserssearch.helper.visibility
import com.juned.githubuserssearch.model.User
import com.juned.githubuserssearch.viewmodel.FollowViewModel

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private val followViewModel by viewModels<FollowViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME) as String
        val sectionIndex = arguments?.getInt(ARG_SECTION_INDEX) as Int

        if (sectionIndex == 0) {
            followViewModel.getFollowersData(username)
        } else {
            followViewModel.getFollowingData(username)
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.apply {
            rvFollow.apply {
                setHasFixedSize(true)
                this.layoutManager = layoutManager
                addItemDecoration(
                    DividerItemDecoration(
                        requireActivity(),
                        layoutManager.orientation
                    )
                )
            }
        }

        followViewModel.apply {

            isLoading.observe(this@FollowFragment) {
                showLoading(it)
            }
            listUsers.observe(this@FollowFragment) {
                setUsersData(it)
            }
            error.observe(this@FollowFragment) {
                it.getContentIfNotHandled()?.let { text ->
                    showErrorSnackBar(text)
                }
            }
        }
    }
    private fun showErrorSnackBar(text: String) {
        Snackbar.make(binding.root, getString(R.string.error_message)+ text, Snackbar.LENGTH_SHORT)
            .setAction(R.string.try_again) {
                val sectionIndex = arguments?.getInt(ARG_SECTION_INDEX) as Int
                val username = arguments?.getString(ARG_USERNAME) as String
                if(sectionIndex == 0){
                    followViewModel.getFollowersData(username)
                }else{
                    followViewModel.getFollowingData(username)
                }
            }.setDuration(10000)
            .show()
    }

    private fun setUsersData(users: List<ListUsersResponse>) {
        val listUser = ArrayList<User>()
        for(user in users){
            listUser.add(User(user.login, user.avatarUrl))
        }
        binding.apply {
            if (users.count() == 0) {
                tvNotFound.visibility = visibility(true)
            } else {
                rvFollow.adapter = UserAdapter(listUser)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarFollowers.visibility = visibility(true)
        } else {
            binding.progressBarFollowers.visibility = visibility(false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARG_USERNAME = "username"
        const val ARG_SECTION_INDEX= "section_index"
    }
}