package com.fithun.ui.activities.loginFlow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.fithun.utils.SavedPrefManager
import com.fithun.R
import com.fithun.api.requests.ChangePassRequest
import com.fithun.databinding.ActivityChangePasswordBinding
import com.fithun.utils.FormValidations
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.displayToast
import com.fithun.viewModel.LoginFlowViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

 @AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding
    private val viewModel: LoginFlowViewModel by viewModels()

    private var mobilePasswordCurrent = 0
    private var mobilePasswordCreate = 0
    private var mobilePasswordConfirm = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }

        binding.etCurrentPassword.addTextChangedListener(textWatcherValidation)
        binding.etPassword.addTextChangedListener(textWatcherValidation)
        binding.etConfirmPassword.addTextChangedListener(textWatcherValidation)


        binding.MobilePasswordCurrentEye.setOnClickListener {

            when (mobilePasswordCurrent) {
                0 -> {
                    binding.etCurrentPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.passwordImageCurrent.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.eye))
                    mobilePasswordCurrent = 1


                }
                1 -> {
                    binding.etCurrentPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    binding.passwordImageCurrent.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.eye_hide))
                    mobilePasswordCurrent = 0
                }
                else -> {
                    binding.etCurrentPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.passwordImageCurrent.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.eye))
                    mobilePasswordCurrent = 1
                }
            }

            binding.etCurrentPassword.setSelection(binding.etCurrentPassword.text.length)
        }

        binding.MobilePasswordEye.setOnClickListener {

            when (mobilePasswordCreate) {
                0 -> {
                    binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.passwordImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.eye))
                    mobilePasswordCreate = 1


                }
                1 -> {
                    binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    binding.passwordImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.eye_hide))
                    mobilePasswordCreate = 0
                }
                else -> {
                    binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.passwordImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.eye))
                    mobilePasswordCreate = 1
                }
            }

            binding.etPassword.setSelection(binding.etPassword.text.length)
        }
        binding.ConfirmPasswordEye.setOnClickListener {

            when (mobilePasswordConfirm) {
                0 -> {
                    binding.etConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.passwordConfirmImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.eye))
                    mobilePasswordConfirm = 1


                }
                1 -> {
                    binding.etConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    binding.passwordConfirmImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.eye_hide))
                    mobilePasswordConfirm = 0
                }
                else -> {
                    binding.etConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.passwordConfirmImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.eye))
                    mobilePasswordConfirm = 1
                }
            }

            binding.etConfirmPassword.setSelection(binding.etConfirmPassword.text.length)
        }





        binding.ChangePasswordDisable.setOnClickListener {
            FormValidations.changePassword(
                binding.etCurrentPassword,
                binding.llPassWordCurrent,
                binding.tvCurrentPassword,
                binding.etPassword,
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
                this@ChangePasswordActivity,
                "",

            )
        }

        binding.ChangePasswordClick.setOnClickListener {
            FormValidations.changePassword(
                binding.etCurrentPassword,
                binding.llPassWordCurrent,
                binding.tvCurrentPassword,
                binding.etPassword,
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
                this@ChangePasswordActivity,
                "",

            )

            if (binding.etCurrentPassword.text.isNotEmpty()  &&
                binding.etConfirmPassword.text.isNotEmpty()  && binding.etConfirmPassword.text.toString() == binding.etConfirmPassword.text.toString()
            ){
                    val changeRequest = ChangePassRequest()
                    changeRequest.apply {
                    oldpass= binding.etCurrentPassword.text.toString()
                    pass = binding.etConfirmPassword.text.toString()
                    confirmPass = binding.etConfirmPassword.text.toString()


                }

                viewModel.changePassApi(SavedPrefManager.getStringPreferences(this@ChangePasswordActivity, SavedPrefManager.AccessToken).toString(),changeRequest);
            }


        }

        ChangePassObserver()



    }

    private fun ChangePassObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.changePassApiResponseData.collect { response ->
                    when(response){

                        is Resource.Loading ->{
                            Progress.start(this@ChangePasswordActivity)
                        }


                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200){
//
//                                   startActivity(Intent(this@ChangePasswordActivity, LoginActivity::class.java))
                                    finishAfterTransition()
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
            val currentField = if (s === binding.etPassword.text) "Create Password"
            else if (s === binding.etConfirmPassword.text) "Confirm Password" else "Current Password"
            FormValidations.changePassword(
                binding.etCurrentPassword,
                binding.llPassWordCurrent,
                binding.tvCurrentPassword,
                binding.etPassword,
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
                this@ChangePasswordActivity,
                currentField,

                )

            if (binding.etCurrentPassword.text.isNotEmpty()  &&
                binding.etConfirmPassword.text.isNotEmpty()  &&   binding.etConfirmPassword.text.equals(binding.etConfirmPassword.text)){
                binding.ChangePasswordDisable.isVisible = false
                binding.ChangePasswordClick.isVisible = true
            }else{
                binding.ChangePasswordDisable.isVisible = true
                binding.ChangePasswordClick.isVisible = false
            }


        }

    }



}