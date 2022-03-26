package com.juned.githubuserssearch.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.juned.githubuserssearch.view.FollowFragment

class SectionsPagerAdapter(activity: AppCompatActivity, private val username: String) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowFragment()

        fragment.arguments = Bundle().apply {
            putString(FollowFragment.ARG_USERNAME, username)
            putInt(FollowFragment.ARG_SECTION_INDEX, position)
        }

        return fragment
    }

}