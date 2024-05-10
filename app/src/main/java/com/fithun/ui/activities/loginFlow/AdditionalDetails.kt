package com.fithun.ui.activities.loginFlow

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.fithun.R
import com.fithun.databinding.ActivityAdditionalDetailsBinding
import com.fithun.ui.hostFragment.FragmentHostActivity
import com.fithun.utils.AndroidExtension
import com.fithun.utils.DateFormat
import com.fithun.utils.DialogUtils
import com.fithun.utils.FormValidations
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.utils.displayToast
import com.fithun.utils.setSafeOnClickListener
import com.fithun.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.Calendar
import java.util.Timer

@AndroidEntryPoint
class AdditionalDetails : AppCompatActivity() {


    private lateinit var binding: ActivityAdditionalDetailsBinding

    var isFor = ""
    private val viewModel: ProfileViewModel by viewModels()
    private var timer: Timer? = null
    var apiRequestDate = ""
    private var finalRequestDob = ""
    private var isBelow18 = false

    private var filePart: ArrayList<MultipartBody.Part> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdditionalDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        intent.getStringExtra("isFor")?.let { isFor = it }
        clicks()
        val firstName = intent.getStringExtra("firstName")
        val lastName = intent.getStringExtra("username")
        val email = intent.getStringExtra("googleEmail")
        binding.etName.setText(firstName)
        binding.etuserName.setText(lastName)
        binding.etEmail.setText(email)



        binding.signUpClick.setOnClickListener {


            viewModel.editProfileApi(
                token = SavedPrefManager.getStringPreferences(this@AdditionalDetails, SavedPrefManager.AccessToken).toString(),
                name = binding.etName.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                dob =  finalRequestDob.toRequestBody("text/plain".toMediaTypeOrNull()),
                gender = binding.genderSpinner.selectedItem.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                mobileNumber = binding.etMobileNumber.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                mobileCode = "+91".toRequestBody("text/plain".toMediaTypeOrNull()),
                email = binding.etEmail.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                homeAddress = "".toRequestBody("text/plain".toMediaTypeOrNull()),
                officeAddress ="".toRequestBody("text/plain".toMediaTypeOrNull()),
                upiId = binding.etUPIId.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                mobileLinkedWithUpi = "".toRequestBody("text/plain".toMediaTypeOrNull()),
                physicallyChallenged =    binding.PhysicallyChallenged.selectedItem.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                shoeSize = binding.shoeSizeSpinner.selectedItem.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                weight =binding.etWeight.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                referralCode =binding.etRefralCode.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                file = filePart
            )
        }
        binding.etuserName.tag = false
        binding.etuserName.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            binding.etuserName.tag = hasFocus
        }

        editProfileObserver()

    }

    private fun editProfileObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.EditResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(this@AdditionalDetails)
                        }
                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200) {
                                    try {
                                        SavedPrefManager.savePreferenceBoolean(this@AdditionalDetails,SavedPrefManager.isLogin,true)
                                        val intent = Intent(this@AdditionalDetails, FragmentHostActivity::class.java)
                                        startActivity(intent)
                                        finishAfterTransition()

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
                            response.message?.let {
                                AndroidExtension.alertBox(it, this@AdditionalDetails)
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
                    finalRequestDob = dateFormat
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

        binding.SizeChart.setOnClickListener {
            showSizeChart()
        }


        binding.genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val item = parent.getItemAtPosition(pos)
                if (item != "Select gender") {
                    binding.llGender.setBackgroundResource(R.drawable.white_border_background)
                    binding.TvGender.isVisible = false
                    binding.TvableToWalk.isVisible = false
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        binding.shoeSizeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
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


        binding.YesWalk.visibility = View.GONE
        binding.NoWalk.visibility = View.GONE
        binding.ablewalk.visibility=View.GONE

        binding.YesWalk.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.NoWalk.isChecked = false
            }
        }

        binding.NoWalk.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.YesWalk.isChecked = false
                binding.TvableToWalk.isVisible = false
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

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.etName.addTextChangedListener(textWatcherValidation)

        binding.etEmail.addTextChangedListener(textWatcherValidation)
        binding.etMobileNumber.addTextChangedListener(textWatcherValidation)
        binding.etDOB.addTextChangedListener(textWatcherValidation)
        binding.etWeight.addTextChangedListener(textWatcherValidation)


        binding.signUpClickDisable.setSafeOnClickListener {

            additionalValidation(currentField = "")

        }

        binding.signUpClick.setSafeOnClickListener {

            additionalValidation(currentField = "")
            if (binding.etName.text.isNotEmpty() &&  binding.etuserName.text.isNotEmpty()
                && binding.etEmail.text.isNotEmpty() && binding.etEmail.text.toString()
                    .matches(Regex(FormValidations.emailPattern)) &&

                binding.etMobileNumber.text.isNotEmpty() && binding.etMobileNumber.text.length == 10 &&
                binding.etDOB.text.isNotEmpty() && !binding.genderSpinner.selectedItem.equals("Select gender")
                && !binding.shoeSizeSpinner.selectedItem.equals("Select your shoe size")  &&
                !binding.PhysicallyChallenged.selectedItem.equals("Select physically Challenged")
                && (binding.YesWalk.isChecked || binding.NoWalk.isChecked)

            )
            {


            } }
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
                if (s === binding.etName.text) "Name"
                else if (s === binding.etuserName.text) "User Name" else if (s === binding.etEmail.text) "Email"
                else if (s === binding.etMobileNumber.text) "Mobile Number" else if (s === binding.etDOB.text) "DOB"
                else if (s === binding.genderSpinner.selectedItem) "Gender" else if (s === binding.shoeSizeSpinner.selectedItem) "Shoe Size"
                else if (s === binding.etWeight.text) "Weight"
                else if (s === binding.PhysicallyChallenged.selectedItem) "Physically Challenged" else "0"

            additionalValidation(currentField = currentField)
            if (binding.etName.text.isNotEmpty() &&  binding.etuserName.text.isNotEmpty()
                && binding.etEmail.text.isNotEmpty() &&

                binding.etMobileNumber.text.isNotEmpty() && binding.etMobileNumber.text.length == 10 &&
                binding.etDOB.text.isNotEmpty() && binding.genderSpinner.selectedItem.toString() != "Select gender" &&
                binding.shoeSizeSpinner.selectedItem.toString() != "Select your shoe size" &&
                binding.PhysicallyChallenged.selectedItem.toString() != "Select physically Challenged"

            ) {
                binding.signUpClickDisable.isVisible = false
                binding.signUpClick.isVisible = true
            } else {
                binding.signUpClickDisable.isVisible = true
                binding.signUpClick.isVisible = false
            }
        }
    }

    private fun additionalValidation(currentField: String) {
        FormValidations.additionalDetails(
            binding.etName,
            binding.llName,
            binding.tvName,
            binding.etuserName,
            binding.llUserName,
            binding.tvUserName,
            binding.etEmail,
            binding.llEmail,
            binding.tvEmail,
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
            binding.etUPIId,
            binding.llUPIId,
            binding.tvUPIId,
            binding.PhysicallyChallenged,
            binding.llPhysicallyChallenged,
            binding.TvPhysicallyChallenged,
            currentField,
            binding.YesWalk,
            binding.NoWalk,
            binding.TvableToWalk,

            )
    }


    private fun isAbove18(date: Calendar): Boolean {
        val currentDate = Calendar.getInstance()
        currentDate.add(Calendar.YEAR, -18)
        return date.before(currentDate)
    }


}
