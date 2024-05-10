package com.fithun.utils

import android.content.Context
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.fithun.R


object FormValidations {

    val emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$"
    var MobilePattern = "[0-9]{10}"
    val Password = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$"
    val Name = "^[A-Za-z]+$"
    val UserName = "^(?=[a-zA-Z0-9._]{8,20}\$)(?!.*[_.]{2})[^_.].*[^_.]\$"
    var any_Number = "[0-9]"
    var link =
        "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})"
    var otp_regex = "^[0-9]{6}\$"

    fun login(
        etMobileNumber: EditText,
        llMobileNumber: LinearLayout,
        tvMobile: TextView,
        etPassword: EditText,
        llPassWord: LinearLayout,
        tvPassword: TextView,
        field: String
    ) {
        when(field){

            "Email" -> {
                val input = etMobileNumber.text.toString().trim()
                val mobilePattern = "\\d{10}"

                if (input.isEmpty()){
                    tvMobile.isVisible =  true

                    tvMobile.text = "Please enter email/mobile number."
                    llMobileNumber.setBackgroundResource(R.drawable.errordrawable)

                }else if (!input.matches(Regex(emailPattern)) && !input.matches(Regex(mobilePattern))) {
                    tvMobile.isVisible =  true
                    tvMobile.text = "Please enter valid email/mobile number."
                    llMobileNumber.setBackgroundResource(R.drawable.errordrawable)

                } else {
                    tvMobile.isVisible =  false
                    tvMobile.text = ""
                    llMobileNumber.setBackgroundResource(R.drawable.white_border_background)
                }

            }

            "Password" -> {
                if (etPassword.text.isEmpty()){
                    tvPassword.isVisible =  true
                    tvPassword.text = "Please enter password."
                    llPassWord.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvPassword.isVisible =  false
                    tvPassword.text = ""
                    llPassWord.setBackgroundResource(R.drawable.white_border_background)
                }
            }

            else -> {
                if (etMobileNumber.text.isEmpty()){
                    tvMobile.isVisible =  true
                    tvMobile.text = "Please enter email/mobile number."
                    llMobileNumber.setBackgroundResource(R.drawable.errordrawable)

                }

                if (etPassword.text.isEmpty()){
                    tvPassword.isVisible =  true
                    tvPassword.text = "Please enter password."
                    llPassWord.setBackgroundResource(R.drawable.errordrawable)
                }

            }
        }
    }


    fun redeemCoin(
        llAmount: LinearLayout,
        amount: EditText,
        txtAmount: TextView,
        upiIdll: LinearLayout,
        upiId: TextView,
        txtUpiId: TextView,
        mobilell: LinearLayout,
        mobileNumber: EditText,
        txtMobile: TextView,
        field: String
    ) {
        when(field){

            ""->{
                if (amount.text.isEmpty()){
                    txtAmount.isVisible =  true
                    txtAmount.text = "Please enter amount."
                    llAmount.setBackgroundResource(R.drawable.errordrawable)
                }
                if (upiId.text.isEmpty()){
                    txtUpiId.isVisible =  true
                    txtUpiId.text = "Please enter UPI ID."
                    upiIdll.setBackgroundResource(R.drawable.errordrawable)
                }
                if (mobileNumber.text.isEmpty()){
                    txtMobile.isVisible =  true
                    txtMobile.text = "Please enter Mobile no. linked with UPI ID."
                    mobilell.setBackgroundResource(R.drawable.errordrawable)
                }
            }

            "Amount" -> {

                if (amount.text.isEmpty()){
                    txtAmount.isVisible =  true
                    txtAmount.text = "Please enter amount."
                    llAmount.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    txtAmount.isVisible =  false
                    txtAmount.text = ""
                    llAmount.setBackgroundResource(R.drawable.white_border_background)
                }
            }

            "upiId" -> {
                if (upiId.text.isEmpty()){
                    txtUpiId.isVisible =  true
                    txtUpiId.text = "Please enter UPI ID."
                    upiIdll.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    txtUpiId.isVisible =  false
                    txtUpiId.text = ""
                    upiIdll.setBackgroundResource(R.drawable.white_border_background)
                }
            }

            "Mobile" -> {
                if (mobileNumber.text.isEmpty()){
                    txtMobile.isVisible =  true
                    txtMobile.text = "Please enter Mobile no. linked with UPI ID.."
                    mobilell.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    txtMobile.isVisible =  false
                    txtMobile.text = ""
                    mobilell.setBackgroundResource(R.drawable.white_border_background)
                }
            }
        }


    }


