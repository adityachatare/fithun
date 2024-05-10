package com.fithun.ui.activities.loginFlow

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignIn.*
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonObject
import com.fithun.R
import com.fithun.api.requests.LoginRequest
import com.fithun.clickInterfaces.LocationDenyInterface
import com.fithun.databinding.ActivityLoginBinding
import com.fithun.permission.PermissionManager
import com.fithun.ui.activities.common.StaticContentActivity
import com.fithun.ui.hostFragment.FragmentHostActivity
import com.fithun.utils.AndroidExtension.locationAllowPermission
import com.fithun.utils.FormValidations
import com.fithun.utils.FormValidations.MobilePattern
import com.fithun.utils.FormValidations.emailPattern
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.utils.displayToast
import com.fithun.utils.setSafeOnClickListener
import com.fithun.viewModel.LoginFlowViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), LocationDenyInterface {


    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityLoginBinding
    private var passwordNotVisible = 0
    private val viewModel: LoginFlowViewModel by viewModels()

    var device_token = ""
    var emailSaved = ""
    var passwordSaved = ""
    var isRemember = false

    var googleId = ""
    var googleFirstName = ""
    var googleLastName = ""
    var googleEmail = ""
    var googleProfilePicURL = ""
    var googleIdToken = ""


    val PERMISSION_REQUEST_SCHEDULE_EXACT_ALARM = 123


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        FirebaseApp.initializeApp(this@LoginActivity)


        val text = this.resources.getString(R.string.do_not_have_a_account_signup)
        val secondName = "<font color=\"#8E68FE\">${getString(R.string.signup)}</font>"

        binding.havingNoAccount.text =
            Html.fromHtml("$text $secondName", HtmlCompat.FROM_HTML_MODE_LEGACY)
        isRemember = SavedPrefManager.getBooleanPreferences(this, SavedPrefManager.isRemember)
        emailSaved = SavedPrefManager.getStringPreferences(this, SavedPrefManager.email).toString()
        passwordSaved =
            SavedPrefManager.getStringPreferences(this, SavedPrefManager.Password).toString()


        PermissionManager.checkAndRequestPermissions(this@LoginActivity)


        val hasExactAlarmPermission =
            PackageManager.PERMISSION_GRANTED == this.checkSelfPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !hasExactAlarmPermission) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.USE_EXACT_ALARM),
                PERMISSION_REQUEST_SCHEDULE_EXACT_ALARM
            )
        }


        //Get Firebase FCM token
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(object : OnCompleteListener<String?> {
                override fun onComplete(task: Task<String?>) {
                    if (!task.isSuccessful) {
                        return
                    }
                    val token = task.result
                    println("LoginActivity.onComplete $token")
                    if (token != null) {
                        device_token = token
                    }

                }
            })

        clicks()
        binding.etMobileNumber.addTextChangedListener(textWatcherValidation)
        binding.etPassword.addTextChangedListener(textWatcherValidation)

        loginUserObserver()
        observeSocialLoginResponse()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("650940935473-7g0g76v1bj3o75fg7bpp9g4nve6tarvl.apps.googleusercontent.com")
            .requestEmail()
            .build()

        mGoogleSignInClient = getClient(this, gso)

        binding.googleIcon.setSafeOnClickListener {
            signIn()
        }


    }

    private fun observeSocialLoginResponse() {
        lifecycleScope.launch {
            viewModel.socialLoginDataResponse.collect { response ->

                when (response) {
                    is Resource.Success -> {
                        if (response.data!!.responseCode == 200) {
                            try {
                                Progress.stop()
                                val googleFirstName = response.data.result.name
                                val googleLastName = response.data.result.userName


                                if (!response.data.result.isProfileCompleted) {
                                    SavedPrefManager.saveStringPreferences(
                                        this@LoginActivity,
                                        SavedPrefManager.AccessToken,
                                        response.data.result.token
                                    )
                                    SavedPrefManager.saveStringPreferences(
                                        this@LoginActivity,
                                        SavedPrefManager.userId,
                                        response.data.result.Id
                                    )


                                    val additionalIntent =
                                        Intent(this@LoginActivity, AdditionalDetails::class.java)
                                    additionalIntent.putExtra("firstName", googleFirstName)
                                    additionalIntent.putExtra("username", googleLastName)
                                    additionalIntent.putExtra("googleEmail", googleEmail)

                                    startActivity(additionalIntent)


                                } else {

                                    SavedPrefManager.saveStringPreferences(
                                        this@LoginActivity,
                                        SavedPrefManager.AccessToken,
                                        response.data.result.token
                                    )
                                    SavedPrefManager.saveStringPreferences(
                                        this@LoginActivity,
                                        SavedPrefManager.userId,
                                        response.data.result.Id
                                    )
                                    SavedPrefManager.savePreferenceBoolean(
                                        this@LoginActivity,
                                        SavedPrefManager.isLogin,
                                        true
                                    )

                                    val intent =
                                        Intent(this@LoginActivity, FragmentHostActivity::class.java)
                                    startActivity(intent)
                                    finishAfterTransition()

                                }
                                finishAfterTransition()

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    is Resource.Error -> {
                        Progress.stop()

                    }

                    is Resource.Loading -> {
                        Progress.start(this@LoginActivity)
                    }

                    is Resource.Empty -> {

                    }

                }

            }
        }

    }

    private fun signIn() {
        signOutGoogleAccount()
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOutGoogleAccount() {
        val googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)
        googleSignInClient.signOut()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // User signed out successfully
                } else {
                    // Handle sign-out failure
                    Log.e("Google Sign Out Error", task.exception.toString())
                }
            }
    }

    companion object {
        const val RC_SIGN_IN = 9001
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }


        if (requestCode == 123) {
            if (resultCode != RESULT_OK) {
                println("Something went wrong....")
            }
        }
    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)


            // Signed in successfully
            googleId = account?.id ?: ""
            googleFirstName = account?.givenName ?: ""
            googleLastName = account?.familyName ?: ""
            googleEmail = account?.email ?: ""
            googleProfilePicURL = account?.photoUrl.toString()
            googleIdToken = account?.idToken ?: ""

            loginWithGoogle(
                googleId,
                googleFirstName,
                googleLastName,
                googleEmail,
                googleProfilePicURL,
                device_token
            )

        } catch (e: ApiException) {

            Log.e(
                "failed code=", e.toString()
            )
        }
    }

    private fun loginWithGoogle(
        googleId: String,
        firstName: String,
        lastName: String,
        googleEmail: String,
        googleProfilePicURL: String,
        deviceToken: String
    ) {
        val request = JsonObject().apply {
            addProperty("socialId", googleId)
            addProperty("deviceType", "Android")
            addProperty("deviceToken", deviceToken)
            addProperty("givenName", firstName)
            addProperty("familyName", lastName)
            addProperty("photoUrl", googleProfilePicURL)
            addProperty("email", googleEmail)
        }
        viewModel.socialLoginApi(request)
    }

    private fun loginUserObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.loginApiResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(this@LoginActivity)
                        }


                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.statusCode == 200) {

                                    SavedPrefManager.saveStringPreferences(
                                        this@LoginActivity,
                                        SavedPrefManager.AccessToken,
                                        response.data.result.token
                                    )
                                    SavedPrefManager.saveStringPreferences(
                                        this@LoginActivity,
                                        SavedPrefManager.userId,
                                        response.data.result.id
                                    )
                                    SavedPrefManager.savePreferenceBoolean(
                                        this@LoginActivity,
                                        SavedPrefManager.isLogin,
                                        true
                                    )

                                    if (response.data.result.kycDetails != null) {
                                        SavedPrefManager.saveStringPreferences(
                                            this@LoginActivity,
                                            SavedPrefManager.UPI_ID,
                                            response.data.result.kycDetails.upiId
                                        )
                                    }
                                    startActivity(
                                        Intent(
                                            this@LoginActivity,
                                            FragmentHostActivity::class.java
                                        )
                                    )
                                    finishAfterTransition()
                                }


                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            displayToast(response.message)
                            Progress.stop()
                        }

                        is Resource.Empty -> {

                        }


                    }


                }

            }
        }

    }

    private fun clicks() {
        if (isRemember) {
            binding.etMobileNumber.setText(emailSaved)
            binding.etPassword.setText(passwordSaved)
            binding.rememberCheck.isChecked = true
            binding.loginClickDisabled.isVisible = false
            binding.loginClick.isVisible = true
        } else {
            binding.rememberCheck.isChecked = false
        }

        binding.termsAndCondition.setOnClickListener {
            val intent = Intent(this, StaticContentActivity::class.java)
            intent.putExtra("isFor", "termsAndCondition")
            startActivity(intent)
        }

        binding.privacyPolicy.setOnClickListener {
            val intent = Intent(this, StaticContentActivity::class.java)
            intent.putExtra("isFor", "privacyPolicy")
            startActivity(intent)
        }

        binding.havingNoAccount.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            intent.putExtra("isFor", "Login")
            startActivity(intent)
        }


        binding.ForgotPasswordClick.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        binding.MobilePasswordEye.setSafeOnClickListener {

            when (passwordNotVisible) {
                0 -> {
                    binding.etPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    binding.passwordImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.eye
                        )
                    )
                    passwordNotVisible = 1

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
                    passwordNotVisible = 0
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
                    passwordNotVisible = 1
                }
            }

            binding.etPassword.setSelection(binding.etPassword.text.length)
        }

        binding.loginClickDisabled.setOnClickListener {
            FormValidations.login(
                binding.etMobileNumber,
                binding.llMobileNumber,
                binding.tvMobile,
                binding.etPassword,
                binding.llPassWord,
                binding.tvPassword,
                ""
            )
        }

        binding.loginClick.setOnClickListener {
            FormValidations.login(
                binding.etMobileNumber,
                binding.llMobileNumber,
                binding.tvMobile,
                binding.etPassword,
                binding.llPassWord,
                binding.tvPassword,
                ""
            )
            if (binding.etMobileNumber.text.isNotEmpty() && (binding.etMobileNumber.text.matches(
                    Regex(emailPattern)
                ) || binding.etMobileNumber.text.matches(Regex(MobilePattern))) && binding.etPassword.text.isNotEmpty()
            ) {

                val loginRequest = LoginRequest()
                loginRequest.apply {
                    email = binding.etMobileNumber.text.toString()
                    pass = binding.etPassword.text.toString()
                    deviceTypes = "android"
                    deviceToken = device_token
                }

                viewModel.loginApi(loginRequest)

            }

            if (binding.rememberCheck.isChecked) {
                SavedPrefManager.savePreferenceBoolean(
                    this@LoginActivity,
                    SavedPrefManager.isRemember,
                    true
                )
                SavedPrefManager.saveStringPreferences(
                    this@LoginActivity,
                    SavedPrefManager.email,
                    binding.etMobileNumber.text.toString()
                )
                SavedPrefManager.saveStringPreferences(
                    this@LoginActivity,
                    SavedPrefManager.Password,
                    binding.etPassword.text.toString()
                )
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
            val currentField = if (s === binding.etMobileNumber.text) "Email" else "Password"
            FormValidations.login(
                binding.etMobileNumber, binding.llMobileNumber, binding.tvMobile,
                binding.etPassword, binding.llPassWord, binding.tvPassword, field = currentField
            )

            if (binding.etMobileNumber.text.isNotEmpty() && (binding.etMobileNumber.text.matches(
                    Regex(emailPattern)
                )
                        || binding.etMobileNumber.text.matches(Regex(MobilePattern))) && binding.etPassword.text.isNotEmpty()
            ) {

                binding.loginClickDisabled.isVisible = false
                binding.loginClick.isVisible = true


            } else {
                binding.loginClickDisabled.isVisible = true
                binding.loginClick.isVisible = false
            }
        }
    }

    override fun openSettings(isForLocation: String) {

        if (isForLocation == "Location") {
            PermissionManager.requestBackgroundPermission(this@LoginActivity)
        } else {
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
                        locationAllowPermission(
                            "Please allow location permission for all time for step count",
                            this@LoginActivity,
                            "Location",
                            this@LoginActivity
                        )
                    }

                    Manifest.permission.ACCESS_BACKGROUND_LOCATION -> {
                        locationAllowPermission(
                            "Please allow location permission for all time in background for step count",
                            this@LoginActivity,
                            "Location",
                            this@LoginActivity
                        )
                    }

                    Manifest.permission.CAMERA -> {
                        locationAllowPermission(
                            "Please allow permission for camera",
                            this@LoginActivity,
                            "",
                            this@LoginActivity
                        )
                    }

                    Manifest.permission.READ_EXTERNAL_STORAGE -> {
                        locationAllowPermission(
                            "Please allow permission for read camera and gallery",
                            this@LoginActivity,
                            "",
                            this@LoginActivity
                        )
                    }

                    Manifest.permission.POST_NOTIFICATIONS -> {
                        locationAllowPermission(
                            "Please allow permission for notifications",
                            this@LoginActivity,
                            "",
                            this@LoginActivity
                        )
                    }

                    Manifest.permission.USE_EXACT_ALARM -> {
                        locationAllowPermission(
                            "Please allow permission ",
                            this@LoginActivity,
                            "",
                            this@LoginActivity
                        )
                    }

                }

            },
            requestCode = requestCode,
            permissions = permissions,
            grantResults = grantResults
        )


    }


}