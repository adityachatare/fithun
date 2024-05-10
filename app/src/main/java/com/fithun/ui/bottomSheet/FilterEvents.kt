package com.fithun.ui.bottomSheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Spinner
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.fithun.R
import com.fithun.clickInterfaces.EventFilterClick
import com.fithun.utils.displayToast
import com.fithun.utils.setSafeOnClickListener

class FilterEvents(val applyFilter:EventFilterClick): BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.contest_filter, container, false)

        val cancelClick =  view.findViewById<LinearLayout>(R.id.CancelClick)!!
        val applyClick =  view.findViewById<RelativeLayout>(R.id.applyClick)!!
        val contestType =  view.findViewById<Spinner>(R.id.contestType)!!
        val selectDay =  view.findViewById<Spinner>(R.id.selectDay)!!
        val entryFee =  view.findViewById<Spinner>(R.id.entryFee)!!
        val selectSpot =  view.findViewById<Spinner>(R.id.selectSpot)!!

        cancelClick.setSafeOnClickListener {
            dismiss()
        }
        applyClick.setSafeOnClickListener {
            if (selectDay.selectedItem.toString() == "Select Day"
                && entryFee.selectedItem.toString() == "Select Entry Fee" && selectSpot.selectedItem.toString() == "Select Spot"){
                displayToast("Please select date or type")
                return@setSafeOnClickListener
            }
            applyFilter.filterEvents(contestType.selectedItem.toString(),selectDay.selectedItem.toString()
                ,entryFee.selectedItem.toString(),selectSpot.selectedItem.toString())
            dismiss()
        }


        return view
    }






    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.contest_filter, null)
        dialog.setContentView(contentView)




        contentView.post {
            val contentHeight = contentView.height
            val peekHeightFraction = 0.75
            val peekHeight = (contentHeight * peekHeightFraction).toInt()

            val params = (contentView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            if (behavior != null && behavior is BottomSheetBehavior) {
                behavior.peekHeight = peekHeight
            }
        }
    }


    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

}