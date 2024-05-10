package com.fithun.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fithun.ui.fragments.LeaderboardFragment
import com.fithun.ui.fragments.WinningsFragment

class ContestViewPagerAdapter(

    fragmentActivity: FragmentActivity,
    private val totalTabs: Int,
    val id: String,
    var typeSpot: Boolean = false,
    val isJoinedUser: Boolean,
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return totalTabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WinningsFragment(typeSpot,id)
            else -> LeaderboardFragment(id,isJoinedUser)
        }
    }
}