    fun signup(

        etOtpVerify: EditText,
        etmOtpVerify: EditText,
        etFirstName: EditText,
        firstNameLL: LinearLayout,
        tvFirstName: TextView,
        etLastName: EditText,
        lastNameLL: LinearLayout,
        tvLastName: TextView,
        etuserName: EditText,
        llUserName: LinearLayout,
        tvUserName: TextView,
        etEmail: EditText,
        llEmail: LinearLayout,
        tvEmail: TextView,
        etPassword: EditText,
        llPassWord: LinearLayout,
        etConfirmPassword: EditText,
        llConfirmPassWord: LinearLayout,
        tvConfirmPassword: TextView,
        etMobileNumber: EditText,
        llMobileNumber: LinearLayout,
        tvMobileNumber: TextView,
        etDOB: TextView,
        llDOB: LinearLayout,
        tvDOB: TextView,
        genderSpinner: Spinner,
        llGender: LinearLayout,
        tvGender: TextView,
        shoeSizeSpinner: Spinner,
        llShoeSize: LinearLayout,
        tvShoeSize: TextView,
        etWeight: EditText,
        llWeight: LinearLayout,
        tvWeight: TextView,
        tvEmailotp: TextView,
        tvMobileOtp: TextView,
        etUPIId: EditText,
        llUPIId: LinearLayout,
        tvUPIId: TextView,
        physicallyChallenged: Spinner,
        llPhysicallyChallenged: LinearLayout,
        tvPhysicallyChallenged: TextView,
        currentField: String,
        userNameIcon: LinearLayout,
        emailNameIcon: ImageView,
        mobileNameIcon: ImageView,
        eOtpIcon: LinearLayout,
        mOtpIcon: LinearLayout,


        passwordValidation: LinearLayout,
        eightCharVaildLL: LinearLayout,
        eightCharImage: ImageView,
        eightCharText: TextView,
        oneLowerCasell: LinearLayout,
        oneLowerCaseImage: ImageView,
        oneLowerCaseText: TextView,
        oneUpperCaseLL: LinearLayout,
        oneUpperCaseImage: ImageView,
        oneUpperCaseText: TextView,
        oneNumberLL: LinearLayout,
        oneNumberImage: ImageView,
        oneNumberText: TextView,
        oneSymbolLL: LinearLayout,
        oneSymbolImage: ImageView,
        oneSymbolText: TextView,
        context: Context,
        yesWalk: CheckBox,
        noWalk: CheckBox,
        tvableToWalk: TextView,
        registerTandC: CheckBox,
        tvtermsAndConditions: TextView,
        userNameImage: ImageView,
        verifiedIconImage: ImageView,
        verifiedIconMobImage: ImageView
    ) {
        when(currentField){

            ""-> {

                if (etFirstName.text.isEmpty()){
                    tvFirstName.isVisible =  true
                    tvFirstName.text = "Please enter first name."
                    firstNameLL.setBackgroundResource(R.drawable.errordrawable)
                }
                if (etLastName.text.isEmpty()){
                    tvLastName.isVisible =  true
                    tvLastName.text = "Please enter last name."
                    lastNameLL.setBackgroundResource(R.drawable.errordrawable)
                }
                if (etuserName.text.isEmpty()){
                    tvUserName.isVisible =  true
                    userNameImage.isVisible = true
                    Glide.with(context).load(R.drawable.cross_red).into(userNameImage)
                    tvUserName.text = "Please enter user name."
                    llUserName.setBackgroundResource(R.drawable.errordrawable)
                }
                if (etEmail.text.isEmpty()){
                    tvEmail.isVisible =  true
                    tvEmail.text = "Please enter email."
                    llEmail.setBackgroundResource(R.drawable.errordrawable)
                }

                if (etConfirmPassword.text.isEmpty()){
                    tvConfirmPassword.isVisible =  true
                    tvConfirmPassword.text = "Please enter confirm password."
                    llConfirmPassWord.setBackgroundResource(R.drawable.errordrawable)
                }
                if (etMobileNumber.text.isEmpty()){
                    tvMobileNumber.isVisible =  true
                    tvMobileNumber.text = "Please enter mobile number."
                    llMobileNumber.setBackgroundResource(R.drawable.errordrawable)
                }
                if (etDOB.text.isEmpty()){
                    tvDOB.isVisible =  true
                    tvDOB.text = "Please enter date of birth."
                    llDOB.setBackgroundResource(R.drawable.errordrawable)
                }
                if (genderSpinner.selectedItem.equals("Select gender")){
                    tvGender.isVisible =  true
                    tvGender.text = "Please select gender."
                    llGender.setBackgroundResource(R.drawable.errordrawable)
                }
                if (shoeSizeSpinner.selectedItem.equals("Select your shoe size")){
                    tvShoeSize.isVisible =  true
                    tvShoeSize.text = "Please select your shoe size."
                    llShoeSize.setBackgroundResource(R.drawable.errordrawable)
                }

                if (etWeight.text.isEmpty()){
                    tvWeight.isVisible =  true
                    tvWeight.text = "Please enter weight."
                    llWeight.setBackgroundResource(R.drawable.errordrawable)
                }



                if (physicallyChallenged.selectedItem.equals("Select physically Challenged")){
                    tvPhysicallyChallenged.isVisible =  true
                    tvPhysicallyChallenged.text = "Please select."
                    llPhysicallyChallenged.setBackgroundResource(R.drawable.errordrawable)
                }



                if (!registerTandC.isChecked){
                    tvtermsAndConditions.isVisible =  true
                    tvtermsAndConditions.text = "Please select terms & condition."

                }




            }
            "First Name" -> {

                if (etFirstName.text.isEmpty()){
                    tvFirstName.isVisible =  true
                    tvFirstName.text = "Please enter first name."
                    firstNameLL.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvFirstName.isVisible =  false
                    tvFirstName.text = ""
                    firstNameLL.setBackgroundResource(R.drawable.white_border_background)
                }

            }
            "Last Name" -> {

                if (etLastName.text.isEmpty()){
                    tvLastName.isVisible =  true
                    tvLastName.text = "Please enter last name."
                    lastNameLL.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvLastName.isVisible =  false
                    tvLastName.text = ""
                    lastNameLL.setBackgroundResource(R.drawable.white_border_background)
                }

            }
            "User Name" -> {

                if (etuserName.text.isEmpty()){
                    tvUserName.isVisible =  true
                    userNameIcon.isVisible = true
                    Glide.with(context).load(R.drawable.cross_red).into(userNameImage)
                    tvUserName.text = "Please enter user name."
                    llUserName.setBackgroundResource(R.drawable.errordrawable)
                }

            }
            "Email" -> {
                if (etEmail.text.isEmpty()){
                    tvEmail.isVisible =  true
                    emailNameIcon.isVisible = false


                    tvEmail.text = "Please enter email."
                    llEmail.setBackgroundResource(R.drawable.errordrawable)
                }else if (!etEmail.text.matches(Regex(emailPattern))){
                    tvEmail.isVisible =  true
                    tvEmail.text = "Please enter a valid email."
                    emailNameIcon.isVisible = false

                    llEmail.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvEmail.isVisible =  false
                    tvEmail.text = ""
                    emailNameIcon.isVisible = true
                    verifiedIconImage.isVisible = false


                    llEmail.setBackgroundResource(R.drawable.white_border_background)
                }

            }
            "OTP" -> {


                if (etOtpVerify.text.isEmpty()) {
                    tvEmailotp.isVisible = true
                    eOtpIcon.isVisible = false
                    tvEmailotp.text = "Please enter otp."
                    llMobileNumber.setBackgroundResource(R.drawable.errordrawable)

                } else if (etOtpVerify.text.length < 6) {
                    tvEmailotp.isVisible = true
                    eOtpIcon.isVisible = false
                    tvEmailotp.text = "Please enter valid otp."
                    llMobileNumber.setBackgroundResource(R.drawable.errordrawable)

                } else {
                    tvEmailotp.isVisible = false
                    eOtpIcon.isVisible = true
                   verifiedIconMobImage.isVisible = false
                    tvEmail.text = ""
                    llMobileNumber.setBackgroundResource(R.drawable.white_border_background)

                }


            }
         "MobOTP" -> {


                if (etmOtpVerify.text.isEmpty()) {
                    tvMobileOtp.isVisible = true
                    mOtpIcon.isVisible = false
                    tvMobileOtp.text = "Please enter otp."
                    llMobileNumber.setBackgroundResource(R.drawable.errordrawable)

                } else if (etmOtpVerify.text.length < 6) {
                    tvMobileOtp.isVisible = true
                    mOtpIcon.isVisible = false
                    tvMobileOtp.text = "Please enter valid otp."
                    llMobileNumber.setBackgroundResource(R.drawable.errordrawable)

                } else {
                    tvMobileOtp.isVisible = false
                    mOtpIcon.isVisible = true
                    tvEmail.text = ""

                    llMobileNumber.setBackgroundResource(R.drawable.white_border_background)

                }


            }






            "Create Password" -> {
                passwordValidation.isVisible = true
                val password = etPassword.text.toString()
                var hasMinLength = false
                var hasLowercase = false
                var hasUppercase = false
                var hasNumber = false
                var hasSymbol = false
                if (password.length >= 8) {
                    hasMinLength = true
                }
                if (Regex("[a-z]").containsMatchIn(password)) {
                    hasLowercase = true
                }

                if (Regex("[A-Z]").containsMatchIn(password)) {
                    hasUppercase = true
                }

                if (Regex("[0-9]").containsMatchIn(password)) {
                    hasNumber = true
                }
                if (Regex("[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>?]").containsMatchIn(password)) {
                    hasSymbol = true
                }

                eightCharImage.loadImageResource(if (hasMinLength) R.drawable.right_tick_colored else R.drawable.right_tick)
                oneLowerCaseImage.loadImageResource(if (hasLowercase) R.drawable.right_tick_colored else R.drawable.right_tick)
                oneUpperCaseImage.loadImageResource(if (hasUppercase) R.drawable.right_tick_colored else R.drawable.right_tick)
                oneNumberImage.loadImageResource(if (hasNumber) R.drawable.right_tick_colored else R.drawable.right_tick)
                oneSymbolImage.loadImageResource(if (hasSymbol) R.drawable.right_tick_colored else R.drawable.right_tick)

                eightCharText.setTextColor(ContextCompat.getColor(context, if (hasMinLength) R.color.theme_color else R.color.password_text_color))
                oneLowerCaseText.setTextColor(ContextCompat.getColor(context, if (hasLowercase) R.color.theme_color else R.color.password_text_color))
                oneUpperCaseText.setTextColor(ContextCompat.getColor(context, if (hasUppercase) R.color.theme_color else R.color.password_text_color))
                oneNumberText.setTextColor(ContextCompat.getColor(context, if (hasNumber) R.color.theme_color else R.color.password_text_color))
                oneSymbolText.setTextColor(ContextCompat.getColor(context, if (hasSymbol) R.color.theme_color else R.color.password_text_color))

                eightCharVaildLL.setBackgroundResource(if (hasMinLength) R.drawable.password_validation_background else R.drawable.password_validation)
                oneLowerCasell.setBackgroundResource(if (hasLowercase) R.drawable.password_validation_background else R.drawable.password_validation)
                oneUpperCaseLL.setBackgroundResource(if (hasUppercase) R.drawable.password_validation_background else R.drawable.password_validation)
                oneNumberLL.setBackgroundResource(if (hasNumber) R.drawable.password_validation_background else R.drawable.password_validation)
                oneSymbolLL.setBackgroundResource(if (hasSymbol) R.drawable.password_validation_background else R.drawable.password_validation)
            }
            "Confirm Password" -> {
                if (etPassword.text.isEmpty()){
                    passwordValidation.isVisible = true

                }else if (etConfirmPassword.text.isEmpty()){
                    passwordValidation.isVisible = false
                    tvConfirmPassword.isVisible = true
                    tvConfirmPassword.text = "Please enter confirm password."
                    llConfirmPassWord.setBackgroundResource(R.drawable.errordrawable)
                }else if(etConfirmPassword.text.toString() != etPassword.text.toString()){
                    tvConfirmPassword.isVisible = true
                    passwordValidation.isVisible = false
                    tvConfirmPassword.text = "Password and confirm password should match."
                    llConfirmPassWord.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvConfirmPassword.isVisible = false
                    passwordValidation.isVisible = false
                    tvConfirmPassword.text = ""
                    llConfirmPassWord.setBackgroundResource(R.drawable.white_border_background)
                }

            }
            "Mobile Number" -> {
                if (etMobileNumber.text.isEmpty()){
                    tvMobileNumber.isVisible = true
                    mobileNameIcon.isVisible = false


                    tvMobileNumber.text = "Please enter mobile number."
                    llMobileNumber.setBackgroundResource(R.drawable.errordrawable)
                }else if(etMobileNumber.text.length < 9){
                    tvMobileNumber.isVisible = true
                    mobileNameIcon.isVisible = false

                    tvMobileNumber.text = "Please enter a valid mobile number."
                    llMobileNumber.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvMobileNumber.isVisible = false
                    mobileNameIcon.isVisible = true
                    tvMobileNumber.text = ""
                    verifiedIconMobImage.isVisible = false
                    llMobileNumber.setBackgroundResource(R.drawable.white_border_background)
                }

            }
            "DOB" -> {
                if (etDOB.text.isEmpty()){
                    tvDOB.isVisible = true
                    tvDOB.text = "Please select date of birth."
                    llDOB.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvDOB.isVisible = false
                    tvDOB.text = ""
                    llDOB.setBackgroundResource(R.drawable.white_border_background)
                }

            }
            "Gender" -> {
                if (genderSpinner.selectedItem.equals("Select gender")){
                    tvGender.isVisible =  true
                    tvGender.text = "Please select gender"
                    llGender.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvGender.isVisible = false
                    tvGender.text = ""
                    llGender.setBackgroundResource(R.drawable.white_border_background)
                }

            }
            "Shoe Size" -> {
                if (shoeSizeSpinner.selectedItem.equals("Select your shoe size")){
                    tvShoeSize.isVisible =  true
                    tvShoeSize.text = "Please select your shoe size."
                    llShoeSize.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvShoeSize.isVisible = false
                    tvShoeSize.text = ""
                    llShoeSize.setBackgroundResource(R.drawable.white_border_background)
                }

            }
            "Weight" -> {
                if (etWeight.text.isEmpty()){
                    tvWeight.isVisible =  true
                    tvWeight.text = "Please enter weight."
                    llWeight.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvWeight.isVisible = false
                    tvWeight.text = ""
                    llWeight.setBackgroundResource(R.drawable.white_border_background)
                }

            }

            "Physically Challenged" -> {
                if (physicallyChallenged.selectedItem.equals("Select physically challenged")){
                    tvPhysicallyChallenged.isVisible =  true
                    tvPhysicallyChallenged.text = "Please select"
                    llPhysicallyChallenged.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvPhysicallyChallenged.isVisible = false
                    tvPhysicallyChallenged.text = ""
                    llPhysicallyChallenged.setBackgroundResource(R.drawable.white_border_background)
                }

            }
            "Terms And Condition" -> {
                if (!registerTandC.isChecked){
                    tvtermsAndConditions.isVisible =  true
                    tvtermsAndConditions.text = "Please select terms & conditions"

                }else{
                    tvtermsAndConditions.isVisible =  false
                    tvtermsAndConditions.text = ""
                }
            }




        }






    }



