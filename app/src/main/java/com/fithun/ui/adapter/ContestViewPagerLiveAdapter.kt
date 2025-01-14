package com.fithun.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fithun.ui.fragments.LeaderboardLiveFragment

class ContestViewPagerLiveAdapter(
    fragmentActivity: FragmentActivity,
    private val totalTabs: Int
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return totalTabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LeaderboardLiveFragment()
            else -> LeaderboardLiveFragment()
        }
    }
}
