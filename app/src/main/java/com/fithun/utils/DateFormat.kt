package com.fithun.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.fithun.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class DateFormat {
    companion object {

        @SuppressLint("SimpleDateFormat")
        fun dateFormatPicker(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("yyyy-MM-dd")
            var parsed: Date? = null
            parsed = try {
                sourceFormat.parse(date!!)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("dd-MM-yyyy")
            result = parsed?.let { destFormat.format(it) }.toString()
            return result
        }

        fun dateFormatPicker1(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("yyyy-MM-dd")
            var parsed: Date? = null
            parsed = try {
                sourceFormat.parse(date!!)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("yyyy-MM-dd")
            result = parsed?.let { destFormat.format(it) }.toString()
            return result
        }


        @SuppressLint("SimpleDateFormat")
        fun convert24To12(time24: String): String {
            var time12 = ""
            try {
                val dateStr =time24
                val df = SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                    Locale.ENGLISH
                )
                df.timeZone = TimeZone.getTimeZone("UTC")
                val date1 = df.parse(dateStr)
                df.timeZone = TimeZone.getDefault()
                val formattedDate = df.format(date1)

                val sdf24 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val sdf12 = SimpleDateFormat("hh:mm a")
                val date = sdf24.parse(formattedDate)
                time12 = sdf12.format(date!!)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return time12
        }


        @SuppressLint("SimpleDateFormat")
        fun dateFormater(time24: String): String {
            var time12 = ""
            try {
                val sdf24 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val sdf12 = SimpleDateFormat("yyyy-MM-dd")
                val date = sdf24.parse(time24)
                time12 = sdf12.format(date!!)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return time12
        }

        fun covertTimeToText(dataDate: String?): String? {
            var convTime: String? = null
            val prefix = ""
            val suffix = "Ago"
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val pasTime = dateFormat.parse(dataDate)
                val nowTime = Date()
                val dateDiff = nowTime.time - pasTime.time
                val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
                val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
                val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
                val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
                if (second < 60) {
                    convTime = "$second Seconds $suffix"
                } else if (minute < 60) {
                    convTime = "$minute Minutes $suffix"
                } else if (hour < 24) {
                    convTime = "$hour Hours $suffix"
                } else if (day >= 7) {
                    convTime = if (day > 360) {
                        (day / 360).toString() + " Years " + suffix
                    } else if (day > 30) {
                        (day / 30).toString() + " Months " + suffix
                    } else {
                        (day / 7).toString() + " Week " + suffix
                    }
                } else if (day < 7) {
                    convTime = "$day Days $suffix"
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return convTime
        }


        var date: Date? = null
        @RequiresApi(Build.VERSION_CODES.O)
        fun dateForCountdown(date: String): Pair<String, String> {
            try {
                val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                df.timeZone = TimeZone.getTimeZone("UTC")

                val dateObj = df.parse(date)

                val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                val timeFormatter = SimpleDateFormat("hh:mm:ss a", Locale.ENGLISH)

                val formattedDate = dateFormatter.format(dateObj)
                val formattedTime = timeFormatter.format(dateObj)

                return Pair(formattedDate, formattedTime)
            }catch (e:Exception){
                e.printStackTrace()
            }
            return  Pair("","")
        }
        @RequiresApi(Build.VERSION_CODES.O)
        fun dateForForHistory(date: String): Pair<String, String> {
            try {
                val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                df.timeZone = TimeZone.getTimeZone("UTC")

                val dateObj = df.parse(date)

                val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
                val timeFormatter = SimpleDateFormat("hh:mm a", Locale.ENGLISH)

                val formattedDate = dateFormatter.format(dateObj)
                val formattedTime = timeFormatter.format(dateObj)

                return Pair(formattedDate, formattedTime)
            }catch (e:Exception){
                e.printStackTrace()
            }
            return  Pair("","")
        }


        fun convert12HourTo24Hour(time12Hour: String): String {
            val inputFormat = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
            val outputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

            try {
                val date = inputFormat.parse(time12Hour)
                return outputFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return "" // Return an empty string or handle the error as needed
        }
        fun convert12HourTo24HourWithTime(time12Hour: String): String {
            val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("HH:mm a", Locale.getDefault())

            try {
                val date = inputFormat.parse(time12Hour)
                return outputFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return "" // Return an empty string or handle the error as needed
        }


        fun calculateRemainingTime(targetTime: String): Long {
            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

            // Get the current time
            val currentTime = Calendar.getInstance()
            val currentDateString = sdf.format(currentTime.time)
            val currentDate = sdf.parse(currentDateString)

            // Parse the target time
            val targetDate = sdf.parse(targetTime)

            // Calculate the remaining time
            val remainingTime = targetDate!!.time - currentDate!!.time

            return if (remainingTime >= 0) remainingTime else 0
        }


        fun contestDateFormat(inputDate: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

            try {
                val date = inputFormat.parse(inputDate)
                return outputFormat.format(date!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return "" // Return an empty string or handle the error as needed
        }


    }






}