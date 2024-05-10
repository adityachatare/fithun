package com.fithun.utils

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import com.fithun.databinding.FragmentHomeBinding
import kotlin.math.abs
import kotlin.math.roundToInt

class SpeedRotationUpdater(val context: Context, val binding: FragmentHomeBinding, delayMillis: Int) {

    private val handler = Handler(Looper.getMainLooper())
    private var minSpeed = 0f
    private var maxSpeed = 6f
    private val minRotation = -130f
    private val maxRotation = 130f
    private var finalRotationValue = -130f
    private var totalDistanceKm = 0.0
    private var unitRequest = ""
    fun startUpdating() {
        handler.post(updateClockRunnable)
    }

    fun stopUpdating() {
        handler.removeCallbacks(updateClockRunnable)
    }

    private val updateClockRunnable: Runnable = object : Runnable {
        @RequiresApi(api = Build.VERSION_CODES.O)
        override fun run() {
            try {
                uiCalculateRotationValue(binding)

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            handler.postDelayed(this, delayMillis.toLong())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun uiCalculateRotationValue(binding: FragmentHomeBinding) {

        val currentSpeed =  SavedPrefManager.getFloatPreferences(context,SavedPrefManager.Speed)
        val kmPerHourSpeed = currentSpeed * 3.6

        val totalDistanceKm = SavedPrefManager.getFloatPreferences(context,SavedPrefManager.totalDistance)/1000
        val stepsCount = (SavedPrefManager.getFloatPreferences(context, SavedPrefManager.totalDistance) * 1.3).roundToInt()

        this.finalRotationValue = calculateRotationValue(kmPerHourSpeed.toFloat())

        binding.totalKm.text = "${convertDistance(totalDistanceKm.toDouble())} $unitRequest"
        binding.stepsCount.text = formatDynamicValue(stepsCount.toString(), 2)

        if (currentSpeed.toDouble() != 0.0){

            binding.kmPerHour.text = formatDynamicValue(kmPerHourSpeed.toString(), 2)
            if(kmPerHourSpeed >= 0.3) {
                binding.speedUnit.text = "Km/h"
            } else {
                binding.speedUnit.text = "m/s"
            }


            // Animate the rotation
            animateRotation(this.finalRotationValue)
        }else{
            binding.kmPerHour.text = "0.0"
            binding.speedUnit.text = "m/s"
            this.finalRotationValue = calculateRotationValue(0f)
            animateRotation(this.finalRotationValue)
        }


    }






    private fun formatDynamicValue(value: String, decimalPlaces: Int): String {
        val parts = value.split(".")

        return if (parts.size == 2 && parts[1].length > decimalPlaces) {
            val truncatedDecimal = parts[1].substring(0, decimalPlaces)
            "${parts[0]}.${truncatedDecimal}"
        } else {
            value // The value is already properly formatted
        }
    }

    private fun calculateRotationValue(speed: Float): Float {
        val range = maxRotation - minRotation
        val adjustedSpeed = (speed - minSpeed) / maxSpeed // Normalize the speed to [0, 1]
        return (adjustedSpeed * range) + minRotation
    }

    private fun animateRotation(finalRotationValue: Float) {
        val currentRotation = binding.rotationHandImage.rotation
        val rotationDifference = abs(finalRotationValue - currentRotation)
        val fullRotationRange = 130 - (-130)
        val halfRotationRange = fullRotationRange / 2

        val animationDuration = (rotationDifference / halfRotationRange) * 1000L

        val animator = ValueAnimator.ofFloat(currentRotation, finalRotationValue)
        animator.duration = animationDuration.toLong()
        animator.addUpdateListener { animation ->
            val newRotation = animation.animatedValue as Float
            val upperOffsetYExtender = -27.0
            binding.rotationHandImage.pivotX = binding.rotationHandImage.width / 2f
            binding.rotationHandImage.pivotY = (binding.rotationHandImage.height + upperOffsetYExtender).toFloat()
            binding.rotationHandImage.rotation = newRotation
        }
        animator.start()
    }


    private fun convertDistance(value: Double): String {
        return if (value >= 1.0) {
            unitRequest = "Km"
            "${"%.1f".format(value)}"
        } else {
            unitRequest = "m"
            "${(value * 1000).toInt()}"
        }
    }
}
