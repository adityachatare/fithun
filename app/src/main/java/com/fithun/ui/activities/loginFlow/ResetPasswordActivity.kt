package com.fithun.ui.activities.loginFlow

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.fithun.R
import com.fithun.api.requests.ResetPassRequest
import com.fithun.databinding.ActivityResetPasswordBinding
import com.fithun.utils.AndroidExtension
import com.fithun.utils.FormValidations
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.viewModel.LoginFlowViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding
    private val viewModel: LoginFlowViewModel by viewModels()
    private var requestToken = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade



        binding.etNewPassword.addTextChangedListener(textWatcherValidation)
        binding.etConfirmPassword.addTextChangedListener(textWatcherValidation)

        intent.getStringExtra("token")?.let { requestToken = it }
        print("requestToken>>>>> $requestToken")

        var mobilePasswordCreate = 0
        var mobilePasswordConfirm = 0


        binding.MobilePasswordNewEye.setOnClickListener {

            when (mobilePasswordCreate) {
                0 -> {
                    binding.etNewPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.passwordImageNew.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.eye))
                    mobilePasswordCreate = 1


                }
                1 -> {
                    binding.etNewPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    binding.passwordImageNew.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.eye_hide))
                    mobilePasswordCreate = 0
                }
                else -> {
                    binding.etNewPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.passwordImageNew.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.eye))
                    mobilePasswordCreate = 1
                }
            }

            binding.etNewPassword.setSelection(binding.etNewPassword.text.length)
        }
        binding.ConfirmPasswordEyeNew.setOnClickListener {

            when (mobilePasswordConfirm) {
                0 -> {
                    binding.etConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.passwordConfirmImageConfirm.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.eye))
                    mobilePasswordConfirm = 1


                }
                1 -> {
                    binding.etConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    binding.passwordConfirmImageConfirm.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.eye_hide))
                    mobilePasswordConfirm = 0
                }
                else -> {
                    binding.etConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.passwordConfirmImageConfirm.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.eye))
                    mobilePasswordConfirm = 1
                }
            }

            binding.etConfirmPassword.setSelection(binding.etConfirmPassword.text.length)
        }



        binding.resetPasswordwordDisable.setOnClickListener {
            FormValidations.resetPasswordword(
                binding.etNewPassword,
                binding.llPassWord,
                binding.etConfirmPassword,
                binding.llConfirmPassWord,
                binding.tvConfirmPassword,
                binding.passwordValidation,
                binding.eightCharVaildLL,
                binding.eightCharImage,
                binding.eightCharText,
                binding.oneLowerCasell,
                binding.oneLowerCaseImage,
                binding.oneLowerCaseText,
                binding.oneUpperCaseLL,
                binding.oneUpperCaseImage,
                binding.oneUpperCaseText,
                binding.oneNumberLL,
                binding.oneNumberImage,
                binding.oneNumberText,
                binding.oneSymbolLL,
                binding.oneSymbolImage,
                binding.oneSymbolText,
                this@ResetPasswordActivity,
                ""
            )

        }


        binding.resetPasswordwordClick.setOnClickListener {
            FormValidations.resetPasswordword(
                binding.etNewPassword,
                binding.llPassWord,
                binding.etConfirmPassword,
                binding.llConfirmPassWord,
                binding.tvConfirmPassword,
                binding.passwordValidation,
                binding.eightCharVaildLL,
                binding.eightCharImage,
                binding.eightCharText,
                binding.oneLowerCasell,
                binding.oneLowerCaseImage,
                binding.oneLowerCaseText,
                binding.oneUpperCaseLL,
                binding.oneUpperCaseImage,
                binding.oneUpperCaseText,
                binding.oneNumberLL,
                binding.oneNumberImage,
                binding.oneNumberText,
                binding.oneSymbolLL,
                binding.oneSymbolImage,
                binding.oneSymbolText,
                this@ResetPasswordActivity,
                ""
            )
            if (binding.etConfirmPassword.text.isNotEmpty()  &&   binding.etConfirmPassword.text.equals(binding.etConfirmPassword.text)){

                val resetRequest = ResetPassRequest()
                resetRequest.apply {
                    token = requestToken
                    pass = binding.etNewPassword.text.toString()
                    confirmPass = binding.etConfirmPassword.text.toString()


                }

                viewModel.resetApiData( resetRequest)
            }




        }


       resetPasswordObserver()
    }

    private fun resetPasswordObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.resetApiResponseData.collect { response ->
                    when(response){

                        is Resource.Loading ->{
                            Progress.start(this@ResetPasswordActivity)
                        }


                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200){
                                  SavedPrefManager.saveStringPreferences(this@ResetPasswordActivity, SavedPrefManager.AccessToken,response.data.result?.password)
                                    startActivity(Intent(this@ResetPasswordActivity, LoginActivity::class.java))
                                    finishAfterTransition()
                                }

                            }catch (e:Exception){
                                
                                  e.printStackTrace()
                            }
                        }

                        is Resource.Error ->{
                            Progress.stop()
                            response.message?.let {
                                AndroidExtension.alertBox(it, this@ResetPasswordActivity)
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
                    s.clear()
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val currentField = if (s === binding.etNewPassword.text) "Create Password"
            else if (s === binding.etConfirmPassword.text) "Confirm Password" else ""
            FormValidations.resetPasswordword(
                binding.etNewPassword,
                binding.llPassWord,
                binding.etConfirmPassword,
                binding.llConfirmPassWord,
                binding.tvConfirmPassword,
                binding.passwordValidation,
                binding.eightCharVaildLL,
                binding.eightCharImage,
                binding.eightCharText,
                binding.oneLowerCasell,
                binding.oneLowerCaseImage,
                binding.oneLowerCaseText,
                binding.oneUpperCaseLL,
                binding.oneUpperCaseImage,
                binding.oneUpperCaseText,
                binding.oneNumberLL,
                binding.oneNumberImage,
                binding.oneNumberText,
                binding.oneSymbolLL,
                binding.oneSymbolImage,
                binding.oneSymbolText,
                this@ResetPasswordActivity,
                currentField
            )

            if (binding.etNewPassword.text.isNotEmpty()  && binding.etConfirmPassword.text.isNotEmpty()  && binding.etNewPassword.text.toString() == binding.etConfirmPassword.text.toString()){

                binding.resetPasswordwordDisable.isVisible = false
                binding.resetPasswordwordClick.isVisible = true


            }else{
                binding.resetPasswordwordDisable.isVisible = true
                binding.resetPasswordwordClick.isVisible = false
            }


        }

    }



}
