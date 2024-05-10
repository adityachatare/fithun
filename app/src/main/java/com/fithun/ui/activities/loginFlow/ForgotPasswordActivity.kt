package com.fithun.ui.activities.loginFlow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.fithun.R
import com.fithun.api.requests.VerifyOtpRequest
import com.fithun.clickInterfaces.CommonClick
import com.fithun.databinding.ActivityForgotPasswordBinding
import com.fithun.utils.AndroidExtension
import com.fithun.utils.FormValidations
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.displayToast
import com.fithun.viewModel.LoginFlowViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity(), CommonClick {


    private lateinit var binding: ActivityForgotPasswordBinding
    var isForgot =  true
    var optEmail = ""
    var token = ""

    private val viewModel: LoginFlowViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }
        binding.resendOtp.setOnClickListener {
            viewModel.resendApi(binding.etMobileNumberAndEmail.text.toString())
            countdown()
        }

        binding.etMobileNumberAndEmail.addTextChangedListener(textWatcherValidation)
        binding.etVerificationCode.addTextChangedListener(textWatcherValidation)


        binding.ForgotPasswordDisable.setOnClickListener {
            if (isForgot){
                FormValidations.forgotPassword(binding.etMobileNumberAndEmail,binding.llMobileNumber,binding.tvMobile,"")
            }else{
                FormValidations.otpVerification(binding.etVerificationCode,binding.llVerification,binding.tvVerificationCode,"")
            }

        }
        binding.ForgotPasswordClick.setOnClickListener {
            if (isForgot){
            FormValidations.forgotPassword(binding.etMobileNumberAndEmail,binding.llMobileNumber,binding.tvMobile,"")
            if (binding.etMobileNumberAndEmail.text.isNotEmpty( )&& (binding.etMobileNumberAndEmail.text.matches(Regex(
                        FormValidations.emailPattern)) || binding.etMobileNumberAndEmail.text.matches(Regex(FormValidations.MobilePattern)))) {



                viewModel.forgotApi(binding.etMobileNumberAndEmail.text.toString())
            }

            }else{
                FormValidations.otpVerification(binding.etVerificationCode,binding.llVerification,binding.tvVerificationCode,"")
                if (binding.etVerificationCode.text.isNotEmpty() && binding.etVerificationCode.text.length == 6) {
                    val verifyOtpRequest = VerifyOtpRequest()
                    verifyOtpRequest.apply {
                        emailandPass = optEmail
                        otp = binding.etVerificationCode.text.toString()
                    }

                    viewModel.otpVerifyApi(verifyOtpRequest);

                }
            }



        }
        countdown()
        forgotPassObserver()
        ResendOtpObserver()
        otpVerificationObserver()
    }

    private fun countdown() {
        val countdownTimeInMillis = 3 * 60 * 1000 // 1 minute in milliseconds

        object : CountDownTimer(countdownTimeInMillis.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                val formattedTime = String.format("%02d:%02d", minutes, seconds)
                binding.otpTimer.text= formattedTime
                binding.resendOtp.isEnabled = false
                binding.resendOtp.setTextColor(ContextCompat.getColor(this@ForgotPasswordActivity, R.color.grey))
            }

            override fun onFinish() {
                // Timer has finished, implement your actions here
                binding.otpTimer.text = ""
                binding.resendOtp.isEnabled = true
                binding.resendOtp.setTextColor(ContextCompat.getColor(this@ForgotPasswordActivity, R.color.status_bar))
            }
        }.start()
    }




    private fun ResendOtpObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.resendOtpApiResponseData.collect { response ->
                    when(response){

                        is Resource.Loading ->{
                            Progress.start(this@ForgotPasswordActivity)
                        }


                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200){
                                    optEmail = response.data.result?.email.toString();
                                    isForgot = false

                                    binding.emailLablel.isVisible = false
                                    binding.etMobileNumberAndEmail.isVisible = false
                                    binding.tvMobile.isVisible = false

                                    binding.VerificationTV.isVisible = true
                                    binding.verificationLayout.isVisible = true
                                    binding.llVerification.isVisible = true
                                    binding.ForgotPasswordDisable.isVisible = true
                                    binding.ForgotPasswordClick.isVisible = false
                                    binding.buttonTextDisable.text = "Otp Verify"
                                    binding.buttonText.text = "Otp Verify"
                                    binding.heading.text = "Otp Verification"
                                }

                            }catch (e:Exception){
                                
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error ->{
                            Progress.stop()
                            displayToast(response.message)
                            

                        }


                        is Resource.Empty ->{

                        }


                    }


                }

            }
        }
    }

    private fun forgotPassObserver() {
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                    viewModel.forgotApiResponseData.collect { response ->
                        when(response){

                            is Resource.Loading ->{
                                Progress.start(this@ForgotPasswordActivity)
                            }


                            is Resource.Success -> {
                                try {
                                    Progress.stop()
                                    if (response.data?.responseCode == 200){
                                        optEmail = response.data.result?.email.toString();
                                        isForgot = false

                                        binding.emailLablel.isVisible = false
                                        binding.etMobileNumberAndEmail.isVisible = false
                                        binding.tvMobile.isVisible = false

                                        binding.VerificationTV.isVisible = true
                                        binding.verificationLayout.isVisible = true
                                        binding.llVerification.isVisible = true
                                        binding.ForgotPasswordDisable.isVisible = true
                                        binding.ForgotPasswordClick.isVisible = false
                                        binding.buttonTextDisable.text = "OTP Verify"
                                        binding.buttonText.text = "OTP Verify"
                                        binding.heading.text = "OTP Verification"
                                    }

                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }

                            is Resource.Error ->{
                                Progress.stop()
                                response.message?.let {message ->
                                        AndroidExtension.alertBox(message,this@ForgotPasswordActivity)
                                }



                            }


                            is Resource.Empty ->{

                            }


                        }


                    }

                }
            }
        }




    private val textWatcherValidation = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                if (s.length == 1 && s.toString().startsWith("0")) {
                    s.clear();
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (isForgot){
                val currentField =  "Email"
                FormValidations.forgotPassword(binding.etMobileNumberAndEmail,binding.llMobileNumber,binding.tvMobile,currentField)

                if (binding.etMobileNumberAndEmail.text.isNotEmpty( )&& (binding.etMobileNumberAndEmail.text.matches(Regex(
                        FormValidations.emailPattern)) || binding.etMobileNumberAndEmail.text.matches(Regex(FormValidations.MobilePattern)))){

                    binding.ForgotPasswordDisable.isVisible = false
                    binding.ForgotPasswordClick.isVisible = true


                }else{
                    binding.ForgotPasswordDisable.isVisible = true
                    binding.ForgotPasswordClick.isVisible = false
                }



            }
            else{
                FormValidations.otpVerification(binding.etVerificationCode,binding.llVerification,binding.tvVerificationCode,"OTP")

                if (binding.etVerificationCode.text.isNotEmpty()&& binding.etVerificationCode.text.length ==6){

                    binding.ForgotPasswordDisable.isVisible = false
                    binding.ForgotPasswordClick.isVisible = true


                }else{
                    binding.ForgotPasswordDisable.isVisible = true
                    binding.ForgotPasswordClick.isVisible = false
                }
            }


        }

    }

    private fun otpVerificationObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.otpVerifyApiResponseData.collect { response ->
                    when(response){

                        is Resource.Loading ->{
                            Progress.start(this@ForgotPasswordActivity
                            )
                        }


                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200){

                                    token = response.data.result?.token!!
                                    AndroidExtension.alertBoxFinish(response.data.responseMessage!!,this@ForgotPasswordActivity,this@ForgotPasswordActivity)


                                }

                            }catch (e:Exception){
                                
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error ->{
                            Progress.stop()
                            
                            displayToast(response.message)

                        }


                        is Resource.Empty ->{

                        }


                    }


                }

            }
        }
    }

    override fun applyClick() {
        startActivity(Intent(this@ForgotPasswordActivity, ResetPasswordActivity::class.java).putExtra("token",token))
        finishAfterTransition()
    }


}