    fun forgotPassword(
        etMobileNumberAndEmail: EditText,
        llMobileNumber: LinearLayout,
        tvMobile: TextView,
        field: String
    ) {
        val input = etMobileNumberAndEmail.text.toString().trim()
        when(field){

            "Email" -> {

                val mobilePattern = "\\d{10}"

                if (input.isEmpty()) {
                    tvMobile.isVisible = true
                    tvMobile.text = "Please enter email/mobile number."
                    llMobileNumber.setBackgroundResource(R.drawable.errordrawable)

                } else if (!input.matches(Regex(emailPattern)) && !input.matches(Regex(mobilePattern))) {
                    tvMobile.isVisible = true
                    tvMobile.text = "Please enter valid email/mobile number."
                    llMobileNumber.setBackgroundResource(R.drawable.errordrawable)

                } else {
                    tvMobile.isVisible = false
                    tvMobile.text = ""
                    llMobileNumber.setBackgroundResource(R.drawable.white_border_background)

                }


            }

            else -> {
                if (input.isEmpty()) {
                    tvMobile.isVisible = true
                    tvMobile.text = "Please enter email/mobile number."
                    llMobileNumber.setBackgroundResource(R.drawable.errordrawable)

                }
            }



        }






    }


    fun otpVerification(
        etMobileNumberAndEmail: EditText,
        llMobileNumber: LinearLayout,
        tvMobile: TextView,
        field: String
    ) {
        val input = etMobileNumberAndEmail.text.toString().trim()
        when(field){

            "OTP" -> {


                if (input.isEmpty()) {
                    tvMobile.isVisible = true
                    tvMobile.text = "Please enter otp"
                    llMobileNumber.setBackgroundResource(R.drawable.errordrawable)

                } else if (input.length < 6) {
                    tvMobile.isVisible = true
                    tvMobile.text = "Please enter valid otp"
                    llMobileNumber.setBackgroundResource(R.drawable.errordrawable)

                } else {
                    tvMobile.isVisible = false
                    tvMobile.text = ""
                    llMobileNumber.setBackgroundResource(R.drawable.white_border_background)

                }


            }

            else -> {
                if (input.isEmpty()) {
                    tvMobile.isVisible = true
                    tvMobile.text = "Please enter otp."
                    llMobileNumber.setBackgroundResource(R.drawable.errordrawable)

                }
            }



        }






    }



