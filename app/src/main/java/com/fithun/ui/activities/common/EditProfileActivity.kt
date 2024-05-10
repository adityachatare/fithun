package com.fithun.ui.activities.common

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.fithun.R
import com.fithun.api.requests.VerifyEmailMobileOtpRequest
import com.fithun.api.requests.VerifyMobileRequest
import com.fithun.clickInterfaces.LocationDenyInterface
import com.fithun.databinding.ActivityEditProfileBinding
import com.fithun.permission.PermissionManager
import com.fithun.utils.AndroidExtension
import com.fithun.utils.DateFormat
import com.fithun.utils.FormValidations
import com.fithun.utils.ImageRotation
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.utils.displayToast
import com.fithun.utils.loadWithPlaceHolder
import com.fithun.utils.setSafeOnClickListener
import com.fithun.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity(), LocationDenyInterface {

    private lateinit var binding: ActivityEditProfileBinding

    private val viewModel: ProfileViewModel by viewModels()

    private var imageFile: File? = null
    private var photoURI: Uri? = null
    private var camera: Int = 2
    private var imagePath = ""
    private val gallery = 1
    private lateinit var image: Uri
    private var profilepic = ""
    private var apiRequestDate = ""
    var token = ""
    private var userImageUploadedProfile = false
    private var base64: String? = null
    private var filePart: ArrayList<MultipartBody.Part> = ArrayList()
    private var finalRequestDob = ""

    private var isValidateForOtp =  false
    private var isPhoneVerified =  false
    private var isEmailVerified =  false


    private fun formatDate(isoDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val outputFormat = SimpleDateFormat("yyyy-MM-dd")

        return try {
            val date = inputFormat.parse(isoDate)
            outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            // Handle parsing errors
            ""
        }
    }
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }


        PermissionManager.checkAndRequestPermissions(this@EditProfileActivity)


        binding.emailNameImage.setOnClickListener {
            binding.emailNameImage.isEnabled = false
            viewModel.signUpEmailOtpApi(binding.etEmail.text.toString())
        }

        binding.mobileNameImage.setOnClickListener{

            binding.mobileNameImage.isEnabled = false
            viewModel.signUpMobileOtpApi(binding.etMobileNumber.text.toString())
        }

        binding.etOtpVerify.addTextChangedListener(textWatchers)
        binding.etmOtpVerify.addTextChangedListener(textWatchers)

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




        binding.countrySelectorll.isEnabled = false

        binding.etFirstName.addTextChangedListener(textWatcherValidation)
        binding.etLastName.addTextChangedListener(textWatcherValidation)
        binding.etHomeAddress.addTextChangedListener(textWatcherValidation)
        binding.etEmail.addTextChangedListener(textWatcherValidation)
        binding.etOfficeAddress.addTextChangedListener(textWatcherValidation)
        binding.etUpiMobileNumber.addTextChangedListener(textWatcherValidation)
        binding.etMobileNumber.addTextChangedListener(textWatcherValidation)
        binding.etDOB.addTextChangedListener(textWatcherValidation)
        binding.etUPIId.addTextChangedListener(textWatcherValidation)
        binding.etWeight.addTextChangedListener(textWatcherValidation)

        binding.userImageSelected.setSafeOnClickListener {
            selectImage()
        }

        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month =
            calendar.get(Calendar.MONTH)  // This is 0-based, so January is 0, February is 1, and so on.
        val date = calendar.get(Calendar.DAY_OF_MONTH)
        val c = Calendar.getInstance()

        c.set(year, month, date)

         binding.llDOB.setSafeOnClickListener {

            val datePickerDialog = DatePickerDialog(
                this,
                R.style.DatePickerTheme, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    val dateFormat = String.format(
                        "%04d-%02d-%02d",
                        selectedYear,
                        selectedMonth + 1,
                        selectedDayOfMonth
                    )
                    apiRequestDate = DateFormat.dateFormatPicker1(dateFormat).toString()
                    binding.etDOB.text = DateFormat.dateFormatPicker1(dateFormat)
                    finalRequestDob = apiRequestDate
                },
                year,
                month, // Use the 0-based month value
                date
            )

            datePickerDialog.datePicker.maxDate = c.timeInMillis
            datePickerDialog.show()
        }

        binding.updateProfileClick.setSafeOnClickListener {


            if (!isPhoneVerified){
                AndroidExtension.alertBox("Please Verify your mobile number.", this)
                return@setSafeOnClickListener
            }
            if (!isEmailVerified){
                AndroidExtension.alertBox("Please Verify your email address.", this)
                return@setSafeOnClickListener
            }



            editValidation(currentField = "")
            if (binding.etFirstName.text.isNotEmpty() && binding.etLastName.text.isNotEmpty()
                && binding.etEmail.text.isNotEmpty()  &&
                binding.etMobileNumber.text.isNotEmpty()  &&
                binding.etDOB.text.isNotEmpty() && binding.etWeight.text.isNotEmpty() &&
                binding.genderSpinner.selectedItem.toString() != "Select gender"
            ) {

                viewModel.editProfileApi(
                    token = SavedPrefManager.getStringPreferences(this@EditProfileActivity, SavedPrefManager.AccessToken).toString(),
                    name = "${binding.etFirstName.text} ${binding.etLastName.text}".toRequestBody("text/plain".toMediaTypeOrNull()),
                    dob = finalRequestDob.toRequestBody("text/plain".toMediaTypeOrNull()),
                    gender = binding.genderSpinner.selectedItem.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    mobileNumber = binding.etMobileNumber.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    mobileCode = "91".toRequestBody("text/plain".toMediaTypeOrNull()),
                    email = binding.etEmail.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    homeAddress = binding.etHomeAddress.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    officeAddress = binding.etOfficeAddress.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    upiId = binding.etUPIId.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    physicallyChallenged="".toRequestBody("text/plain".toMediaTypeOrNull()),
                    shoeSize="".toRequestBody("text/plain".toMediaTypeOrNull()),
                    weight =binding.etWeight.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    mobileLinkedWithUpi = binding.etUpiMobileNumber.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    referralCode = "".toRequestBody("text/plain".toMediaTypeOrNull()),
                    file = filePart,
                )
            }
        }

        val htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
            </head>
            <body>
                <ul>
                    <li>Open Google Pay</li>
                    <br>
                    <li>In the top right, tap your photo.</li>
                    <br>
                    <li>Tap Bank account.</li>
                    <br>
                    <li>Tap the bank account whose UPI ID you want to view.</li>
                    <br>
                    <li>You'll find the associated UPI ID under "UPI IDs."</li>
                </ul>
            </body>
            </html>
        """.trimIndent()

        val formattedText = Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_COMPACT)
        binding.howToGet.text = formattedText
        viewModel.userProfile(SavedPrefManager.getStringPreferences(this@EditProfileActivity, SavedPrefManager.AccessToken).toString())


        editProfileObserver()
        profileObserver()
        sendEmailObserver()
        verifyMobileObserver()
        sendMobileObserver()
        verifySendEmailMobile()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun profileObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.profileApiResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(this@EditProfileActivity)
                        }


                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200)
                                    try {

                                        binding.etFirstName.removeTextChangedListener(textWatcherValidation)
                                        binding.etLastName.removeTextChangedListener(textWatcherValidation)
                                        binding.etHomeAddress.removeTextChangedListener(textWatcherValidation)
                                        binding.etEmail.removeTextChangedListener(textWatcherValidation)
                                        binding.etOfficeAddress.removeTextChangedListener(textWatcherValidation)
                                        binding.etUpiMobileNumber.removeTextChangedListener(textWatcherValidation)
                                        binding.etMobileNumber.removeTextChangedListener(textWatcherValidation)
                                        binding.etDOB.removeTextChangedListener(textWatcherValidation)
                                        binding.etUPIId.removeTextChangedListener(textWatcherValidation)
                                        binding.etWeight.removeTextChangedListener(textWatcherValidation)


                                        with(response.data.result){
                                            val newString = name.split(" ")

                                            if (newString.size > 1) {
                                                binding.etFirstName.setText(newString[0])
                                                binding.etLastName.setText(newString[1])
                                            } else {

                                                binding.etFirstName.setText(name)
                                            }

                                            if (kycDetails != null){
                                                binding.etUPIId.setText(kycDetails.upiId)
                                                binding.etUPIId.isEnabled = false
                                            }else{
                                                binding.etUPIId.setText(additionalDetails.upiId)
                                            }

                                            if (email.isNotEmpty()){
                                                isEmailVerified = true

                                                isValidateForOtp = true
                                                binding.emailNameImage.isVisible = false
                                                binding.verifiedIconImage.isVisible = true
                                            }
                                            if (mobileNumber.isNotEmpty()){
                                                isValidateForOtp = true
                                                isPhoneVerified = true
                                                binding.mobileNameImage.isVisible = false
                                                binding.verifiedIconMobImage.isVisible = true
                                            }

                                            binding.etEmail.setText(email)
                                            binding.etHomeAddress.setText(homeAddress)
                                            binding.etOfficeAddress.setText(officeAddress)
                                            binding.etMobileNumber.setText(mobileNumber)
                                            binding.etUpiMobileNumber.setText(mobileLinkedWithUpi)
                                            binding.etWeight.setText(additionalDetails.weight.toString())







                                            profilePic.let {
                                                binding.userProfile.loadWithPlaceHolder(
                                                    it
                                                )
                                            }

                                            when(gender.uppercase()){
                                                "MALE" -> {
                                                    binding.genderSpinner.setSelection(1)
                                                }

                                                "FEMALE" -> {
                                                    binding.genderSpinner.setSelection(2)
                                                }

                                                else -> {
                                                    binding.genderSpinner.setSelection(3)
                                                }
                                            }

                                            profilepic = profilePic
                                            if(dob.isNotEmpty()){
                                                val dob = dob
                                                val formattedDob = formatDate(dob)
                                                finalRequestDob = formattedDob
                                                binding.etDOB.text = formattedDob
                                            }

                                            if (binding.etFirstName.text.isNotEmpty() && binding.etLastName.text.isNotEmpty()
                                                && binding.etEmail.text.isNotEmpty() && binding.etUpiMobileNumber.text.isNotEmpty()
                                                && binding.etOfficeAddress.text.isNotEmpty() &&
                                                binding.etMobileNumber.text.isNotEmpty() &&
                                                binding.etHomeAddress.text.isNotEmpty() &&binding.etUPIId.text.isNotEmpty() &&
                                                binding.etDOB.text.isNotEmpty() && binding.genderSpinner.selectedItem.toString() != "Select gender"

                                            ) {
                                                binding.editProfileClickDisable.isVisible = false
                                                binding.updateProfileClick.isVisible = true
                                            } else {
                                                binding.editProfileClickDisable.isVisible = true
                                                binding.updateProfileClick.isVisible = false
                                            }


                                        }

                                        binding.etFirstName.addTextChangedListener(textWatcherValidation)
                                        binding.etLastName.addTextChangedListener(textWatcherValidation)
                                        binding.etHomeAddress.addTextChangedListener(textWatcherValidation)
                                        binding.etEmail.addTextChangedListener(textWatcherValidation)
                                        binding.etOfficeAddress.addTextChangedListener(textWatcherValidation)
                                        binding.etUpiMobileNumber.addTextChangedListener(textWatcherValidation)
                                        binding.etMobileNumber.addTextChangedListener(textWatcherValidation)
                                        binding.etDOB.addTextChangedListener(textWatcherValidation)
                                        binding.etUPIId.addTextChangedListener(textWatcherValidation)
                                        binding.etWeight.addTextChangedListener(textWatcherValidation)


                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }


                            } catch (e: Exception) {
                                
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            displayToast(response.message)
                            

                        }

                        is Resource.Empty -> {


                        }

                    }

                }
            }
        }
    }

    private fun selectImage() {
        val dialog = BottomSheetDialog(this)

        val view = layoutInflater.inflate(R.layout.choose_camera_bottom_sheet, null)

        dialog.setCancelable(true)

        val cameraButton = view.findViewById<ImageView>(R.id.choose_from_camera)
        cameraButton.setSafeOnClickListener {

            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                try {
                    imageFile = createImageFile()!!
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
                if (imageFile != null) {
                    photoURI = FileProvider.getUriForFile(
                        this,
                        "com.fithun.fileprovider",
                        imageFile!!
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, camera)
                    dialog.dismiss()
                }
            }
        }

        val galleryButton = view.findViewById<ImageView>(R.id.choose_from_gallery)
        galleryButton.setSafeOnClickListener {

            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, gallery)
            dialog.dismiss()
        }

        dialog.setContentView(view)


        dialog.show()
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )

        imagePath = image.absolutePath
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == gallery && resultCode == RESULT_OK) {
            if (data != null) {
                image = data.data!!

                val selectedImage = getImageFromUri(image)
                processSelectedImage(selectedImage)
                val path = getPathFromURI(image)


                filePart.clear()
                path?.let { createFilePart("file", it) }?.let { filePart.add(it) }
            }
        } else if (requestCode == camera && resultCode == RESULT_OK) {
            try {
                imageFile = File(imagePath)

                val selectedImage = BitmapFactory.decodeFile(imageFile!!.absolutePath)
                processSelectedImage(selectedImage)
                val finalBitmap =
                    ImageRotation.modifyOrientation(ImageRotation.getBitmap(imagePath)!!, imagePath)
                val imageUri = ImageRotation.getImageUri(applicationContext, finalBitmap!!)
                val getRealPath = ImageRotation.getRealPathFromURI(applicationContext, imageUri)
                filePart.clear()
                getRealPath?.let { createFilePart("file", it) }?.let { filePart.add(it) }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getImageFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun processSelectedImage(selectedImage: Bitmap?) {
        if (selectedImage != null) {
            binding.userProfile.setImageBitmap(selectedImage)
            profilepic = bitmapToString(selectedImage)

            userImageUploadedProfile = true

        }
    }



    fun getPathFromURI(contentUri: Uri?): String? {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = this.contentResolver.query(contentUri!!, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(column_index)
        }
        cursor.close()
        return res
    }

    private fun bitmapToString(`in`: Bitmap): String {
        var options = 50
        var base64Value = ""
        val bytes = ByteArrayOutputStream()
        `in`.compress(Bitmap.CompressFormat.JPEG, 40, bytes)
        while (bytes.toByteArray().size / 1024 > 400) {
            bytes.reset() //Reset baos is empty baos
            `in`.compress(Bitmap.CompressFormat.JPEG, options, bytes)
            options -= 10
        }
        base64 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(bytes.toByteArray())
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        base64Value = base64Value.replace("\n", "") + base64
        return base64Value
    }
    private fun createFilePart(fieldName: String, filePath: String): MultipartBody.Part {
        val file = File(filePath)
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(fieldName, file.name, requestFile)
    }


    private fun editProfileObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.EditResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(this@EditProfileActivity)
                        }


                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200) {
                                    try {
                                        AndroidExtension.alertBoxFinishActivity(response.data.responseMessage,this@EditProfileActivity,this@EditProfileActivity)

                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }


                            } catch (e: Exception) {
                                
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()


                        }


                        is Resource.Empty -> {


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
            val currentField =
                if (s === binding.etFirstName.text) "First Name" else if (s === binding.etLastName.text) "Last Name"
                else if (s === binding.etEmail.text) "Email"
                else if (s === binding.etHomeAddress.text) "Home Address"
                else if (s === binding.etOfficeAddress.text) "Office Address"
                else if (s === binding.etUpiMobileNumber.text) "MobileUPINumber"

                else if (s === binding.etMobileNumber.text) "Mobile Number" else if (s === binding.etDOB.text) "DOB"
                else if (s === binding.genderSpinner.selectedItem) "Gender"
                else if (s === binding.etUPIId.text) "UPI"
                else if (s === binding.etWeight.text) "Weight"

                else "0"

            isValidateForOtp = false


            if (s === binding.etEmail.text){
                isEmailVerified = false
            }
            if (s === binding.etMobileNumber.text){
                isPhoneVerified = false
            }




            editValidation(currentField = currentField)
            if (binding.etFirstName.text.isNotEmpty() && binding.etLastName.text.isNotEmpty()
                && binding.etEmail.text.isNotEmpty()  &&
                binding.etMobileNumber.text.isNotEmpty()  &&
                binding.etDOB.text.isNotEmpty() && binding.etWeight.text.isNotEmpty() &&
                binding.genderSpinner.selectedItem.toString() != "Select gender"


            ) {

                binding.editProfileClickDisable.isVisible = false
                binding.updateProfileClick.isVisible = true
            } else {
                binding.editProfileClickDisable.isVisible = true
                binding.updateProfileClick.isVisible = false
            }


        }

    }
    private fun editValidation(currentField: String) {
        FormValidations.editDetails(
            binding.tvFirstName,
            binding.tvDOB,
            binding.tvLastName,
            binding.etDOB,
            binding.etUpiMobileNumber,
            binding.etLastName,
            binding.etFirstName,
            binding.etHomeAddress,
            binding.etOfficeAddress,
            binding.tvEmail,
            binding.tvHomeAdd,binding.tvOfficeAdd,
            binding.tvUPIId,binding.tvOfficeAdd,
            binding.tvMobileNo,
            binding.etEmail,
            binding.llEmail,
            binding.etMobileNumber,
            binding.llMobileNumber,
            binding.llDOB,
            binding.genderSpinner,
            binding.llGender,
            binding.etUPIId,
            binding.llUPIId,

            currentField,

            binding.firstNameLL,
            binding.lastNameLL,
            binding.TvGender,
            binding.llinkedUP,
            binding.tvLinkedUPIId,
            binding.llHome,
            binding.llOffice,
            binding.etWeight,
            binding.llWeight,
            binding.tvWeight,
            binding.emailNameImage,
            binding.verifiedIconImage,
            binding.mobileNameImage,
            binding.verifiedIconMobImage,
            isValidateForOtp



        )

    }



    override fun openSettings(isForLocation: String) {
        if (isForLocation=="Location"){
            PermissionManager.requestBackgroundPermission(this@EditProfileActivity)
        }else{
            val intent = Intent()
            intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            intent.data = Uri.fromParts("package", this.packageName, null)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)



        PermissionManager.handlePermissionResult(
            this,
            retryCallback = { deniedPermission ->

                when (deniedPermission) {
                    Manifest.permission.ACCESS_FINE_LOCATION -> {
                        AndroidExtension.locationAllowPermission(
                            "Please allow location permission for all time for step count",
                            this@EditProfileActivity,
                            "Location",
                            this@EditProfileActivity
                        )
                    }
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION -> {
                        AndroidExtension.locationAllowPermission(
                            "Please allow location permission for all time in background for step count",
                            this@EditProfileActivity,
                            "Location",
                            this@EditProfileActivity
                        )
                    }
                    Manifest.permission.CAMERA -> {
                        AndroidExtension.locationAllowPermission(
                            "Please allow permission for camera",
                            this@EditProfileActivity,
                            "",
                            this@EditProfileActivity
                        )
                    }

                    Manifest.permission.READ_EXTERNAL_STORAGE -> {
                        AndroidExtension.locationAllowPermission(
                            "Please allow permission for read camera and gallery",
                            this@EditProfileActivity,
                            "",
                            this@EditProfileActivity
                        )
                    }
                    Manifest.permission.POST_NOTIFICATIONS -> {
                        AndroidExtension.locationAllowPermission(
                            "Please allow permission for notifications",
                            this@EditProfileActivity,
                            "",
                            this@EditProfileActivity
                        )
                    }
                    Manifest.permission.USE_EXACT_ALARM  -> {
                        AndroidExtension.locationAllowPermission(
                            "Please allow permission ",
                            this@EditProfileActivity,
                            "",
                            this@EditProfileActivity
                        )
                    }
                }

            },
            requestCode = requestCode,
            permissions = permissions,
            grantResults = grantResults
        )



    }


    private fun verifyMobileObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.signMobileOtpVerResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(this@EditProfileActivity)
                        }


                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200) {
                                    binding.otpmVerification.isVisible=false
                                    binding.verifiedIconMobImage.isVisible=true
                                    binding.mobileNameImage.isVisible=false
                                    isPhoneVerified=true

                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            response.message?.let {
                                AndroidExtension.alertBox(it, this@EditProfileActivity)
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
                            Progress.start(this@EditProfileActivity)
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
                                AndroidExtension.alertBox(it, this@EditProfileActivity)
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
                            Progress.start(this@EditProfileActivity)
                        }


                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200) {
                                    binding.otpVerification.isVisible=false
                                    binding.verifiedIconImage.isVisible=true
                                    binding.emailNameImage.isVisible=false
                                    isEmailVerified=true

                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            response.message?.let {
                                AndroidExtension.alertBox(it, this@EditProfileActivity)
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
                            Progress.start(this@EditProfileActivity)
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
                                AndroidExtension.alertBox(it, this@EditProfileActivity)
                            }

                        }


                        is Resource.Empty -> {

                        }


                    }


                }

            }
        }
    }


    private val textWatchers = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s != null) {
                if (s.toString() == binding.etOtpVerify.text.toString() && s.length == 6  ){
                    binding.eOtpIcon.isVisible = true
                }else if (s.toString() == binding.etmOtpVerify.text.toString() &&  s.length == 6){
                    binding.mOtpIcon.isVisible = true
                }else{
                    binding.eOtpIcon.isVisible = false
                    binding.mOtpIcon.isVisible = false
                }
            }
        }
    }




}
