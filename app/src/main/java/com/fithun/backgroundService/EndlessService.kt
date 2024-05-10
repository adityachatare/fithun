package com.fithun.backgroundService

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.SystemClock
import androidx.annotation.RequiresApi
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fithun.api.Constants
import com.fithun.ui.hostFragment.FragmentHostActivity
import com.fithun.utils.SavedPrefManager
import com.fithun.utils.StepCountListener
import com.fithun.R
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt
import java.time.LocalDateTime

class EndlessService : Service() {

    private lateinit var stepCountListener: StepCountListener

    private var unitRequest = ""
    private var unitRequestContest = ""
    private var lastSteps = ""
    private lateinit var notificationManager: NotificationManager
    val notificationChannelId = "ENDLESS SERVICE CHANNEL"

    override fun onBind(intent: Intent): IBinder? {
        return null
    }




    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        stepCountListener = StepCountListener(applicationContext)

        setServiceState(this, ServiceState.STARTED)

        Thread {
            while (true) {
                val stepsCount = (SavedPrefManager.getFloatPreferences(applicationContext, SavedPrefManager.totalDistance) * 1.3).roundToInt()
                val distanceWalked =  SavedPrefManager.getFloatPreferences(applicationContext,SavedPrefManager.totalDistance)/1000
                manageContest()


                val totalStepCount = formatDynamicValue(stepsCount.toString(), 2)
                val totalDistanceWalked =convertDistance(distanceWalked.toDouble())

                if(totalStepCount!="0" && lastSteps < totalStepCount) {
                    updateNotification()
                    createStep(stepsCount = totalStepCount, distanceWalked = totalDistanceWalked, unit =unitRequest)
                    lastSteps =  totalStepCount
                }


                val stepsCountForContest = (SavedPrefManager.getFloatPreferences(applicationContext, SavedPrefManager.totalDistanceForContest) * 1.3).roundToInt()
                val distanceWalkedForContest =  SavedPrefManager.getFloatPreferences(applicationContext,SavedPrefManager.totalDistanceForContest)/1000

                val totalStepCountForContest = formatDynamicValue(stepsCountForContest.toString(), 2)
                val totalDistanceWalkedForContest =convertDistance(distanceWalkedForContest.toDouble())



                if(SavedPrefManager.getBooleanPreferences(applicationContext, SavedPrefManager.isContestStart)){
                    if(totalStepCountForContest != "0" && SavedPrefManager.getStringPreferences(applicationContext,SavedPrefManager.CONTEST_ID).toString().isNotEmpty()){
                        createStepForContest(stepsCount = totalStepCountForContest, distanceWalked = totalDistanceWalkedForContest, unit =unitRequestContest)

                    }

                }else{
                    if(totalStepCountForContest != "0" && SavedPrefManager.getStringPreferences(applicationContext,SavedPrefManager.CONTEST_ID).toString().isNotEmpty()){
                        createStepForContest(stepsCount = totalStepCountForContest, distanceWalked = totalDistanceWalkedForContest, unit =unitRequestContest)
                    }
                }

                try {
                    Thread.sleep(10000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
        return START_NOT_STICKY
    }



    private fun createStep(stepsCount:String,distanceWalked:String,unit:String) {
        val queue = Volley.newRequestQueue(this)
        val c = Calendar.getInstance().time

        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate: String = df.format(c)
        val request: StringRequest = object : StringRequest(
            Method.POST, Constants.SERVICE_URL,
            Response.Listener {
            }, Response.ErrorListener { error -> // method to handle errors.


            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()

                params["stepCount"] = stepsCount
                params["distanceWalked"] = distanceWalked
                params["unit"] = unit
                params["date"] = formattedDate
                params["contestId"] = ""

                return params
            }

            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["token"] = SavedPrefManager.getStringPreferences(this@EndlessService, SavedPrefManager.AccessToken).toString()
                return params
            }
        }
        queue.add(request)
    }
    private fun createStepForContest(stepsCount:String,distanceWalked:String,unit:String) {
        val queue = Volley.newRequestQueue(this)
        val c = Calendar.getInstance().time

        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate: String = df.format(c)
        val request: StringRequest = object : StringRequest(
            Method.POST, Constants.SERVICE_URL,
            Response.Listener {
                if(!SavedPrefManager.getBooleanPreferences(this@EndlessService, SavedPrefManager.isContestStart)){
                    SavedPrefManager.stepCountDeleteOfContest(this@EndlessService)
                }
            }, Response.ErrorListener { error -> // // method to handle errors.

                if (error.networkResponse.statusCode == 404){
                    val errorMessage = String(error.networkResponse.data)
                    if (errorMessage.contains("Contest is expired", ignoreCase = true)) {
                        SavedPrefManager.stepCountDeleteOfContest(this@EndlessService)
                    }
                }


            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()


                params["stepCount"] = stepsCount
                params["distanceWalked"] = distanceWalked
                params["unit"] = unit
                params["date"] = formattedDate
                params["contestId"] = "${SavedPrefManager.getStringPreferences(this@EndlessService,SavedPrefManager.CONTEST_ID)}"

                return params
            }

            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["token"] = SavedPrefManager.getStringPreferences(this@EndlessService, SavedPrefManager.AccessToken).toString()
                return params
            }
        }
        queue.add(request)
    }

    private fun formatDynamicValue(value: String, decimalPlaces: Int): String {
        val parts = value.split(".")

        return if (parts.size == 2 && parts[1].length > decimalPlaces) {
            val truncatedDecimal = parts[1].substring(0, decimalPlaces)
            "${parts[0]}.${truncatedDecimal}"
        } else {
            value
        }
    }

    private fun convertDistance(value: Double): String {
        return if (value >= 1.0) {
            unitRequest = "Km"
            unitRequestContest = "Km"
            "%.1f".format(value)
        } else {
            unitRequest = "m"
            unitRequestContest = "m"
            "${(value * 1000).toInt()}"
        }
    }

    override fun onCreate() {
        super.onCreate()

        val notification = createNotification(isVibration = true,)
        startForeground(1, notification)
    }



    override fun onTaskRemoved(rootIntent: Intent) {

        val restartServiceIntent = Intent(applicationContext, EndlessService::class.java).also {
            it.setPackage(packageName)
        }
        val restartServicePendingIntent: PendingIntent = PendingIntent.getService(
            this,
            1,
            restartServiceIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        applicationContext.getSystemService(Context.ALARM_SERVICE)
        val alarmService: AlarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePendingIntent);
    }





    private fun createNotification(isVibration: Boolean): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                notificationChannelId,
                "Endless Service notifications channel",
                NotificationManager.IMPORTANCE_LOW
            ).let {
                it.description = "Endless Service channel"
                it.enableLights(true)
                it.lightColor = Color.RED
                it.enableVibration(isVibration) // Disable vibration
                it.vibrationPattern = null // Set vibration pattern to null
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent: PendingIntent = Intent(this, FragmentHostActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        }

        val stepCount =  (SavedPrefManager.getFloatPreferences(this, SavedPrefManager.totalDistance) * 1.3).roundToInt()
        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(this, notificationChannelId) else Notification.Builder(this)

        return builder
            .setContentTitle("Fit Hun")
            .setContentText("Your steps being count, Total Step: $stepCount")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher_square)
            .setTicker("Ticker text")
            .build()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun manageContest() {

        val startDate =  SavedPrefManager.getStringPreferences(applicationContext,SavedPrefManager.START_DATE).toString()
        val endDate =  SavedPrefManager.getStringPreferences(applicationContext,SavedPrefManager.END_DATE).toString()


        if (startDate.isNotEmpty() && endDate.isNotEmpty()){
            val inputDateTime = LocalDateTime.parse(startDate, DateTimeFormatter.ISO_DATE_TIME)
            val formattedDateTimeString = inputDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))

