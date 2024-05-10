package com.fithun.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.LinearLayoutCompat
import com.airbnb.lottie.LottieAnimationView
import com.fithun.R
import com.fithun.clickInterfaces.CheckVersionStatus
import com.fithun.clickInterfaces.CommonClick
import com.fithun.clickInterfaces.LocationDenyInterface

object AndroidExtension {
    fun showDialogLogout(context: Context, click: CommonClick) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.logout_popup)
        val yesBtn = dialog.findViewById<RelativeLayout>(R.id.logoutClick)
        val noBtn = dialog.findViewById<LinearLayout>(R.id.CancelClick)

        dialog.setCancelable(false)
        noBtn.setSafeOnClickListener {
            dialog.dismiss()
        }
        yesBtn.setSafeOnClickListener {
            dialog.cancel()
            click.applyClick()

        }
        dialog.show()
        val window = dialog.window
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        )
    }
    fun showDialogDelete(context: Context, click: CommonClick) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.delete_popup)
        val yesBtn = dialog.findViewById<RelativeLayout>(R.id.deleteClick)
        val noBtn = dialog.findViewById<LinearLayout>(R.id.CancelClick)
        dialog.window!!.attributes.windowAnimations = R.style.Fade

        dialog.setCancelable(false)
        noBtn.setSafeOnClickListener {
            dialog.dismiss()
        }
        yesBtn.setSafeOnClickListener {
            dialog.cancel()
            click.applyClick()

        }
        dialog.show()
        val window = dialog.window
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        )
    }
    fun locationAllowPermission(message: String, context: Context, isForLocation:String, location: LocationDenyInterface) {
        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        builder.setIcon(R.drawable.app_logo)
        builder.setTitle("Spirit of Fitness")
        builder.setMessage(message)


        // Create a positive button with the custom text color
        builder.setPositiveButton("Go") { _, _ ->
            alertDialog!!.dismiss()
            location.openSettings(isForLocation)
        }

        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog.show()
    }

    fun alertBox(message: String, context: Context) {
        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        builder.setIcon(R.drawable.app_logo)
        builder.setTitle("Spirit of Fitness")
        builder.setMessage(message)

        builder.setPositiveButton("ok") { dialogInterface, which ->
            alertDialog!!.dismiss()
        }
        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog.dismiss()
        alertDialog.show()
    }

    var alertDialog: AlertDialog? = null
    fun alertBoxFinishActivity(message: String, context: Context, activity: Activity) {
        alertDialog?.dismiss()
        val builder = AlertDialog.Builder(context)
        builder.setIcon(R.drawable.app_logo)
        builder.setTitle("Spirit of Fitness")
        builder.setMessage(message)


        builder.setPositiveButton("ok") { _, _ ->
            activity.finishAfterTransition()
            alertDialog!!.dismiss()
        }
        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog!!.show()
    }


    fun alertBoxFinish(message: String, context: Context, click: CommonClick) {
        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)



        builder.setPositiveButton("ok") { _, _ ->
            click.applyClick()
            alertDialog!!.dismiss()
        }
        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog!!.show()
    }


    fun alertBoxDeleteItem(message: String, context: Context, click: CommonClick) {
        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        builder.setIcon(R.drawable.app_logo)
        builder.setTitle("Spirit of Fitness")
        builder.setMessage(message)



        builder.setPositiveButton("Yes") { _, _ ->
            click.applyClick()
            alertDialog!!.dismiss()
        }

        builder.setNegativeButton("Cancel") { _, _ ->
            alertDialog!!.dismiss()
        }
        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog.show()
    }


    fun LottieAnimationView.initLoader(isLoading: Boolean) {
        visibility = if (isLoading) {
            playAnimation()
            View.VISIBLE
        } else {
            pauseAnimation()
            animation?.reset()
            View.GONE
        }
    }

    private var currentDialog: AlertDialog? = null

    fun checkVersionStatus(message: String, context: Context,click: CheckVersionStatus) {
        currentDialog?.dismiss() // Dismiss the current dialog if it's shown

        val builder = AlertDialog.Builder(context)
        builder.setIcon(R.mipmap.ic_launcher_square)
        builder.setTitle("Fit-hun")
        builder.setMessage(message)
        builder.setPositiveButton("Update Now") { _, _ ->
            click.checkStatus()
        }


        currentDialog = builder.create()
        currentDialog!!.setCancelable(false)
        currentDialog!!.show()
    }


    fun manageMessages(e: Throwable):String{
        val errorMessage = if (e.message?.contains("failed to connect") == true ||
            e.message?.contains("Connection timed out") == true) {
            "Server not responding"
        } else {
            e.message ?: "Unknown error occurred"
        }

        return  errorMessage

    }








}