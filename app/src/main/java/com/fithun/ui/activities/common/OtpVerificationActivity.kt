package com.fithun.ui.activities.common

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fithun.R
import com.fithun.databinding.ActivityOtpVerificationBinding
import com.fithun.utils.AndroidExtension
import com.fithun.utils.GenericKeyEvent
import com.fithun.utils.GenericTextWatcher
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.utils.setSafeOnClickListener
import com.fithun.viewModel.RedeemViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@AndroidEntryPoint
class OtpVerificationActivity : AppCompatActivity() {

    private val viewModel: RedeemViewModel by viewModels()

    private lateinit var binding: ActivityOtpVerificationBinding

    var amount:String? = ""
    var upiId:String? = ""
    var name:String? = ""
    var number:String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        binding.et1.addTextChangedListener(GenericTextWatcher(binding.et1, binding.et2))
        binding.et2.addTextChangedListener(GenericTextWatcher(binding.et2, binding.et3))
        binding.et3.addTextChangedListener(GenericTextWatcher(binding.et3, binding.et4))
        binding.et4.addTextChangedListener(GenericTextWatcher(binding.et4, binding.et5))
        binding.et5.addTextChangedListener(GenericTextWatcher(binding.et5, binding.et6))
        binding.et6.addTextChangedListener(GenericTextWatcher(binding.et6, null))

        binding.et1.setOnKeyListener(GenericKeyEvent(binding.et1, null))
        binding.et2.setOnKeyListener(GenericKeyEvent(binding.et2, binding.et1))
        binding.et3.setOnKeyListener(GenericKeyEvent(binding.et3, binding.et2))
        binding.et4.setOnKeyListener(GenericKeyEvent(binding.et4, binding.et3))
        binding.et5.setOnKeyListener(GenericKeyEvent(binding.et5, binding.et4))
        binding.et6.setOnKeyListener(GenericKeyEvent(binding.et6, binding.et5))

        //get intent data
        intent.getStringExtra("amount").let { amount = it }
        intent.getStringExtra("upiId").let { upiId = it }
        intent.getStringExtra("name").let { name = it }
        intent.getStringExtra("mobileNumber").let { number = it }

        countdown()

        binding.NextButton.setSafeOnClickListener {
            if (otp().length == 6) {
                viewModel.verifyOTPMobileEmailApi(
                    SavedPrefManager.getStringPreferences(
                        this,
                        SavedPrefManager.AccessToken
                    ).toString(), otp()
                )

            }

        }

        binding.ResendCode.setOnClickListener {
            countdown()
            sendOtpOnMobile()
        }

        binding.backButton.setOnClickListener{
            finishAfterTransition()
        }


        otpVerificationObserver()
        redeemCoinObserver()

    }

    private fun otpVerificationObserver() {

            lifecycleScope.launch {
                viewModel.verifyOTPMobileEmailResponseData.collect { response ->

                    when (response) {
                        is Resource.Success -> {
                            if (response.data!!.responseCode == 200) {
                                try {
                                    Progress.stop()
                                    viewModel.redeemApi(
                                        SavedPrefManager.getStringPreferences(
                                            this@OtpVerificationActivity,
                                            SavedPrefManager.AccessToken
                                        ).toString(), amount.toString(), upiId.toString(), name.toString(),number.toString()
                                    )
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                        }

                        is Resource.Loading -> {
                            Progress.start(this@OtpVerificationActivity)
                        }

                        is Resource.Empty -> {
                            Progress.stop()
                        }
                    }
                }
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
                                Toast.makeText(this@OtpVerificationActivity, "Otp sent successfully", Toast.LENGTH_SHORT).show()
                            } catch (e:Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        Progress.stop()
                    }

                    is Resource.Loading -> {
                        Progress.start(this@OtpVerificationActivity)
                    }

                    is Resource.Empty -> {
                        Progress.stop()
                    }
                }
            }
        }
    }


    private fun redeemCoinObserver(){

            lifecycleScope.launch {
                viewModel.redeemCoinResponseData.collect { response ->

                    when (response) {
                        is Resource.Success -> {
                            if (response.data!!.responseCode == 200) {
                                try {
                                    Progress.stop()
                                    AndroidExtension.alertBoxFinishActivity("Request send to admin. Please wait for admin approval",this@OtpVerificationActivity,this@OtpVerificationActivity)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                        }

                        is Resource.Loading -> {
                            Progress.start(this@OtpVerificationActivity)
                        }

                        is Resource.Empty -> {
                            Progress.stop()
                        }
                    }
                }
            }
    }

    private fun otp():String{
        val otp = binding.et1.text.toString()+
                binding.et2.text.toString()+
                binding.et3.text.toString()+
                binding.et4.text.toString()+
                binding.et5.text.toString()+
                binding.et6.text.toString()
        return otp
    }

    private fun countdown() {

        object : CountDownTimer(180000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                binding.ResendCodeAgain.visibility = View.GONE
                binding.ResendCode.visibility = View.VISIBLE
                binding.ResendCode.isEnabled = false
                val f = DecimalFormat("00")


                val min = (millisUntilFinished / 60000) % 60

                val sec = (millisUntilFinished / 1000) % 60
                binding.timer.text = f.format(min) + ":" + f.format(sec)

            }

            @SuppressLint("ResourceAsColor")
            override fun onFinish() {
                binding.timer.text = "00:00"
                binding.ResendCodeAgain.visibility = View.VISIBLE
                binding.ResendCode.visibility = View.GONE
                binding.ResendCode.isEnabled = true
            }
        }.start()
    }

}