            val inputDateTimeEnd = LocalDateTime.parse(endDate, DateTimeFormatter.ISO_DATE_TIME)
            val formattedDateTimeStringEnd = inputDateTimeEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))


            // Parse the strings into LocalDateTime objects
            val startDateTime = LocalDateTime.parse(formattedDateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            val endDateTime = LocalDateTime.parse(formattedDateTimeStringEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))

            // Get the current date and time as a formatted string with only hour and minute
            val currentDateTimeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))

//          Start  Time Of contest manage when notification does not come

            if (startDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) == currentDateTimeString && !SavedPrefManager.getBooleanPreferences(this,SavedPrefManager.isContestStart)) {
                SavedPrefManager.stepCountDeleteOfContest(this)
                SavedPrefManager.savePreferenceBoolean(this,SavedPrefManager.isContestStart,true)
            }

//          End Time Of contest manage when notification does not come

            if (endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) == currentDateTimeString && SavedPrefManager.getBooleanPreferences(this,SavedPrefManager.isContestStart)) {
                val stepsCountForContest = (SavedPrefManager.getFloatPreferences(applicationContext, SavedPrefManager.totalDistanceForContest) * 1.3).roundToInt()
                val distanceWalkedForContest =  SavedPrefManager.getFloatPreferences(applicationContext,SavedPrefManager.totalDistanceForContest)/1000

                val totalStepCountForContest = formatDynamicValue(stepsCountForContest.toString(), 2)
                val totalDistanceWalkedForContest =convertDistance(distanceWalkedForContest.toDouble())


                if(SavedPrefManager.getBooleanPreferences(applicationContext, SavedPrefManager.isContestStart)){
                    if(totalStepCountForContest != "0" && SavedPrefManager.getStringPreferences(applicationContext,SavedPrefManager.CONTEST_ID).toString().isNotEmpty()){
                        SavedPrefManager.savePreferenceBoolean(this,SavedPrefManager.isContestStart,false)
                        createStepForContest(stepsCount = totalStepCountForContest, distanceWalked = totalDistanceWalkedForContest, unit =unitRequestContest)
                    }

                }
            }
        }

    }


    private fun updateNotification() {
        val notification = createNotification(isVibration = false)
        notificationManager.notify(1, notification)
    }





}