    fun resetPasswordword(
                      etPassword: EditText,
                      llPassWord: LinearLayout,
                      etConfirmPassword: EditText,
                      llConfirmPassWord: LinearLayout,
                      tvConfirmPassword: TextView,
                      passwordValidation: LinearLayout,
                      eightCharVaildLL: LinearLayout,
                      eightCharImage: ImageView,
                      eightCharText: TextView,
                      oneLowerCasell: LinearLayout,
                      oneLowerCaseImage: ImageView,
                      oneLowerCaseText: TextView,
                      oneUpperCaseLL: LinearLayout,
                      oneUpperCaseImage: ImageView,
                      oneUpperCaseText: TextView,
                      oneNumberLL: LinearLayout,
                      oneNumberImage: ImageView,
                      oneNumberText: TextView,
                      oneSymbolLL: LinearLayout,
                      oneSymbolImage: ImageView,
                      oneSymbolText: TextView,
                      context: Context,
    field: String){
        passwordValidation.isVisible = true
        when(field){

            "" -> {

                if (etConfirmPassword.text.isEmpty()){
                    tvConfirmPassword.isVisible =  true
                    tvConfirmPassword.text = "Please enter confirm password."
                    llConfirmPassWord.setBackgroundResource(R.drawable.errordrawable)
                }

            }

            "Create Password" -> {
                passwordValidation.isVisible = true
                val password = etPassword.text.toString()
                var hasMinLength = false
                var hasLowercase = false
                var hasUppercase = false
                var hasNumber = false
                var hasSymbol = false
                if (password.length >= 8) {
                    hasMinLength = true
                }
                if (Regex("[a-z]").containsMatchIn(password)) {
                    hasLowercase = true
                }

                if (Regex("[A-Z]").containsMatchIn(password)) {
                    hasUppercase = true
                }

                if (Regex("[0-9]").containsMatchIn(password)) {
                    hasNumber = true
                }
                if (Regex("[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>?]").containsMatchIn(password)) {
                    hasSymbol = true
                }

                eightCharImage.loadImageResource(if (hasMinLength) R.drawable.right_tick_colored else R.drawable.right_tick)
                oneLowerCaseImage.loadImageResource(if (hasLowercase) R.drawable.right_tick_colored else R.drawable.right_tick)
                oneUpperCaseImage.loadImageResource(if (hasUppercase) R.drawable.right_tick_colored else R.drawable.right_tick)
                oneNumberImage.loadImageResource(if (hasNumber) R.drawable.right_tick_colored else R.drawable.right_tick)
                oneSymbolImage.loadImageResource(if (hasSymbol) R.drawable.right_tick_colored else R.drawable.right_tick)

                eightCharText.setTextColor(ContextCompat.getColor(context, if (hasMinLength) R.color.theme_color else R.color.password_text_color))
                oneLowerCaseText.setTextColor(ContextCompat.getColor(context, if (hasLowercase) R.color.theme_color else R.color.password_text_color))
                oneUpperCaseText.setTextColor(ContextCompat.getColor(context, if (hasUppercase) R.color.theme_color else R.color.password_text_color))
                oneNumberText.setTextColor(ContextCompat.getColor(context, if (hasNumber) R.color.theme_color else R.color.password_text_color))
                oneSymbolText.setTextColor(ContextCompat.getColor(context, if (hasSymbol) R.color.theme_color else R.color.password_text_color))

                eightCharVaildLL.setBackgroundResource(if (hasMinLength) R.drawable.password_validation_background else R.drawable.password_validation)
                oneLowerCasell.setBackgroundResource(if (hasLowercase) R.drawable.password_validation_background else R.drawable.password_validation)
                oneUpperCaseLL.setBackgroundResource(if (hasUppercase) R.drawable.password_validation_background else R.drawable.password_validation)
                oneNumberLL.setBackgroundResource(if (hasNumber) R.drawable.password_validation_background else R.drawable.password_validation)
                oneSymbolLL.setBackgroundResource(if (hasSymbol) R.drawable.password_validation_background else R.drawable.password_validation)
            }
            "Confirm Password" -> {
                if (etConfirmPassword.text.isEmpty()){
                    tvConfirmPassword.isVisible = true
                    tvConfirmPassword.text = "Please enter confirm password."
                    llConfirmPassWord.setBackgroundResource(R.drawable.errordrawable)
                }else if(etConfirmPassword.text.toString() != etPassword.text.toString()){
                    tvConfirmPassword.isVisible = true
                    tvConfirmPassword.text = "Password and confirm password should match."
                    llConfirmPassWord.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvConfirmPassword.isVisible = false
                    tvConfirmPassword.text = ""
                    llConfirmPassWord.setBackgroundResource(R.drawable.white_border_background)
                }

            }
        }


    }

