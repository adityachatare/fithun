package com.fithun.ui.activities.loginFlow

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.fithun.R
import com.fithun.api.requests.SignUpRequest
import com.fithun.api.requests.VerifyEmailMobileOtpRequest
import com.fithun.api.requests.VerifyMobileRequest
import com.fithun.clickInterfaces.CommonClick
import com.fithun.databinding.ActivitySignUpBinding
import com.fithun.ui.activities.common.StaticContentActivity
import com.fithun.utils.AndroidExtension
import com.fithun.utils.DateFormat
import com.fithun.utils.DialogUtils
import com.fithun.utils.FormValidations
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.displayToast
import com.fithun.utils.loadImageResource
import com.fithun.utils.setSafeOnClickListener
import com.fithun.viewModel.LoginFlowViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity(), CommonClick {

    private lateinit var binding: ActivitySignUpBinding

    var isFor = ""
    private val viewModel: LoginFlowViewModel by viewModels()
    private var timer: Timer? = null
    var apiRequestDate = ""

    var isEmailVerified = false

    val currentDate = Calendar.getInstance()
    val currentYear = currentDate.get(Calendar.YEAR)


    val minAllowedAge = 18
    val minBirthYear = currentYear - minAllowedAge

    // Set the maximum allowed age (e.g., 100 years) and calculate the maximum birth year
    val maxAllowedAge = 100
    val maxBirthYear = currentYear - maxAllowedAge

    var isPhoneVerified = false
    private var isBelow18 = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        intent.getStringExtra("isFor")?.let { isFor = it }
        val text = this.resources.getString(R.string.already_have_a_account_signup)
        val secondName = "<font color=\"#8E68FE\">${getString(R.string.login)}</font>"

        binding.havingAccount.text =
            Html.fromHtml("$text $secondName", HtmlCompat.FROM_HTML_MODE_LEGACY)

        clicks()

        binding.emailNameImage.setOnClickListener {
            binding.emailNameImage.isEnabled = false
            viewModel.signUpEmailOtpApi(binding.etEmail.text.toString())
        }

        binding.mobileNameImage.setOnClickListener{

            binding.mobileNameImage.isEnabled = false
            viewModel.signUpMobileOtpApi(binding.etMobileNumber.text.toString())
        }







        binding.eOtpIcon.setOnClickListener{

            val verificationRequest = VerifyEmailMobileOtpRequest();
            verificationRequest.apply {
                email = binding.etEmail.text.toString()
                otp = binding.etOtpVerify.text.toString()

            }

          viewModel.signUpVerificationOtpApi(verificationRequest)

        }
        binding.mOtpIcon.setOnClickListener{

            val verificationMobRequest = VerifyMobileRequest();
            verificationMobRequest.apply {
                mobileNumber = binding.etMobileNumber.text.toString()
                otp = binding.etmOtpVerify.text.toString()

            }
            viewModel.MobileVerifationOtpApi(verificationMobRequest)
        }

        binding.etuserName.tag = false
        binding.etuserName.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            binding.etuserName.tag = hasFocus
        }


        sendMobileObserver()
        observeSignUpResponse()
        userNameObserver()
        sendEmailObserver()
        verifyMobileObserver()
        verifySendEmailMobile()


    }

    private fun verifyMobileObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.signMobileOtpVerResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(this@SignUpActivity)
                        }


                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200) {
                                    binding.otpmVerification.isVisible=false
                                    binding.verifiedIconMobImage.isVisible=true
                                    binding.mobileNameImage.isVisible=false
                                    isPhoneVerified=true
//                                binding.otpVerification.isVisible=true

                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            response.message?.let {
                                AndroidExtension.alertBox(it, this@SignUpActivity)
                            }

                        }


                        is Resource.Empty -> {

                        }


                    }


                }

            }
        }

    }

    private fun sendMobileObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.signMobileOtpResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(this@SignUpActivity)
                        }


                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200) {
                                    binding.otpmVerification.isVisible=true
                                    binding.mobileNameImage.isEnabled = true

                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            binding.mobileNameImage.isEnabled = true
                            response.message?.let {
                                AndroidExtension.alertBox(it, this@SignUpActivity)
                            }

                        }


                        is Resource.Empty -> {

                        }


                    }


                }

            }
        }
    }


    private fun verifySendEmailMobile() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.signMOtpResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(this@SignUpActivity)
                        }


                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200) {
                             binding.otpVerification.isVisible=false
                                    binding.verifiedIconImage.isVisible=true
                                binding.emailNameImage.isVisible=false
                                    isEmailVerified=true
//                                    isPhoneVerified=true
//                                binding.otpVerification.isVisible=true

                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            response.message?.let {
                                AndroidExtension.alertBox(it, this@SignUpActivity)
                            }

                        }


                        is Resource.Empty -> {

                        }


                    }


                }

            }
        }
    }

    private fun sendEmailObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.signEOtpResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(this@SignUpActivity)
                        }


                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200) {
                                 binding.otpVerification.isVisible=true
                                    binding.emailNameImage.isEnabled = true

                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            binding.emailNameImage.isEnabled = true
                            response.message?.let {
                                AndroidExtension.alertBox(it, this@SignUpActivity)
                            }

                        }


                        is Resource.Empty -> {

                        }


                    }


                }

            }
        }
    }




    @SuppressLint("InflateParams", "SetTextI18n")
    fun showSizeChart() {

        try {
            val bindingSizeChart = LayoutInflater.from(this).inflate(R.layout.pop_lists, null)
            val dialog = DialogUtils().createDialog(this, bindingSizeChart.rootView, 0)

            val dialogBackButton = bindingSizeChart.findViewById<ImageView>(R.id.BackButton)
            val imageForFeetSize = bindingSizeChart.findViewById<ImageView>(R.id.imageForFeetSize)


            if(binding.genderSpinner.selectedItem == "FEMALE"){
                Glide.with(this).load(R.drawable.women_chart).into(imageForFeetSize)
            }else{
                Glide.with(this).load(R.drawable.men_chart).into(imageForFeetSize)

            }


            dialogBackButton.setOnClickListener {
                dialog.dismiss()
            }



            dialog.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun clicks() {
        var mobilePasswordCreate = 0
        var mobilePasswordConfirm = 0

        binding.MobilePasswordEye.setOnClickListener {

            when (mobilePasswordCreate) {
                0 -> {
                    binding.etPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    binding.passwordImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,

                            R.drawable.eye
                        )
                    )
                    mobilePasswordCreate = 1


                }

                1 -> {
                    binding.etPassword.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    binding.passwordImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.eye_hide
                        )
                    )
                    mobilePasswordCreate = 0
                }

                else -> {
                    binding.etPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    binding.passwordImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.eye
                        )
                    )
                    mobilePasswordCreate = 1
                }
            }

            binding.etPassword.setSelection(binding.etPassword.text.length)
        }
        binding.ConfirmPasswordEye.setOnClickListener {

            when (mobilePasswordConfirm) {
                0 -> {
                    binding.etConfirmPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    binding.passwordConfirmImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.eye
                        )
                    )
                    mobilePasswordConfirm = 1


                }

                1 -> {
                    binding.etConfirmPassword.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    binding.passwordConfirmImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.eye_hide
                        )
                    )
                    mobilePasswordConfirm = 0
                }

                else -> {
                    binding.etConfirmPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    binding.passwordConfirmImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.eye
                        )
                    )
                    mobilePasswordConfirm = 1
                }
            }

            binding.etConfirmPassword.setSelection(binding.etConfirmPassword.text.length)
        }
        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)  // This is 0-based, so January is 0, February is 1, and so on.
        val date = calendar.get(Calendar.DAY_OF_MONTH)
        val c = Calendar.getInstance()

        c.set(year, month, date)

        binding.llDOB.setSafeOnClickListener {
            val c = Calendar.getInstance()
            val currentYear = c.get(Calendar.YEAR)
            val currentMonth = c.get(Calendar.MONTH)
            val currentDay = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                R.style.DatePickerTheme, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    val selectedDate = Calendar.getInstance().apply {
                        set(selectedYear, selectedMonth, selectedDayOfMonth)
                    }


                    val dateFormat = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDayOfMonth)
                    apiRequestDate = dateFormat
                    binding.etDOB.text = DateFormat.dateFormatPicker(dateFormat)


                    if (isAbove18(selectedDate)) {
                        displayToast("You are above")
                    } else {
                        isBelow18 = true
                        displayToast("You are below 18")
                    }
                },
                currentYear - 18, // Minimum allowed year (18 years ago)
                currentMonth,
                currentDay
            )

            datePickerDialog.datePicker.maxDate = c.timeInMillis
            datePickerDialog.show()
        }






        binding.havingAccount.setSafeOnClickListener {
            if (isFor == "Welcome") {
                startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                finishAfterTransition()
            } else {
                finishAfterTransition()
            }
        }

        binding.SizeChart.setOnClickListener {
            showSizeChart()
        }

        binding.YesWalk.visibility = View.GONE
        binding.NoWalk.visibility = View.GONE
        binding.ablewalk.visibility=View.GONE

        binding.YesWalk.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.NoWalk.isChecked = false

                binding.TvableToWalk.isVisible = false
                binding.TvableToWalk.text = ""
            }
        }

        binding.NoWalk.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.YesWalk.isChecked = false
                binding.TvableToWalk.isVisible = false
                binding.TvableToWalk.text = ""
            }
        }

        binding.PhysicallyChallenged.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val item = parent.getItemAtPosition(pos)
                if (item == "Yes") {
                    // Show both checkboxes when "Yes" is selected in the spinner
                    binding.YesWalk.visibility = View.VISIBLE
                    binding.NoWalk.visibility = View.VISIBLE
                    binding.ablewalk.visibility = View.VISIBLE
                } else {
                    // Hide both checkboxes for any other selection in the spinner
                    binding.YesWalk.visibility = View.GONE
                    binding.NoWalk.visibility = View.GONE
                    binding.ablewalk.visibility=View.GONE
                    binding.TvableToWalk.isVisible = false
                    binding.TvableToWalk.text = ""
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        binding.RegisterTandC.setOnCheckedChangeListener { _, isChecked ->
            if (binding.etFirstName.text.isNotEmpty() && binding.etLastName.text.isNotEmpty() && binding.etuserName.text.isNotEmpty()
                && binding.etEmail.text.isNotEmpty() &&
                binding.etPassword.text.isNotEmpty() &&
                binding.etConfirmPassword.text.isNotEmpty() && binding.etConfirmPassword.text.toString() == binding.etPassword.text.toString() &&
                binding.etMobileNumber.text.isNotEmpty() && binding.etMobileNumber.text.length == 10 &&
                binding.etDOB.text.isNotEmpty() && binding.genderSpinner.selectedItem.toString() != "Select gender" &&
                binding.shoeSizeSpinner.selectedItem.toString() != "Select your shoe size" &&
                binding.etWeight.text.isNotEmpty() &&
                binding.PhysicallyChallenged.selectedItem.toString() != "Select physically challenged"
                && isChecked && isEmailVerified && isPhoneVerified) {
                binding.signUpClickDisable.isVisible = false
                binding.signUpClick.isVisible = true
            } else {
                if(isEmailVerified){
                    displayToast("This email is already Registered")
                }
                if(!isPhoneVerified){
                    displayToast("This phone number is already Registered")
                }
                binding.signUpClickDisable.isVisible = true
                binding.signUpClick.isVisible = false
            }
        }
        binding.genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val item = parent.getItemAtPosition(pos)
                if (item != "Select gender") {
                    binding.llGender.setBackgroundResource(R.drawable.white_border_background)
                    binding.TvGender.isVisible = false

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding.shoeSizeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val item = parent.getItemAtPosition(pos)
                    if (item != "Select your shoe size") {
                        binding.llShoeSize.setBackgroundResource(R.drawable.white_border_background)
                        binding.TvShoeSize.isVisible = false
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.etFirstName.addTextChangedListener(textWatcherValidation)
        binding.etLastName.addTextChangedListener(textWatcherValidation)
        binding.etuserName.addTextChangedListener(userNameWatcher)
        binding.etEmail.addTextChangedListener(textWatcherValidation)
        binding.etPassword.addTextChangedListener(textWatcherValidation)
        binding.etConfirmPassword.addTextChangedListener(textWatcherValidation)
        binding.etMobileNumber.addTextChangedListener(textWatcherValidation)
        binding.etDOB.addTextChangedListener(textWatcherValidation)
        binding.etWeight.addTextChangedListener(textWatcherValidation)
        binding.etOtpVerify.addTextChangedListener(textWatcherValidation)
        binding.etmOtpVerify.addTextChangedListener(textWatcherValidation)



        binding.signUpClickDisable.setSafeOnClickListener {
            binding.passwordValidation.isVisible = true
            signUpValidation(currentField = "")


        }
        binding.tvTandC.setOnClickListener {
            val intent = Intent(this, StaticContentActivity::class.java)
            intent.putExtra("isFor", "termsAndCondition")
            startActivity(intent)
        }

        binding.privacyPolicy.setOnClickListener {
            val intent = Intent(this, StaticContentActivity::class.java)
            intent.putExtra("isFor", "privacyPolicy")
            startActivity(intent)
        }
        binding.signUpClick.setSafeOnClickListener {
            binding.passwordValidation.isVisible = true
            signUpValidation(currentField = "")


            if (binding.etFirstName.text.isNotEmpty() && binding.etLastName.text.isNotEmpty() && binding.etuserName.text.isNotEmpty()
                && binding.etEmail.text.isNotEmpty() && binding.etEmail.text.toString()
                    .matches(Regex(FormValidations.emailPattern)) &&
                binding.etPassword.text.isNotEmpty() &&
                binding.etConfirmPassword.text.isNotEmpty() && binding.etConfirmPassword.text.toString() == binding.etPassword.text.toString() &&
                binding.etMobileNumber.text.isNotEmpty() && binding.etMobileNumber.text.length == 10 &&
                binding.etDOB.text.isNotEmpty() && !binding.genderSpinner.selectedItem.equals("Select gender")
                && !binding.shoeSizeSpinner.selectedItem.equals("Select your shoe size") && binding.etWeight.text.isNotEmpty() &&

                !binding.PhysicallyChallenged.selectedItem.equals("Select physically Challenged")
                && binding.RegisterTandC.isChecked

            ) {

                if (binding.PhysicallyChallenged.selectedItem.equals("Yes") && !binding.YesWalk.isChecked && !binding.NoWalk.isChecked){
                    binding.ableToWalkCB.isVisible = true
                    binding.ablewalk.isVisible = true
                    binding.TvableToWalk.isVisible = true
                    binding.TvableToWalk.text = "Please select are you able to walk?"
                    return@setSafeOnClickListener
                }

                val signUpRequest = SignUpRequest()
                signUpRequest.apply {
                    name = "${binding.etFirstName.text} ${binding.etLastName.text}"
                    userName = binding.etuserName.text.toString()
                    email = binding.etEmail.text.toString()
                    password = binding.etPassword.text.toString()
                    mobileNumber = binding.etMobileNumber.text.toString()
                    dob = apiRequestDate
                    referId = binding.etRefralCode.text.toString()
                    isMinor = isBelow18
                    gender = binding.genderSpinner.selectedItem.toString().uppercase()
                    mobileCode = binding.ccpMyProfile.selectedCountryCode.toString()
                    additionalDetails.shoeSize = binding.shoeSizeSpinner.selectedItem.toString()
                    additionalDetails.weight = binding.etWeight.text.toString()
                    additionalDetails.upiId = binding.etUPIId.text.toString()
                    additionalDetails.physicallyChallenged = binding.PhysicallyChallenged.selectedItem.toString()
                    additionalDetails.ableToWalk = binding.YesWalk.isSelected
                }
                viewModel.signUpApi(signUpRequest = signUpRequest)
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
            val currentField =
                if (s === binding.etFirstName.text) "First Name" else if (s === binding.etLastName.text) "Last Name"
                else if (s === binding.etuserName.text) "User Name" else if (s === binding.etEmail.text) "Email" else if (s === binding.etOtpVerify.text) "OTP"
                else if (s === binding.etPassword.text) "Create Password" else if (s === binding.etConfirmPassword.text) "Confirm Password"
                else if (s === binding.etMobileNumber.text) "Mobile Number" else if (s === binding.etDOB.text) "DOB" else if (s === binding.etmOtpVerify.text) "MobOTP"
                else if (s === binding.genderSpinner.selectedItem) "Gender" else if (s === binding.shoeSizeSpinner.selectedItem) "Shoe Size"
                else if (s === binding.etWeight.text) "Weight" else if (s === binding.etUPIId.text) "UPI"
                else if (s === binding.PhysicallyChallenged.selectedItem) "Physically Challenged" else "0"

            signUpValidation(currentField = currentField)



            if (binding.etFirstName.text.isNotEmpty() && binding.etLastName.text.isNotEmpty() && binding.etuserName.text.isNotEmpty()
                && binding.etEmail.text.isNotEmpty() && binding.etOtpVerify.text.isNotEmpty() && binding.etmOtpVerify.text.isNotEmpty() &&
                binding.etPassword.text.isNotEmpty() &&
                binding.etConfirmPassword.text.isNotEmpty() && binding.etConfirmPassword.text.toString() == binding.etPassword.text.toString() &&
                binding.etMobileNumber.text.isNotEmpty() && binding.etMobileNumber.text.length == 10 &&
                binding.etDOB.text.isNotEmpty() && binding.genderSpinner.selectedItem.toString() != "Select gender" &&
                binding.shoeSizeSpinner.selectedItem.toString() != "Select your shoe size" &&
                binding.etWeight.text.isNotEmpty() &&
                binding.PhysicallyChallenged.selectedItem.toString() != "Select physically challenged"
                && binding.RegisterTandC.isChecked && isEmailVerified && isPhoneVerified

            ) {
                binding.signUpClickDisable.isVisible = false
                binding.signUpClick.isVisible = true
            } else {
                binding.signUpClickDisable.isVisible = true
                binding.signUpClick.isVisible = false
            }


        }

    }



//userName

    private fun userNameObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.userApiResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                        }


                        is Resource.Success -> {
                            try {
                                if (response.data?.responseCode == 200) {

                                    if (response.data.result!!.isValid == true){
                                        binding.tvUserName.isVisible =  false
                                        binding.tvLastName.text = ""
                                        binding.userNameIcon.isVisible = true

                                        binding.userNameImage.loadImageResource(R.drawable.username_icon)
                                        binding.llUserName.setBackgroundResource(R.drawable.white_border_background)
                                    }


                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            displayToast(response.message)

                        }


                        is Resource.Empty -> {

                        }


                    }


                }

            }
        }
    }
    var searchFlag = false
    private val userNameWatcher = object: TextWatcher {
        private var searchJob: Job? = null
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            timer?.cancel()
        }

        override fun afterTextChanged(text: Editable?) {
            if((text.toString().isNotEmpty())) {
                timer = Timer()
                timer!!.schedule(object : TimerTask() {
                    override fun run() {
                        if (text.toString() == "" && binding.etuserName.tag as Boolean) {
                            searchFlag = false
                            viewModel.userNameApi(binding.etuserName.text.toString())
                        } else if (text.toString() != "") {
                            searchFlag = false
                            viewModel.userNameApi(binding.etuserName.text.toString())
                        }
                    }
                }, 600)
            } else {
                binding.tvUserName.isVisible =  false
                binding.tvLastName.text = ""
                binding.userNameIcon.isVisible = true
                binding.userNameImage.loadImageResource(R.drawable.cross_red)
                binding.llUserName.setBackgroundResource(R.drawable.white_border_background)
            }

        }
    }

    private fun signUpValidation(currentField: String) {
        FormValidations.signup(
            binding.etOtpVerify,
            binding.etmOtpVerify,
            binding.etFirstName,
            binding.firstNameLL,
            binding.tvFirstName,
            binding.etLastName,
            binding.lastNameLL,
            binding.tvLastName,
            binding.etuserName,
            binding.llUserName,
            binding.tvUserName,
            binding.etEmail,
            binding.llEmail,
            binding.tvEmail,
            binding.etPassword,
            binding.llPassWord,
            binding.etConfirmPassword,
            binding.llConfirmPassWord,
            binding.tvConfirmPassword,
            binding.etMobileNumber,
            binding.llMobileNumber,
            binding.tvMobileNumber,
            binding.etDOB,
            binding.llDOB,
            binding.tvDOB,
            binding.genderSpinner,
            binding.llGender,
            binding.TvGender,
            binding.shoeSizeSpinner,
            binding.llShoeSize,
            binding.TvShoeSize,
            binding.etWeight,
            binding.llWeight,
            binding.tvWeight,
            binding.tvEmailotp,
            binding.tvMobileOtp,
            binding.etUPIId,
            binding.llUPIId,
            binding.tvUPIId,
            binding.PhysicallyChallenged,
            binding.llPhysicallyChallenged,
            binding.TvPhysicallyChallenged,
            currentField,
            binding.userNameIcon,
            binding.emailNameImage,
            binding.mobileNameImage,
            binding.eOtpIcon,
            binding.mOtpIcon,
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
            this@SignUpActivity,
            binding.YesWalk,
            binding.NoWalk,
            binding.TvableToWalk,
            binding.RegisterTandC,
            binding.TvtermsAndConditions,
            binding.userNameImage,
            binding.verifiedIconImage,
          binding.verifiedIconMobImage
        )

    }

    private fun observeSignUpResponse() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.signUpApiResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                         Progress.start(this@SignUpActivity)
                        }

                        is Resource.Success -> {
                            Progress.stop()
                            try {
                                if (response.data?.statusCode == 200) {
                                    if (isFor == "Welcome") {
                                        AndroidExtension.alertBoxFinish(response.data.responseMessage,this@SignUpActivity,this@SignUpActivity)
                                    } else {
                                        AndroidExtension.alertBoxFinish(response.data.responseMessage,this@SignUpActivity,this@SignUpActivity)
                                    }

                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            try {
                                response.message?.let {message ->
                                    AndroidExtension.alertBox(message,this@SignUpActivity)
                                }
                            }catch (e:Exception){
                                e.printStackTrace()
                            }



                        }


                        is Resource.Empty -> {

                        }


                    }


                }

            }
        }
    }

    override fun applyClick() {
        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
        finishAfterTransition()
    }

    private fun isWithinAllowedRange(date: Calendar, minBirthYear: Int, maxBirthYear: Int): Boolean {
        val currentDate = Calendar.getInstance()
        currentDate.add(Calendar.YEAR, - minAllowedAge)
        val minAllowedDate = currentDate.time

        currentDate.add(Calendar.YEAR, -maxAllowedAge)
        val maxAllowedDate = currentDate.time

        return !date.before(minAllowedDate) && !date.after(maxAllowedDate)
    }


    private fun handleValidDate(date: Calendar) {
        val dateFormat = String.format(
            "%04d-%02d-%02d",
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH) + 1,  // Note: Calendar months are zero-based
            date.get(Calendar.DAY_OF_MONTH)
        )

        // Proceed with your logic using the valid date
        apiRequestDate = dateFormat
        binding.etDOB.text = DateFormat.dateFormatPicker(dateFormat)

        // Additional logic or actions related to the valid date can be added here
    }


    private fun isAbove18(date: Calendar): Boolean {
        val currentDate = Calendar.getInstance()
        currentDate.add(Calendar.YEAR, -18)
        return date.before(currentDate)
    }



}

