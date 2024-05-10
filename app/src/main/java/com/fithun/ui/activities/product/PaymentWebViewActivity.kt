package com.fithun.ui.activities.product

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.fithun.R
import com.fithun.api.Constants
import com.fithun.clickInterfaces.PaymentDoneListener
import com.fithun.databinding.ActivityPaymentWebViewBinding
import com.fithun.ui.bottomSheet.DialogBoxPayment
import com.fithun.utils.displayToast

class PaymentWebViewActivity : AppCompatActivity(),PaymentDoneListener{

    private lateinit var binding: ActivityPaymentWebViewBinding

    var orderId = ""
    var fromScreen = ""
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityPaymentWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations =  R.style.Fade
        intent.getStringExtra("orderId")?.let { orderId =  it }
        intent.getStringExtra("fromScreen")?.let { fromScreen =  it }

        openUrl()
    }


    private fun openUrl() {
        binding.paymentWebView.loadUrl(orderId)
        binding.paymentWebView.webViewClient = MyWebViewClient(binding.progressBar)
        binding.paymentWebView.clearCache(true)
        binding.paymentWebView.requestFocus()
        binding.paymentWebView.settings.javaScriptEnabled = true
        binding.paymentWebView.settings.lightTouchEnabled = true
        binding.paymentWebView.settings.domStorageEnabled = true
        binding.paymentWebView.isSoundEffectsEnabled = true
        binding.paymentWebView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        binding.paymentWebView.settings.useWideViewPort = true
        binding.paymentWebView.settings.mediaPlaybackRequiresUserGesture = true

    }


    inner class MyWebViewClient(private val progressBar: ProgressBar) : WebViewClient() {
        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            progressBar.visibility = View.VISIBLE
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            progressBar.visibility = View.GONE
            if (url.contains("${Constants.PAYMENT_URl}/api/payment/cancel")) {
                displayToast("cancel")
            } else if (url.contains("${Constants.PAYMENT_URl}/api/payment/success")) {
                supportFragmentManager.let { DialogBoxPayment(this@PaymentWebViewActivity,fromScreen).show(it, "MyCustomFragment") }

            } else if (url.contains("${Constants.PAYMENT_URl}/api/payment/failed")) {
                displayToast("failed")

            }
        }


        init {
            progressBar.visibility = View.VISIBLE
        }
    }

    override fun paymentDone() {

        if (fromScreen =="Cart"){
            startActivity(Intent(this,OrderHistoryActivity::class.java))
            finishAfterTransition()
        }else{
            val data = Intent()
            data.putExtra("isPaymentDone", true)
            setResult(Activity.RESULT_OK, data)
            finishAfterTransition()
        }


    }

    override fun failedDone() {
        finish()
    }

    override fun cancelPayment() {
        finish()
    }


}