    fun changePassword(
        etCurrentPassword: EditText,
        llPassWordCurrent: LinearLayout,
        tvCurrentPassword: TextView,
        etPassword: EditText,
        llPassWord: LinearLayout,
        etConfirmPassword: EditText,
        llConfirmPassWord: LinearLayout,
        tvConfirmPassword: TextView,
        passwordValidation: LinearLayout,
        eightCharVaildLL: LinearLayout,
        eightCharImage: ImageView,
        eightCharText: TextView,
        oneLowerCasell: LinearLayout,
        oneLowerCaseImage: ImageView,
        oneLowerCaseText: TextView,
        oneUpperCaseLL: LinearLayout,
        oneUpperCaseImage: ImageView,
        oneUpperCaseText: TextView,
        oneNumberLL: LinearLayout,
        oneNumberImage: ImageView,
        oneNumberText: TextView,
        oneSymbolLL: LinearLayout,
        oneSymbolImage: ImageView,
        oneSymbolText: TextView,
        context:Context,
        field: String,


    ) {
        passwordValidation.isVisible = true
        when(field){

            "" -> {

                if (etCurrentPassword.text.isEmpty()){
                    tvCurrentPassword.isVisible =  true
                    tvCurrentPassword.text = "Please enter current password."
                    llPassWordCurrent.setBackgroundResource(R.drawable.errordrawable)
                }
                if (etConfirmPassword.text.isEmpty()){
                    tvConfirmPassword.isVisible =  true
                    tvConfirmPassword.text = "Please enter confirm password."
                    llConfirmPassWord.setBackgroundResource(R.drawable.errordrawable)
                }

            }

            "Current Password" -> {
                if (etCurrentPassword.text.isEmpty()){
                    tvCurrentPassword.isVisible =  true
                    tvCurrentPassword.text = "Please enter current password."
                    llPassWordCurrent.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvCurrentPassword.isVisible =  false
                    tvCurrentPassword.text = ""
                    llPassWordCurrent.setBackgroundResource(R.drawable.white_border_background)
                }
            }
            "Create Password" -> {
                passwordValidation.isVisible = true
                val password = etPassword.text.toString()
                var hasMinLength = false
                var hasLowercase = false
                var hasUppercase = false
                var hasNumber = false
                var hasSymbol = false
                if (password.length >= 8) {
                    hasMinLength = true
                }
                if (Regex("[a-z]").containsMatchIn(password)) {
                    hasLowercase = true
                }

                if (Regex("[A-Z]").containsMatchIn(password)) {
                    hasUppercase = true
                }

                if (Regex("[0-9]").containsMatchIn(password)) {
                    hasNumber = true
                }
                if (Regex("[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>?]").containsMatchIn(password)) {
                    hasSymbol = true
                }

                eightCharImage.loadImageResource(if (hasMinLength) R.drawable.right_tick_colored else R.drawable.right_tick)
                oneLowerCaseImage.loadImageResource(if (hasLowercase) R.drawable.right_tick_colored else R.drawable.right_tick)
                oneUpperCaseImage.loadImageResource(if (hasUppercase) R.drawable.right_tick_colored else R.drawable.right_tick)
                oneNumberImage.loadImageResource(if (hasNumber) R.drawable.right_tick_colored else R.drawable.right_tick)
                oneSymbolImage.loadImageResource(if (hasSymbol) R.drawable.right_tick_colored else R.drawable.right_tick)

                eightCharText.setTextColor(ContextCompat.getColor(context, if (hasMinLength) R.color.theme_color else R.color.password_text_color))
                oneLowerCaseText.setTextColor(ContextCompat.getColor(context, if (hasLowercase) R.color.theme_color else R.color.password_text_color))
                oneUpperCaseText.setTextColor(ContextCompat.getColor(context, if (hasUppercase) R.color.theme_color else R.color.password_text_color))
                oneNumberText.setTextColor(ContextCompat.getColor(context, if (hasNumber) R.color.theme_color else R.color.password_text_color))
                oneSymbolText.setTextColor(ContextCompat.getColor(context, if (hasSymbol) R.color.theme_color else R.color.password_text_color))

                eightCharVaildLL.setBackgroundResource(if (hasMinLength) R.drawable.password_validation_background else R.drawable.password_validation)
                oneLowerCasell.setBackgroundResource(if (hasLowercase) R.drawable.password_validation_background else R.drawable.password_validation)
                oneUpperCaseLL.setBackgroundResource(if (hasUppercase) R.drawable.password_validation_background else R.drawable.password_validation)
                oneNumberLL.setBackgroundResource(if (hasNumber) R.drawable.password_validation_background else R.drawable.password_validation)
                oneSymbolLL.setBackgroundResource(if (hasSymbol) R.drawable.password_validation_background else R.drawable.password_validation)
            }
            "Confirm Password" -> {
                if (etConfirmPassword.text.isEmpty()){
                    tvConfirmPassword.isVisible = true
                    tvConfirmPassword.text = "Please enter confirm password."
                    llConfirmPassWord.setBackgroundResource(R.drawable.errordrawable)
                }else if(etConfirmPassword.text.toString() != etPassword.text.toString()){
                    tvConfirmPassword.isVisible = true
                    tvConfirmPassword.text = "Password and confirm password should match."
                    llConfirmPassWord.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvConfirmPassword.isVisible = false
                    tvConfirmPassword.text = ""
                    llConfirmPassWord.setBackgroundResource(R.drawable.white_border_background)
                }

            }
        }


    }




