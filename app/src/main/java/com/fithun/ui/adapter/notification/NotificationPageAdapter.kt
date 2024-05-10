package com.fithun.ui.adapter.notification

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fithun.ui.fragments.OfferFragment
import com.fithun.ui.fragments.notification.NotificationsFragment

class NotificationPageAdapter(
    fragmentActivity: FragmentActivity,
    private val totalTabs: Int
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return totalTabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NotificationsFragment()
            else -> OfferFragment()
        }
    }
}
