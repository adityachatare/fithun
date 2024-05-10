package com.fithun.permission
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionManager {

    private const val LOCATION_PERMISSION_REQUEST_CODE = 123
    private const val CAMERA_PERMISSION_REQUEST_CODE = 124
    private const val GALLERY_PERMISSION_REQUEST_CODE = 125
    private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 126
    private const val ALARM_PERMISSION_REQUEST_CODE = 130

    private const val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
    private const val cameraPermission = Manifest.permission.CAMERA
    private const val postNotificationPermission = Manifest.permission.POST_NOTIFICATIONS

    private const val galleryPermission = Manifest.permission.READ_EXTERNAL_STORAGE


    private const val alarmPermission = Manifest.permission.USE_EXACT_ALARM

    private val backgroundPermissionRequestCode = 123


    private val allPermissions = arrayOf(
        postNotificationPermission,
        locationPermission,
        cameraPermission,
        galleryPermission,
        alarmPermission,
    )

    fun checkAndRequestPermissions(activity: Activity, retryCallback: ((String) -> Unit)? = null) {
        val permissionsToRequest = allPermissions.filter { !isPermissionGranted(activity, it) }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                activity,
                permissionsToRequest.toTypedArray(),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // All permissions are already granted
            // Proceed with your logic
        }
    }




    private fun isPermissionGranted(activity: Activity, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun requestBackgroundPermission(context:Activity) {
        ActivityCompat.requestPermissions(
            context,
            arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
            backgroundPermissionRequestCode
        )
    }



    fun handlePermissionResult(
        activity: Activity,
        retryCallback: ((String) -> Unit)? = null,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                for (i in permissions.indices) {
                    if (permissions[i] == locationPermission &&
                        (grantResults[i] == PackageManager.PERMISSION_DENIED ||
                                grantResults[i] == PackageManager.PERMISSION_GRANTED)
                    ) {
                        // Location permission denied or granted
                        retryCallback?.invoke(locationPermission)
                        return
                    }
                }
            }
            CAMERA_PERMISSION_REQUEST_CODE -> {
                for (i in permissions.indices) {
                    if (permissions[i] == cameraPermission && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        retryCallback?.invoke(cameraPermission)
                        return
                    }
                }
            }
            GALLERY_PERMISSION_REQUEST_CODE -> {
                for (i in permissions.indices) {
                    if (permissions[i] == galleryPermission && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        retryCallback?.invoke(galleryPermission)
                        return
                    }
                }
            }
            NOTIFICATION_PERMISSION_REQUEST_CODE -> {
                for (i in permissions.indices) {
                    if (permissions[i] == postNotificationPermission && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        retryCallback?.invoke(postNotificationPermission)
                        return
                    }
                }
            }
            ALARM_PERMISSION_REQUEST_CODE -> {
                for (i in permissions.indices) {
                    if (permissions[i] == alarmPermission && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        retryCallback?.invoke(alarmPermission)
                        return
                    }
                }
            }


        }
    }
}
