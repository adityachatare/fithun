package com.fithun.ui.activities.common

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.fithun.R
import com.fithun.databinding.ActivityViewProfileBinding
import com.fithun.ui.activities.loginFlow.ChangePasswordActivity
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.utils.displayToast
import com.fithun.utils.setTextViewValue
import com.fithun.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class ViewProfileActivity : AppCompatActivity() {

    private var isLoaded =  true

    private lateinit var binding: ActivityViewProfileBinding
    private val viewModel: ProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding =ActivityViewProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        binding.backButton.setOnClickListener {
           finishAfterTransition()
        }

        binding.changePassword.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }

        binding.updateProfileClick.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
        profileObserver()

    }

    override fun onStart() {
        super.onStart()
        viewModel.userProfile(SavedPrefManager.getStringPreferences(this@ViewProfileActivity, SavedPrefManager.AccessToken).toString())

    }

    private fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)

        return try {
            val date = inputFormat.parse(inputDate)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            e.printStackTrace()
            inputDate // Return the original date if there's an error
        }
    }
    private fun profileObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.profileApiResponseData.collect { response ->
                    when(response){

                        is Resource.Loading ->{
                            if (isLoaded){
                                Progress.start(this@ViewProfileActivity)
                            }
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200){
                                    isLoaded = false

                                    with(response.data.result){
                                        binding.name.text = name
                                        binding.email.text = email
                                        binding.mobile.text = mobileNumber
                                        binding.gender.text = gender
                                        val dob = dob
                                        val formattedDob = formatDate(dob) // Call the formatDate function to format the date
                                        binding.datebirth.text = formattedDob
                                        Glide.with(this@ViewProfileActivity).load(profilePic).placeholder(R.drawable.placeholder).thumbnail(0.25f).into(binding.userProfile)


                                        if (kycDetails != null){
                                            binding.upId.text = kycDetails.upiId
                                        }else{
                                            binding.upId.text = additionalDetails.upiId
                                        }

                                        binding.linkedUpi.setTextViewValue(mobileLinkedWithUpi)
                                        binding.homeAdd.setTextViewValue(homeAddress)
                                        binding.officeId.setTextViewValue(officeAddress)





                                    }


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
}