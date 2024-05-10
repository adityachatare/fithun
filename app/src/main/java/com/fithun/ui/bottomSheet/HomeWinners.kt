package com.fithun.ui.bottomSheet

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.fithun.R
import com.fithun.clickInterfaces.WalletFilter
import com.fithun.utils.AndroidExtension
import com.fithun.utils.DateFormat
import com.fithun.utils.displayToast
import com.fithun.utils.setSafeOnClickListener
import java.util.Calendar

class HomeWinners(val click:WalletFilter): BottomSheetDialogFragment() {

    private val cal = Calendar.getInstance()
    private var yearset = 0
    private var monthset = 0
    var day = 0
    private var datePicker: DatePickerDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_winner_filter, container, false)

        val cancelClick =  view.findViewById<LinearLayout>(R.id.CancelClick)!!
        val applyClick =  view.findViewById<RelativeLayout>(R.id.applyClick)!!
        val startDate =  view.findViewById<LinearLayout>(R.id.startDate)!!
        val endDate =  view.findViewById<LinearLayout>(R.id.endDate)!!
        val fromET =  view.findViewById<TextView>(R.id.fromET)!!
        val toDateET =  view.findViewById<TextView>(R.id.toDateET)!!
        val walletSpinner =  view.findViewById<Spinner>(R.id.walletSpinner)!!


        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DAY_OF_MONTH)



        startDate.setSafeOnClickListener {
            datePicker = DatePickerDialog(
                requireContext(),
                R.style.DatePickerTheme,{ _, year, monthOfYear, dayOfMonth ->
                    cal.set(year, monthOfYear, dayOfMonth)
                    toDateET.text = ""
                    fromET.text = DateFormat.dateFormatPicker1("$year-${monthOfYear + 1}-$dayOfMonth")

                    yearset = year
                    monthset = monthOfYear
                    day = dayOfMonth
                },
                year,
                month,
                date
            )
            datePicker!!.datePicker.maxDate = System.currentTimeMillis() - 1000
            datePicker!!.show()
        }



        endDate.setSafeOnClickListener {
            if(fromET.text.isEmpty()){
                AndroidExtension.alertBox("Please select start date.",requireContext())
                return@setSafeOnClickListener
            }

            datePicker = DatePickerDialog(
                requireContext(),
                R.style.DatePickerTheme, { _, year, monthOfYear, dayOfMonth ->

                    toDateET.text = DateFormat.dateFormatPicker1("$year-${monthOfYear + 1}-$dayOfMonth")

                },
                year,
                month,
                date
            )
            datePicker!!.datePicker.minDate = cal.timeInMillis


            datePicker!!.show()
        }

        cal.set(yearset, monthset, day)



        cancelClick.setOnClickListener {
            dismiss()
        }

        applyClick.setSafeOnClickListener {

            if (walletSpinner.selectedItem.toString() == "Choose" && fromET.text.isEmpty() && toDateET.text.isEmpty()){
                displayToast("Please select date or type")
                return@setSafeOnClickListener
            }

            click.filterWalletClick(fromET.text.toString(),toDateET.text.toString(),walletSpinner.selectedItem.toString(),)
            dismiss()
        }


        return view
    }






    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.home_winner_filter, null)
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