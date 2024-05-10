package com.fithun.firebaseMessaging

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.fithun.R
import com.fithun.ui.hostFragment.FragmentHostActivity
import com.fithun.utils.SavedPrefManager

class MyFirebaseMessagingService : FirebaseMessagingService() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun handleIntent(intent: Intent?) {
        try {
            if (intent!!.extras != null) {
                val builder = RemoteMessage.Builder("MessagingService")

                for (key in intent!!.extras!!.keySet()) {
                    builder.addData(key!!, intent!!.extras!![key].toString())
                }
                onMessageReceived(builder.build())
            } else {
                super.handleIntent(intent)
            }
        } catch (e: java.lang.Exception) {
            super.handleIntent(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            getRemoteView(remoteMessage.data)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant", "UnspecifiedImmutableFlag")
    fun getRemoteView(data: MutableMap<String, String>) {




        val notification: Notification
        val CHANNEL_ID = "my_channel_01"
        var pendingIntent: PendingIntent? = null
        val notifyImage = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_square)



        if(data["notificationType"] == "START"){
            SavedPrefManager.stepCountDeleteOfContest(this)
            SavedPrefManager.savePreferenceBoolean(this,SavedPrefManager.isContestStart,true)
            SavedPrefManager.saveStringPreferences(this,SavedPrefManager.CONTEST_ID,data["contestId"])
        }else{
            SavedPrefManager.savePreferenceBoolean(this,SavedPrefManager.isContestStart,false)
        }

        val notifyIntent = Intent(this, FragmentHostActivity::class.java)
        pendingIntent = PendingIntent.getActivity(applicationContext, 0, notifyIntent, PendingIntent.FLAG_MUTABLE)



        if (Build.VERSION.SDK_INT >= 26) {
            //This only needs to be run on Devices on Android O and above
            val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val id = "my_channel_01"
            val name: CharSequence = "YOUR_CHANNEL NAME" //user visible
            val description = "YOUR_CHANNEL_DESCRIPTION" //user visible
            val importance = NotificationManager.IMPORTANCE_MAX




            @SuppressLint("WrongConstant") val mChannel = NotificationChannel(id, name, importance)
            mChannel.description = description
            mChannel.enableLights(true)
            mChannel.enableVibration(true)
            mChannel.canShowBadge()
            mChannel.setShowBadge(true)
            mNotificationManager.createNotificationChannel(mChannel)
            notification = Notification.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_square)
                .setContentTitle(data["title"])
                .setContentText(data["body"])
                .setTicker(getString(R.string.app_name))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationManager.IMPORTANCE_LOW)
                .setAutoCancel(true)
                .setLargeIcon(Bitmap.createScaledBitmap(notifyImage, 100, 100, false))
                .setContentIntent(pendingIntent)
                .setOngoing(false).build()


            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notificationId = System.currentTimeMillis().toInt()
            notificationManager.notify(notificationId, notification)

        }
        else {
            val mNotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val id = "YOUR_CHANNEL_ID"
            val name: CharSequence = "YOUR_CHANNEL NAME" //user visible
            val description = "YOUR_CHANNEL_DESCRIPTION" //user visible
            val importance = NotificationManager.IMPORTANCE_MAX
            @SuppressLint("WrongConstant") val mChannel =
                NotificationChannel(id, name, importance)
            mChannel.description = description
            mChannel.enableLights(true)
            mChannel.enableVibration(true)
            mChannel.canShowBadge()
            mChannel.setShowBadge(true)
            mNotificationManager.createNotificationChannel(mChannel)
            val notificationBuilder  : NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_square)
                .setContentTitle(getString(R.string.app_name))
                .setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
                .setContentTitle(data["title"])
                .setContentText(data["body"])
                .setLargeIcon(notifyImage)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationManager.IMPORTANCE_LOW)
                .setAutoCancel(true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_square)

            } else {
                notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_square)
            }
            notificationBuilder.setContentIntent(pendingIntent)

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notificationId = System.currentTimeMillis().toInt()
            notificationManager.notify(notificationId,  notificationBuilder.build())

        }
    }



}
