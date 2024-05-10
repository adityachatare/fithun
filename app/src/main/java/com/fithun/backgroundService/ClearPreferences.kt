package com.fithun.backgroundService


import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

class ClearPreferences(private val context: Context) {

    fun setClearPreference() {
        val now = Calendar.getInstance()
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (now.after(targetTime)) {
            targetTime.add(Calendar.DAY_OF_YEAR, 1)
        }

        val timeDiffMillis = targetTime.timeInMillis - now.timeInMillis

        val workRequest = OneTimeWorkRequestBuilder<ClearPreferencesWorker>()
            .setInitialDelay(timeDiffMillis, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "clear_preferences",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
}