    fun addAddress(
        etFirstName: EditText,
        firstNameLL: LinearLayout,
        tvFirstName: TextView,
        etLastName: EditText,
        lastNameLL: LinearLayout,
        tvLastName: TextView,
        etHouse: EditText,
        houseNumberLL: LinearLayout,
        tvHouseNumber: TextView,
        etArea: EditText,
        areaLL: LinearLayout,
        tvArea: TextView,
        etCountry: TextView,
        countryLL: LinearLayout,
        tvCountry: TextView,
        etState: TextView,
        stateLL: LinearLayout,
        tvState: TextView,
        etCity: TextView,
        cityLL: LinearLayout,
        tvCity: TextView,
        etZip: EditText,
        zipLL: LinearLayout,
        tvZip: TextView,
        field: String,
    ) {

        when(field){

            "" -> {
                if (etFirstName.text.isEmpty()){
                    tvFirstName.isVisible =  true
                    tvFirstName.text = "*Please enter first name"
                    firstNameLL.setBackgroundResource(R.drawable.errordrawable)
                }

                if (etLastName.text.isEmpty()){
                    tvLastName.isVisible =  true
                    tvLastName.text = "*Please enter last name"
                    lastNameLL.setBackgroundResource(R.drawable.errordrawable)
                }

                if (etHouse.text.isEmpty()){
                    tvHouseNumber.isVisible =  true
                    tvHouseNumber.text = "*Please enter house number"
                    houseNumberLL.setBackgroundResource(R.drawable.errordrawable)
                }

                if (etArea.text.isEmpty()){
                    tvArea.isVisible =  true
                    tvArea.text = "*Please enter area name"
                    areaLL.setBackgroundResource(R.drawable.errordrawable)
                }
                if (etCountry.text.isEmpty()){
                    tvCountry.isVisible =  true
                    tvCountry.text = "*Please select country"
                    countryLL.setBackgroundResource(R.drawable.errordrawable)
                }
                if (etState.text.isEmpty()){
                    tvState.isVisible =  true
                    tvState.text = "*Please select state"
                    stateLL.setBackgroundResource(R.drawable.errordrawable)
                }
                if (etCity.text.isEmpty()){
                    tvCity.isVisible =  true
                    tvCity.text = "*Please select city"
                    cityLL.setBackgroundResource(R.drawable.errordrawable)
                }
                if (etZip.text.isEmpty()){
                    tvZip.isVisible =  true
                    tvZip.text = "*Please enter zipcode"
                    zipLL.setBackgroundResource(R.drawable.errordrawable)
                }
            }


            "First Name" -> {
                if (etFirstName.text.isEmpty()){
                    tvFirstName.isVisible =  true
                    tvFirstName.text = "*Please enter first name"
                    firstNameLL.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvFirstName.isVisible =  false
                    tvFirstName.text = ""
                    firstNameLL.setBackgroundResource(R.drawable.white_border_background)
                }
            }

            "Last Name" -> {
                if (etLastName.text.isEmpty()){
                    tvLastName.isVisible =  true
                    tvLastName.text = "*Please enter last name"
                    lastNameLL.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvLastName.isVisible =  false
                    tvLastName.text = ""
                    lastNameLL.setBackgroundResource(R.drawable.white_border_background)
                }
            }

            "House Number" ->{
                if (etHouse.text.isEmpty()){
                    tvHouseNumber.isVisible =  true
                    tvHouseNumber.text = "*Please enter house number"
                    houseNumberLL.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvHouseNumber.isVisible =  false
                    tvHouseNumber.text = ""
                    houseNumberLL.setBackgroundResource(R.drawable.white_border_background)
                }
            }


            "Area" -> {
                if (etArea.text.isEmpty()){
                    tvArea.isVisible =  true
                    tvArea.text = "*Please enter area name"
                    areaLL.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvArea.isVisible =  false
                    tvArea.text = ""
                    areaLL.setBackgroundResource(R.drawable.white_border_background)
                }
            }


            "Country" -> {
                if (etCountry.text.isEmpty()){
                    tvCountry.isVisible =  true
                    tvCountry.text = "*Please select country"
                    countryLL.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvCountry.isVisible =  false
                    tvCountry.text = ""
                    countryLL.setBackgroundResource(R.drawable.white_border_background)
                }
            }


            "State" -> {
                if (etState.text.isEmpty()){
                    tvState.isVisible =  true
                    tvState.text = "*Please select state"
                    stateLL.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvState.isVisible =  false
                    tvState.text = ""
                    stateLL.setBackgroundResource(R.drawable.white_border_background)
                }
            }


            "City" -> {
                if (etCity.text.isEmpty()){
                    tvCity.isVisible =  true
                    tvCity.text = "*Please select city"
                    cityLL.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvCity.isVisible =  false
                    tvCity.text = ""
                    cityLL.setBackgroundResource(R.drawable.white_border_background)
                }
            }

            "ZipCode"->{
                if (etZip.text.isEmpty()){
                    tvZip.isVisible =  true
                    tvZip.text = "*Please enter zipcode"
                    zipLL.setBackgroundResource(R.drawable.errordrawable)
                }else if (etZip.text.length < 6){
                    tvZip.isVisible =  true
                    tvZip.text = "*Please enter 6 digit zipcode"
                    zipLL.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvZip.isVisible =  false
                    tvZip.text = ""
                    zipLL.setBackgroundResource(R.drawable.white_border_background)
                }
            }


        }




    }





