package com.fithun.backgroundService

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ClearPreferenceReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
    if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {

        context?.let {
            ClearPreferences(it).setClearPreference()
        }
    }
}


}