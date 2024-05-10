package com.fithun.ui.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.fithun.R
import com.fithun.clickInterfaces.ContestEndClick
import com.fithun.utils.setSafeOnClickListener

class ContestEndDialog(val title:String, val description:String,val click:ContestEndClick) : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.wallet_rules_layout, container, false)

        val popupCross = view.findViewById<ImageView>(R.id.popup_cross)
        val titleTv = view.findViewById<TextView>(R.id.EnquiryTV)
        val descriptionTv = view.findViewById<TextView>(R.id.description)


        titleTv.text =  title
        descriptionTv.text =  description


        popupCross.setSafeOnClickListener {
            click.contestEnds()
            dismiss()
        }

        return view
    }


    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }




}