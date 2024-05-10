package com.fithun.backgroundService

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fithun.api.Constants
import com.fithun.utils.CommonFunctions
import com.fithun.utils.SavedPrefManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

class ClearPreferencesWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    private var unitRequest = ""

    override fun doWork(): Result {
        val stepsCount = (SavedPrefManager.getFloatPreferences(applicationContext, SavedPrefManager.totalDistance) * 1.3).roundToInt()
        val distanceWalked =  SavedPrefManager.getFloatPreferences(applicationContext,SavedPrefManager.totalDistance)/1000
//        val unitRequest  = SavedPrefManager.getStringPreferences(applicationContext,SavedPrefManager.Unit).toString()
        val unitRequest  = CommonFunctions.convertDistanceKM(distanceWalked.toDouble());

        val totalStepCount = formatDynamicValue(stepsCount.toString())
        val totalDistanceWalked =convertDistance(distanceWalked.toDouble())

        if(totalStepCount!="0") {
            Log.d("api called", "success")
            createStepForDay(stepsCount = totalStepCount, distanceWalked = totalDistanceWalked, unit =unitRequest,context=applicationContext)
        }
        return Result.success()
    }

    private fun createStepForDay(stepsCount:String, distanceWalked:String, unit:String, context: Context?) {
        val queue = Volley.newRequestQueue(context)
        val c = Calendar.getInstance().time

        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate: String = df.format(c)
        val request: StringRequest = object : StringRequest(
            Method.POST, Constants.SERVICE_URL,
            Response.Listener {

                SavedPrefManager.clearPrefsForNextDay(context)
                Log.d("createStepForDay", "createStepForDay: Done")
            }, Response.ErrorListener {   // method to handle errors.
                /*if (error.networkResponse.statusCode == 404){
                    val errorMessage = String(error.networkResponse.data)

                }*/

            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()

                params["stepCount"] = stepsCount
                params["distanceWalked"] = distanceWalked
                params["unit"] = unit
                params["date"] = formattedDate
                params["contestId"] = ""
                Log.d("stepsCount", stepsCount)
                Log.d("distanceWalked", distanceWalked)
                Log.d("formattedDate", formattedDate)
                Log.d("unit", unit)
                return params
            }

            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["token"] = SavedPrefManager.getStringPreferences(context, SavedPrefManager.AccessToken).toString()
                return params
            }
        }
        queue.add(request)
    }



    private fun formatDynamicValue(value: String): String {
        val parts = value.split(".")

        return if (parts.size == 2 && parts[1].length > 2) {
            val truncatedDecimal = parts[1].substring(0, 2)
            "${parts[0]}.${truncatedDecimal}"
        } else {
            value
        }
    }

    private fun convertDistance(value: Double): String {
        return if (value >= 1.0) {
            unitRequest = "Km"
            "%.1f".format(value)
        } else {
            unitRequest = "m"
            "${(value * 1000).toInt()}"
        }
    }
}
