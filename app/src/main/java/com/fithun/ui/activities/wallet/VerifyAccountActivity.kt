package com.fithun.ui.activities.wallet

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fithun.R
import com.fithun.api.Constants
import com.fithun.databinding.ActivityVerifyAccountBinding
import com.fithun.ui.bottomSheet.WalletDialog
import com.fithun.utils.AndroidExtension
import com.fithun.utils.DateFormat
import com.fithun.utils.DialogUtils
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.utils.setSafeOnClickListener
import com.fithun.viewModel.HomeFlowViewModel
import com.fithun.viewModel.KycViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.Charset
import java.util.Calendar

@AndroidEntryPoint
class VerifyAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyAccountBinding
    private val viewModel: KycViewModel by viewModels()
    private val viewModel1: HomeFlowViewModel by viewModels()
    var upiId:String? = ""
    var name = ""
    var pan = ""
    var upiIdApi = ""
    private lateinit var upiDialog: Dialog
    private lateinit var panDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        viewModel1.userProfile(SavedPrefManager.getStringPreferences(this, SavedPrefManager.AccessToken).toString())
        profileObserver()
        viewModel.getKycApi(SavedPrefManager.getStringPreferences(this, SavedPrefManager.AccessToken).toString())
        getKycApi()

        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }

        binding.verifyPAn.setOnClickListener {
            openPopUpForPanVerification()
        }
        binding.upiIdClick.setOnClickListener {
            openDialogForUpiVerification()
        }
        binding.imgUpiId.setOnClickListener {
            supportFragmentManager.let {
                WalletDialog("Great news!", "You've to verify your UPI (Unified Payments Interface) account and then all your transactions will be securely processed through this verified UPI account. Enjoy seamless and convenient payments with confidence!").show(it, "MyCustomFragment")
            }
        }
    }


    @SuppressLint("MissingInflatedId")
    private fun openPopUpForPanVerification() {
        try {
            val binding = LayoutInflater.from(this).inflate(R.layout.pan_verify_popup, null)
            panDialog = DialogUtils().createDialog(this, binding.rootView, 0)!!

            val dialogBackButton = binding.findViewById<ImageView>(R.id.panBackButton)
            val panNumberEt = binding.findViewById<EditText>(R.id.panNumberEt)
            val nameOnPen = binding.findViewById<EditText>(R.id.nameOnPen)
            val dobll = binding.findViewById<LinearLayout>(R.id.dobll)
            val dobet = binding.findViewById<TextView>(R.id.dobet)
            val nextButtonPan = binding.findViewById<RelativeLayout>(R.id.nextButtonPan)

            dialogBackButton.setOnClickListener {
                panDialog.dismiss()
            }

            nextButtonPan.setOnClickListener {
                panVerification(dobet.text.toString(), nameOnPen.text.toString(), panNumberEt.text.toString())
                panDialog.dismiss()
            }
            dobll.setSafeOnClickListener {
                val calendar: Calendar = Calendar.getInstance()

                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)  // This is 0-based, so January is 0, February is 1, and so on.
                val date = calendar.get(Calendar.DAY_OF_MONTH)

                val c = Calendar.getInstance()

                val datePickerDialog = DatePickerDialog(
                    this,
                    R.style.DatePickerTheme, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                        val dateFormat = String.format("%04d-%02d-%02d",selectedYear, selectedMonth + 1, selectedDayOfMonth)
                       // apiRequestDate = dateFormat
                        dobet.setText(DateFormat.dateFormatPicker1(dateFormat))
                    },
                    year,
                    month, // Use the 0-based month value
                    date
                )

                datePickerDialog.datePicker.maxDate = c.timeInMillis
                datePickerDialog.show()
            }

            panDialog.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    @SuppressLint("MissingInflatedId")
    private fun openDialogForUpiVerification() {
        try {
            val binding = LayoutInflater.from(this).inflate(R.layout.upi_verify_popup, null)
            upiDialog = DialogUtils().createDialog(this, binding.rootView, 0)!!

            val dialogBackButton = binding.findViewById<ImageView>(R.id.BackButton)
            val nextButton = binding.findViewById<RelativeLayout>(R.id.nextButton)
            val etUpiNumber = binding.findViewById<EditText>(R.id.etUpiNumber)

            if(!upiId.isNullOrEmpty()){
                etUpiNumber.setText(upiId)
            }

            nextButton.setOnClickListener {
                upiVerification(etUpiNumber.text.toString())
                upiDialog.cancel()
            }

            dialogBackButton.setOnClickListener { upiDialog.dismiss() }

            upiDialog.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun profileObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel1.profileApiResponseData.collect { response ->
                    when(response){

                        is Resource.Loading ->{}

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200){


                                    with(response.data.result){
                                        binding.mobileNumber.text = "${response.data.result.mobileCode} ${response.data!!.result.mobileNumber}"
                                        binding.emailAddress.text = response.data.result.email

                                        if (kycDetails != null){
                                            binding.txtUpiId.text = kycDetails.upiId
                                            upiId = kycDetails.upiId
                                        }else{
                                            binding.txtUpiId.text = additionalDetails.upiId
                                            upiId = additionalDetails.upiId
                                        }




                                        name = response.data.result.name

                                    }

                                }

                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error ->{
                            Progress.stop()
                        }

                        is Resource.Empty ->{

                        }
                    }
                }
            }
        }
    }


    private fun upiVerification(data: String) {
        Progress.start(this@VerifyAccountActivity)
        val queue = Volley.newRequestQueue(this)

        val request: StringRequest = object : StringRequest(
            Method.POST, Constants.UPI_VERIFICATION,
            Response.Listener<String?> { response ->
                Progress.stop()
                Toast.makeText(this, "Verified Successfully", Toast.LENGTH_LONG).show()
                viewModel.createKycApi(token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.AccessToken).toString(), name = name, pan,"",data)
                createKycApi()
            }, Response.ErrorListener { error -> // method to handle errors.
                Progress.stop()
                Toast.makeText(this, "Verification Failed", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("upi_id", data)
                val requestBody = jsonBody.toString()
                return requestBody.toByteArray(Charset.forName("UTF-8"))
            }

            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmcmVzaCI6ZmFsc2UsImlhdCI6MTcwMTA3ODY4MiwianRpIjoiOWNjYWYxYjgtYWE5OC00ZDg1LTg3ODYtNzBkMTg0NTA0OGFlIiwidHlwZSI6ImFjY2VzcyIsImlkZW50aXR5IjoiZGV2LmNvbnNvbGVfMnhmNXVpZW1mb3FqNDJuc3U3ZmlrZGdlc2Y5QHN1cmVwYXNzLmlvIiwibmJmIjoxNzAxMDc4NjgyLCJleHAiOjIwMTY0Mzg2ODIsInVzZXJfY2xhaW1zIjp7InNjb3BlcyI6WyJ3YWxsZXQiXX19.7Lt-n_qkRYlyhdwSHK8z0Qo6JJaJ1t_2f2itpWRISH4"
                return params
            }
        }
        queue.add(request)
    }

    private fun panVerification(dob:String, username:String, panCardNumber:String) {
        val queue = Volley.newRequestQueue(this)

        val request: StringRequest = object : StringRequest(
            Method.POST, Constants.PEN_VERIFICATION,
            Response.Listener<String?> { response ->
                panDialog.dismiss()
                Toast.makeText(this, "Verified Successfully", Toast.LENGTH_LONG).show()
                viewModel.createKycApi(token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.AccessToken).toString(), name = name, panCardNumber,dob,upiIdApi)
                createKycApi()
            }, Response.ErrorListener { error -> // method to handle errors.
                println("VerifyAccountActivity.panVerification ${error}")
                Toast.makeText(this, "Verification Failed", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getBody(): ByteArray {
                val jsonBody = JSONObject()
                jsonBody.put("id_number", panCardNumber)
                jsonBody.put("full_name", username)
                jsonBody.put("dob", dob)
                val requestBody = jsonBody.toString()
                return requestBody.toByteArray(Charset.forName("UTF-8"))
            }

            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmcmVzaCI6ZmFsc2UsImlhdCI6MTcwMTA3ODY4MiwianRpIjoiOWNjYWYxYjgtYWE5OC00ZDg1LTg3ODYtNzBkMTg0NTA0OGFlIiwidHlwZSI6ImFjY2VzcyIsImlkZW50aXR5IjoiZGV2LmNvbnNvbGVfMnhmNXVpZW1mb3FqNDJuc3U3ZmlrZGdlc2Y5QHN1cmVwYXNzLmlvIiwibmJmIjoxNzAxMDc4NjgyLCJleHAiOjIwMTY0Mzg2ODIsInVzZXJfY2xhaW1zIjp7InNjb3BlcyI6WyJ3YWxsZXQiXX19.7Lt-n_qkRYlyhdwSHK8z0Qo6JJaJ1t_2f2itpWRISH4"
                return params
            }
        }
        queue.add(request)
    }

    private fun createKycApi() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.createKycResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(this@VerifyAccountActivity)
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data!!.statusCode== 200) {
                                    viewModel.getKycApi(SavedPrefManager.getStringPreferences(this@VerifyAccountActivity, SavedPrefManager.AccessToken).toString())
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            response.message?.let {
                                AndroidExtension.alertBox(it, this@VerifyAccountActivity)
                            }
                        }

                        is Resource.Empty -> {

                        }
                    }
                }
            }
        }
    }

    private fun getKycApi() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getKycApiResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(this@VerifyAccountActivity)
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data!!.responseCode== 200) {
                                   if(response.data.result.upiId.isNotEmpty()){
                                       upiIdApi = response.data.result.upiId
                                       SavedPrefManager.saveStringPreferences(this@VerifyAccountActivity,SavedPrefManager.UPI_ID,response.data.result.upiId)

                                       binding.upiIdClick.setBackgroundResource(R.drawable.green_background)
                                       binding.upiIdClick.isClickable = false
                                       binding.upiIdClick.isEnabled = false
                                       binding.upiIdClick.text = "Verified"
                                       binding.txtUpiId.text = response.data.result.upiId
                                   }
                                    if(response.data.result.panNumber.isNotEmpty()){
                                        pan = response.data.result.panNumber
                                        binding.verifyPAn.setBackgroundResource(R.drawable.green_background)
                                        binding.verifyPAn.isClickable = false
                                        binding.verifyPAn.isEnabled = false
                                        binding.verifyPAn.text = "Verified"
                                        binding.txtPanCard.text = response.data.result.panNumber
                                    }else{
                                        binding.txtPanCard.text = "NA"
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
}