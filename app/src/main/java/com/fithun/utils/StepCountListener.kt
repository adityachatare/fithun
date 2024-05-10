package com.fithun.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class StepCountListener(private val context: Context) : LocationListener,SensorEventListener {

    private var previousLocation: Location? = null
    private var stepTimer = 0
    private var unitRequest = ""
    private var unitRequestForContest  = ""

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var gyroscope: Sensor? = null


    private var lastAcceleration: FloatArray? = null
    private var lastTimestamp: Long = 0
    private var motionSpeed: Float = 0.0f

    private var gravity: FloatArray? = null
    private var stationaryStartTime: Long = 0
    private var isStationary: Boolean = false


    init {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0f, this)

        } else {
            Toast.makeText(context, "Please enable gps...", Toast.LENGTH_SHORT).show()
        }










    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onLocationChanged(location: Location) {

        val speed = location.speed
        SavedPrefManager.saveFloatPreferences(context,SavedPrefManager.Speed, speed)





        if (previousLocation == null){
            previousLocation  = location
        }


        if (speed in 0.5..1.9) {
            sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

            accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            gyroscope = sensorManager?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)



            sensorManager?.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
            sensorManager?.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)


            if (motionSpeed < 12f) {
                return
            }




            stepTimer += 1
            if (previousLocation != null) {


                val calculateDistanceInMeters = calculateDistanceInMeters(previousLocation!!, location)
                if (stepTimer ==  10){
                    stepTimer = 0

                    previousLocation = if (calculateDistanceInMeters in 10.5..20.0) {
                        val newTotalDistance  = SavedPrefManager.getFloatPreferences(context,SavedPrefManager.totalDistance)  + calculateDistanceInMeters
                        SavedPrefManager.saveFloatPreferences(context,SavedPrefManager.totalDistance,newTotalDistance.toFloat())
                        SavedPrefManager.saveStringPreferences(context,SavedPrefManager.Unit,unitRequest)

                        if(SavedPrefManager.getBooleanPreferences(context, SavedPrefManager.isContestStart)){
                            val newTotalDistanceForContest  = SavedPrefManager.getFloatPreferences(context,SavedPrefManager.totalDistanceForContest)  + calculateDistanceInMeters
                            SavedPrefManager.saveFloatPreferences(context,SavedPrefManager.totalDistanceForContest,newTotalDistanceForContest.toFloat())
                            SavedPrefManager.saveStringPreferences(context,SavedPrefManager.unitForContest,unitRequestForContest)
                        }else{
                            SavedPrefManager.savePreferenceBoolean(context,SavedPrefManager.isContestStart,false)
                            SavedPrefManager.saveFloatPreferences(context,SavedPrefManager.totalDistanceForContest,0f)
                            SavedPrefManager.saveStringPreferences(context,SavedPrefManager.unitForContest,"")
                        }

                        location

                    }else{
                        location

                    }
                }
            }
        }else {
            SavedPrefManager.deleteStepsCounts(context)
            previousLocation  = location

        }
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

    }

    override fun onProviderEnabled(provider: String) {
        Log.d("TAG_COMMON", "onProviderEnabled: $provider")
        // Handle provider enabled
    }

    override fun onProviderDisabled(provider: String) {
        // Handle provider disabled
    }



    private fun calculateDistanceInMeters(location1: Location, location2: Location): Double {
        val earthRadius = 6371000.0 // Earth's radius in meters

        val lat1 = Math.toRadians(location1.latitude)
        val lon1 = Math.toRadians(location1.longitude)
        val lat2 = Math.toRadians(location2.latitude)
        val lon2 = Math.toRadians(location2.longitude)

        val dLat = lat2 - lat1
        val dLon = lon2 - lon1

        val a = sin(dLat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }



    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val acceleration = event.values.clone()
            val timestamp = event.timestamp

            if (lastAcceleration != null) {
                val dt = (timestamp - lastTimestamp) * NS2S // Convert nanoseconds to seconds

                // Apply high-pass filter to remove gravity component
                val filteredAcceleration = highPassFilter(acceleration, gravity ?: acceleration)

                // Calculate acceleration magnitude
                val accelerationMagnitude = calculateAccelerationMagnitude(filteredAcceleration)

                // Check if the device is stationary
                if (accelerationMagnitude <= STATIONARY_THRESHOLD) {
                    // Device is considered stationary
                    if (!isStationary) {
                        // Device just became stationary
                        isStationary = true
                        stationaryStartTime = System.currentTimeMillis()
                    } else if (System.currentTimeMillis() - stationaryStartTime >= STATIONARY_DURATION) {
                        // Device has been stationary for the specified duration
                        motionSpeed = 0.0f // Reset motion speed


                    }
                } else {
                    // Device is moving
                    isStationary = false
                    // Integrate to get velocity (motion speed)
                    motionSpeed += accelerationMagnitude * dt

                }

                // Update last acceleration and timestamp
                lastAcceleration = acceleration
                lastTimestamp = timestamp
            } else {
                // Initialize last acceleration and timestamp
                lastAcceleration = acceleration
                lastTimestamp = timestamp
            }

            // Update gravity for next iteration (sensor fusion)
            gravity = lowPassFilter(acceleration, gravity ?: acceleration)

        }

        if (event?.sensor?.type == Sensor.TYPE_STEP_DETECTOR) {
            Toast.makeText(context, "Steps", Toast.LENGTH_SHORT).show()
        }
        
        
    }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }

    private fun calculateAccelerationMagnitude(acceleration: FloatArray): Float {
        return sqrt(
            acceleration[0].toDouble().pow(2.0) +
                    acceleration[1].toDouble().pow(2.0) +
                    acceleration[2].toDouble().pow(2.0)
        ).toFloat()
    }

    private fun highPassFilter(input: FloatArray, gravity: FloatArray): FloatArray {
        return floatArrayOf(
            input[0] - gravity[0],
            input[1] - gravity[1],
            input[2] - gravity[2]
        )
    }

    private fun lowPassFilter(input: FloatArray, gravity: FloatArray): FloatArray {
        val alpha = 0.8f
        val output = FloatArray(3)
        output[0] = alpha * gravity[0] + (1 - alpha) * input[0]
        output[1] = alpha * gravity[1] + (1 - alpha) * input[1]
        output[2] = alpha * gravity[2] + (1 - alpha) * input[2]
        return output
    }



    companion object {
        private const val NS2S = 1.0f / 1000000000.0f
        private const val STATIONARY_THRESHOLD = 0.1f
        private const val STATIONARY_DURATION = 3000
    }




}