    fun additionalDetails(
        etName: EditText,
        llName: LinearLayout,
        tvName: TextView,
        etuserName: EditText,
        llUserName: LinearLayout,
        tvUserName: TextView,
        etEmail: EditText,
        llEmail: LinearLayout,
        tvEmail: TextView,
        etMobileNumber: EditText,
        llMobileNumber: LinearLayout,
        tvMobileNumber: TextView,
        etDOB: TextView,
        llDOB: LinearLayout,
        tvDOB: TextView,
        genderSpinner: Spinner,
        llGender: LinearLayout,
        tvGender: TextView,
        shoeSizeSpinner: Spinner,
        llShoeSize: LinearLayout,
        tvShoeSize: TextView,
        etWeight: EditText,
        llWeight: LinearLayout,
        tvWeight: TextView,
        etUPIId: EditText,
        llUPIId: LinearLayout,
        tvUPIId: TextView,
        physicallyChallenged: Spinner,
        llPhysicallyChallenged: LinearLayout,
        tvPhysicallyChallenged: TextView,
        currentField: String,
        yesWalk: CheckBox,
        noWalk: CheckBox,
        tvableToWalk: TextView,



    ){
        println("currentField>>> $currentField")
        when(currentField){

            ""-> {
                if (etName.text.isEmpty()){
                    tvName.isVisible =  true


                    tvName.text = "Please enter user name."
                    llName.setBackgroundResource(R.drawable.errordrawable)
                }

                if (etuserName.text.isEmpty()){
                    tvUserName.isVisible =  true


                    tvUserName.text = "Please enter user name."
                    llUserName.setBackgroundResource(R.drawable.errordrawable)
                }
                if (etEmail.text.isEmpty()){
                    tvEmail.isVisible =  true
                    tvEmail.text = "Please enter email."
                    llEmail.setBackgroundResource(R.drawable.errordrawable)
                }


                if (etMobileNumber.text.isEmpty()){
                    tvMobileNumber.isVisible =  true
                    tvMobileNumber.text = "Please enter mobile number."
                    llMobileNumber.setBackgroundResource(R.drawable.errordrawable)
                }
                if (etDOB.text.isEmpty()){
                    tvDOB.isVisible =  true
                    tvDOB.text = "Please enter date of birth."
                    llDOB.setBackgroundResource(R.drawable.errordrawable)
                }
                if (genderSpinner.selectedItem.equals("Select gender")){
                    tvGender.isVisible =  true
                    tvGender.text = "Please select gender."
                    llGender.setBackgroundResource(R.drawable.errordrawable)
                }
                if (shoeSizeSpinner.selectedItem.equals("Select your shoe size")){
                    tvShoeSize.isVisible =  true
                    tvShoeSize.text = "Please select your shoe size."
                    llShoeSize.setBackgroundResource(R.drawable.errordrawable)
                }

                if (etWeight.text.isEmpty()){
                    tvWeight.isVisible =  true
                    tvWeight.text = "Please enter weight."
                    llWeight.setBackgroundResource(R.drawable.errordrawable)
                }


                if (physicallyChallenged.selectedItem.equals("Select physically Challenged")){
                    tvPhysicallyChallenged.isVisible =  true
                    tvPhysicallyChallenged.text = "Please select."
                    llPhysicallyChallenged.setBackgroundResource(R.drawable.errordrawable)
                }

                if (!yesWalk.isChecked && !noWalk.isChecked){
                    tvableToWalk.isVisible =  true
                    tvableToWalk.text = "Please select physically challenged."

                }






            }
            "Name" -> {

                if (etName.text.isEmpty()){
                    tvName.isVisible =  true

                    tvName.text = "Please enter user name."
                    llUserName.setBackgroundResource(R.drawable.errordrawable)
                }


            }

            "User Name" -> {

                if (etuserName.text.isEmpty()){
                    tvUserName.isVisible =  true
                    tvUserName.text = "Please enter user name."
                    llUserName.setBackgroundResource(R.drawable.errordrawable)
                }


            }
            "Email" -> {
                if (etEmail.text.isEmpty()){
                    tvEmail.isVisible =  true



                    tvEmail.text = "Please enter email."
                    llEmail.setBackgroundResource(R.drawable.errordrawable)
                }else if (!etEmail.text.matches(Regex(emailPattern))){
                    tvEmail.isVisible =  true
                    tvEmail.text = "Please enter a valid email."


                    llEmail.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvEmail.isVisible =  false
                    tvEmail.text = ""


                    llEmail.setBackgroundResource(R.drawable.white_border_background)
                }

            }
            "Mobile Number" -> {
                if (etMobileNumber.text.isEmpty()){
                    tvMobileNumber.isVisible = true



                    tvMobileNumber.text = "Please enter mobile number."
                    llMobileNumber.setBackgroundResource(R.drawable.errordrawable)
                }else if(etMobileNumber.text.length < 10){
                    tvMobileNumber.isVisible = true


                    tvMobileNumber.text = "Please enter a valid mobile number."
                    llMobileNumber.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvMobileNumber.isVisible = false

                    tvMobileNumber.text = ""
                    llMobileNumber.setBackgroundResource(R.drawable.white_border_background)
                }

            }
            "DOB" -> {
                if (etDOB.text.isEmpty()){
                    tvDOB.isVisible = true
                    tvDOB.text = "Please select date of birth."
                    llDOB.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvDOB.isVisible = false
                    tvDOB.text = ""
                    llDOB.setBackgroundResource(R.drawable.white_border_background)
                }

            }
            "Gender" -> {
                if (genderSpinner.selectedItem.equals("Select gender")){
                    tvGender.isVisible =  true
                    tvGender.text = "Please select gender"
                    llGender.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvGender.isVisible = false
                    tvGender.text = ""
                    llGender.setBackgroundResource(R.drawable.white_border_background)
                }

            }
            "Shoe Size" -> {
                if (shoeSizeSpinner.selectedItem.equals("Select your shoe size")){
                    tvShoeSize.isVisible =  true
                    tvShoeSize.text = "Please select your shoe size."
                    llShoeSize.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvShoeSize.isVisible = false
                    tvShoeSize.text = ""
                    llShoeSize.setBackgroundResource(R.drawable.white_border_background)
                }

            }
            "Weight" -> {
                if (etWeight.text.isEmpty()){
                    tvWeight.isVisible =  true
                    tvWeight.text = "Please enter weight."
                    llWeight.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvWeight.isVisible = false
                    tvWeight.text = ""
                    llWeight.setBackgroundResource(R.drawable.white_border_background)
                }

            }

            "Physically Challenged" -> {
                if (physicallyChallenged.selectedItem.equals("Select physically Challenged")){
                    tvPhysicallyChallenged.isVisible =  true
                    tvPhysicallyChallenged.text = "Please select"
                    llPhysicallyChallenged.setBackgroundResource(R.drawable.errordrawable)
                }else{
                    tvPhysicallyChallenged.isVisible = false
                    tvPhysicallyChallenged.text = ""
                    llPhysicallyChallenged.setBackgroundResource(R.drawable.white_border_background)
                }

            }





        }






    }

