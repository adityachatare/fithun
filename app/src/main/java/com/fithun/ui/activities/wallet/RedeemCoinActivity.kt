package com.fithun.ui.activities.wallet

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.fithun.R
import com.fithun.databinding.ActivityRedeemCoinBinding
import com.fithun.ui.activities.common.OtpVerificationActivity
import com.fithun.utils.FormValidations
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.utils.displayToast
import com.fithun.viewModel.RedeemViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RedeemCoinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRedeemCoinBinding
    private val viewModel: RedeemViewModel by viewModels()
    var walletAmount = ""
    var amount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding  = ActivityRedeemCoinBinding.inflate(layoutInflater)
        window.attributes.windowAnimations= R.style.Fade
        setContentView(binding.root)

        //getting intent data
        intent.getStringExtra("wallet")?.let { walletAmount = it }
        intent.getIntExtra("amount",0).let { amount = it }
        binding.walletBalance.text = "₩" + walletAmount.toFloat()
        if(amount!=0) {
            binding.amount.setText(amount.toString())
        }

        val upiId =  SavedPrefManager.getStringPreferences(this,SavedPrefManager.UPI_ID).toString()

        if (upiId.isNotEmpty()){
            binding.upiId.text =  upiId
        }


        binding.upiId.setOnClickListener {
            if (upiId.isNotEmpty()){
                return@setOnClickListener
            }
            startActivity(Intent(this,VerifyAccountActivity::class.java))
        }

        addTextWatcher()
        onClick()
    }

    private fun addTextWatcher(){
        binding.amount.addTextChangedListener(textWatcherValidation)
        binding.upiId.addTextChangedListener(textWatcherValidation)
        binding.mobileNumber.addTextChangedListener(textWatcherValidation)
    }

    private val textWatcherValidation = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                if (s.length == 1 && s.toString().startsWith("0")) {
                    s.clear()
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val currentField =
                if (s === binding.amount.text) "Amount" else if (s === binding.upiId.text) "upiId"
                else if (s === binding.accountHolderName.text) "Name" else if (s === binding.mobileNumber.text) "Mobile" else ""

            FormValidations.redeemCoin(binding.llAmount, binding.amount, binding.txtAmount,binding.upiIdll, binding.upiId, binding.txtUpiId, binding.mobilell, binding.mobileNumber, binding.txtMobile,
                field = currentField )
            if(binding.amount.text.isNotEmpty() && binding.upiId.text.isNotEmpty() && binding.mobileNumber.text.isNotEmpty()){
                binding.NextButton.isVisible = true
                binding.NextButtonDisable.isVisible = false
            }else{
                binding.NextButton.isVisible = false
                binding.NextButtonDisable.isVisible = true
            }
        }
    }

    private fun onClick(){
        binding.NextButtonDisable.setOnClickListener {
            FormValidations.redeemCoin(binding.llAmount, binding.amount, binding.txtAmount,binding.upiIdll, binding.upiId, binding.txtUpiId, binding.mobilell, binding.mobileNumber, binding.txtMobile,
                field = "" )
        }

        binding.NextButton.setOnClickListener {
            sendOtpOnMobile()
        }

        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }
    }

    private fun sendOtpOnMobile(){
        viewModel.sendOtpOnMobileApi(SavedPrefManager.getStringPreferences(this, SavedPrefManager.AccessToken).toString())
        lifecycleScope.launch {
            viewModel.resendOtpApiResponseData.collect { response ->

                when (response) {
                    is Resource.Success -> {
                        if(response.data!!.responseCode == 200) {
                            try {
                                Progress.stop()
                                displayToast(response.data.responseMessage)
                                val intent =  Intent(this@RedeemCoinActivity, OtpVerificationActivity::class.java)
                                intent.putExtra("amount", binding.amount.text.toString())
                                intent.putExtra("upiId", binding.upiId.text.toString())
                                intent.putExtra("name", binding.accountHolderName.text.toString())
                                intent.putExtra("mobileNumber", binding.mobileNumber.text.toString())
                                startActivity(intent)
                                finishAfterTransition()


                            } catch (e:Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        Progress.stop()
                    }

                    is Resource.Loading -> {
                        Progress.start(this@RedeemCoinActivity)
                    }

                    is Resource.Empty -> {
                        Progress.stop()
                    }
                }
            }
        }
    }

}