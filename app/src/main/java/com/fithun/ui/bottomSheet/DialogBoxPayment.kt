package com.fithun.ui.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.airbnb.lottie.LottieAnimationView
import com.fithun.R
import com.fithun.clickInterfaces.PaymentDoneListener
import com.fithun.utils.AndroidExtension.initLoader
import com.fithun.utils.setSafeOnClickListener

class DialogBoxPayment(private var paymentDoneListener: PaymentDoneListener, fromScreen: String) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.order_review_confirmation, container, false)
        val yesBtn = view.findViewById<TextView>(R.id.Continue_button_popup)
        val paymentLottie = view.findViewById<LottieAnimationView>(R.id.lottie_payment)
        paymentLottie.initLoader(true)
        yesBtn.setSafeOnClickListener {
            paymentDoneListener.paymentDone()
            dismiss()


        }
        return view
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.setCancelable(false)
    }

}