    fun editDetails(
        tvFirstName: TextView,
        tvDOB: TextView,
        tvLastName: TextView,
        etDOB: TextView,
        etUpiMobileNumber: EditText,
        etLastName: EditText,
        etFirstName: EditText,
        etHomeAddress: EditText,
        etOfficeAddress: EditText,
        tvEmail: TextView,
        tvHomeAdd: TextView,
        tvOfficeAdd: TextView,
        tvUPIId: TextView,
        tvOfficeAdd1: TextView,
        tvMobileNo: TextView,
        etEmail: EditText,
        llEmail: LinearLayout,
        etMobileNumber: EditText,
        llMobileNumber: LinearLayout,

        llDOB: LinearLayout,

        genderSpinner: Spinner,
        llGender: LinearLayout,
        etUPIId: EditText,
        llUPIId: LinearLayout,
        currentField: String,
        firstNameLL: LinearLayout,
        lastNameLL: LinearLayout,
        tvGender: TextView,
        llinkedUP: LinearLayout,
        tvLinkedUPIId: TextView,
        llHome: LinearLayout,
        llOffice: LinearLayout,
        etWeight: EditText,
        llWeight: LinearLayout,
        tvWeight: TextView,
        emailNameImage: ImageView,
        verifiedIconImage: ImageView,
        mobileNameImage: ImageView,
        verifiedIconMobImage: ImageView,
        isValidateForOtp: Boolean
    ) {
        when (currentField) {

            "" -> {


                tvFirstName.isVisible = false
                tvGender.isVisible = false
                tvDOB.isVisible = false
                tvUPIId.isVisible = false
                tvWeight.isVisible = false
                tvMobileNo.isVisible = false
                tvLastName.isVisible = false
                llDOB.setBackgroundResource(R.drawable.white_border_background)
                tvEmail.setBackgroundResource(R.drawable.white_border_background)
                llGender.setBackgroundResource(R.drawable.white_border_background)
                llWeight.setBackgroundResource(R.drawable.white_border_background)
                llUPIId.setBackgroundResource(R.drawable.white_border_background)
                llMobileNumber.setBackgroundResource(R.drawable.white_border_background)
                llEmail.setBackgroundResource(R.drawable.white_border_background)
                lastNameLL.setBackgroundResource(R.drawable.white_border_background)
                firstNameLL.setBackgroundResource(R.drawable.white_border_background)

                if (etFirstName.text.isEmpty()) {
                    tvFirstName.isVisible = true
                    tvFirstName.text = "Please enter first name."
                    firstNameLL.setBackgroundResource(R.drawable.errordrawable)
                }
                if (etLastName.text.isEmpty()) {
                    tvLastName.isVisible = true
                    tvLastName.text = "Please enter last name."
                    lastNameLL.setBackgroundResource(R.drawable.errordrawable)
                }

                if (etEmail.text.isEmpty()) {
                    tvEmail.isVisible = true
                    tvEmail.text = "Please enter email."
                    llEmail.setBackgroundResource(R.drawable.errordrawable)
                }

                if (etMobileNumber.text.isEmpty()) {
                    tvMobileNo.isVisible = true
                    tvMobileNo.text = "Please enter mobile number."
                    llMobileNumber.setBackgroundResource(R.drawable.errordrawable)
                }

                if (etDOB.text.isEmpty()) {
                    tvDOB.isVisible = true
                    tvDOB.text = "Please enter date of birth."
                    llDOB.setBackgroundResource(R.drawable.errordrawable)
                }
                if (genderSpinner.selectedItem.equals("Select gender")) {
                    tvGender.isVisible = true
                    tvGender.text = "Please select gender."
                    llGender.setBackgroundResource(R.drawable.errordrawable)
                }









            }

            "First Name" -> {

                if (etFirstName.text.isEmpty()) {
                    tvFirstName.isVisible = true
                    tvFirstName.text = "Please enter first name."
                    firstNameLL.setBackgroundResource(R.drawable.errordrawable)
                } else {
                    tvFirstName.isVisible = false
                    tvFirstName.text = ""
                    firstNameLL.setBackgroundResource(R.drawable.white_border_background)
                }

            }

            "Last Name" -> {

                if (etLastName.text.isEmpty()) {
                    tvLastName.isVisible = true
                    tvLastName.text = "Please enter last name."
                    lastNameLL.setBackgroundResource(R.drawable.errordrawable)
                } else {
                    tvLastName.isVisible = false
                    tvLastName.text = ""
                    lastNameLL.setBackgroundResource(R.drawable.white_border_background)
                }

            }



            "Email" -> {
                if (etEmail.text.isEmpty()) {
                    tvEmail.isVisible = true

                    emailNameImage.isVisible = false
                    verifiedIconImage.isVisible = false
                    tvEmail.text = "Please enter email."
                    llEmail.setBackgroundResource(R.drawable.errordrawable)
                } else if (!etEmail.text.matches(Regex(emailPattern))) {
                    tvEmail.isVisible = true
                    emailNameImage.isVisible = false
                    verifiedIconImage.isVisible = false
                    tvEmail.text = "Please enter a valid email."
                    llEmail.setBackgroundResource(R.drawable.errordrawable)
                } else {
                    tvEmail.isVisible = false
                    tvEmail.text = ""
                    if (!isValidateForOtp){
                        emailNameImage.isVisible = true
                        verifiedIconImage.isVisible = false
                    }

                    llEmail.setBackgroundResource(R.drawable.white_border_background)
                }

            }

            "Mobile Number" -> {
                if (etMobileNumber.text.isEmpty()) {
                    tvMobileNo.isVisible = true
                    tvMobileNo.text = "Please enter mobile number."
                    mobileNameImage.isVisible = false
                    verifiedIconMobImage.isVisible = false
                    llMobileNumber.setBackgroundResource(R.drawable.errordrawable)
                } else if (etMobileNumber.text.length < 9) {
                    tvMobileNo.isVisible = true
                    tvMobileNo.text = "Please enter a valid mobile number."
                    llMobileNumber.setBackgroundResource(R.drawable.errordrawable)
                    mobileNameImage.isVisible = false
                    verifiedIconMobImage.isVisible = false
                } else {
                    tvMobileNo.isVisible = false
                    tvMobileNo.text = ""
                    if (!isValidateForOtp){
                        mobileNameImage.isVisible = true
                        verifiedIconMobImage.isVisible = false
                    }

                    llMobileNumber.setBackgroundResource(R.drawable.white_border_background)
                }

            }

            "DOB" -> {
                if (etDOB.text.isEmpty()) {
                    tvDOB.isVisible = true
                    tvDOB.text = "Please select date of birth."
                    llDOB.setBackgroundResource(R.drawable.errordrawable)
                } else {
                    tvDOB.isVisible = false
                    tvDOB.text = ""
                    llDOB.setBackgroundResource(R.drawable.white_border_background)
                }

            }

            "Gender" -> {
                if (genderSpinner.selectedItem.equals("Select gender")) {
                    tvGender.isVisible = true
                    tvGender.text = "Please select gender"
                    llGender.setBackgroundResource(R.drawable.errordrawable)
                } else {
                    tvGender.isVisible = false
                    tvGender.text = ""
                    llGender.setBackgroundResource(R.drawable.white_border_background)
                }

            }

        }



        }
    }





























