package com.fithun.ui.activities.common

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.fithun.R
import com.fithun.api.Constants
import com.fithun.api.requests.LogOutRequest
import com.fithun.backgroundService.Actions
import com.fithun.backgroundService.EndlessService
import com.fithun.backgroundService.ServiceState
import com.fithun.backgroundService.getServiceState
import com.fithun.clickInterfaces.CommonClick
import com.fithun.databinding.ActivitySettingsBinding
import com.fithun.ui.activities.loginFlow.LoginActivity
import com.fithun.ui.activities.product.ChooseDeliveryAddressActivity
import com.fithun.ui.activities.product.OrderHistoryActivity
import com.fithun.ui.activities.wallet.AddCoinActivity
import com.fithun.utils.AndroidExtension
import com.fithun.utils.ManageStack.isForRedeemForFund
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.utils.displayToast
import com.fithun.viewModel.ProfileViewModel
import com.fithun.viewModel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity(),CommonClick {

    private lateinit var binding: ActivitySettingsBinding

    private val viewModel: ProfileViewModel by viewModels()
    private val transactionViewModel: TransactionViewModel by viewModels()
    private var unitRequest = ""
    private var unitRequestContest = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val versionName = packageInfo.versionName

        binding.versionApp.text = "App Version - $versionName"

        clicks()
        profileObserver()
        logoutObserver()
        transactionResponseObserver()
    }

    override fun onStart() {
        super.onStart()
        transactionViewModel.transactionList(token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.AccessToken).toString(), page = 1, limit =11, transactionType = "WALLET", transactionStatus = "PAID", fromDate = "", toDate = "")
        viewModel.userProfile(SavedPrefManager.getStringPreferences(this@SettingsActivity, SavedPrefManager.AccessToken).toString())
    }

    private fun logoutObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.logoutResponseData.collect { response ->
                    when(response){

                        is Resource.Loading ->{
                            Progress.start(this@SettingsActivity)
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200) {

                                    SavedPrefManager.savePreferenceBoolean(this@SettingsActivity, SavedPrefManager.isLogin, false)
                                    SavedPrefManager.deleteAllData(this@SettingsActivity)
                                    val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error ->{
                            Progress.stop()
                            response.message?.let {
                                AndroidExtension.alertBox(it, this@SettingsActivity)
                            }

                        }

                        is Resource.Empty ->{

                        }

                    }

                }

            }
        }
    }

    private fun profileObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.profileApiResponseData.collect { response ->
                    when(response){

                        is Resource.Loading ->{}

                        is Resource.Success -> {
                            try {
                                if (response.data?.responseCode == 200){
                                    binding.name.text = response.data.result.name
                                    binding.username.text = response.data.result.userName

                                    Glide.with(this@SettingsActivity).load(response.data.result.profilePic)
                                        .thumbnail(0.5f)
                                        .placeholder(R.drawable.placeholder)
                                        .into(binding.userProfile)
                                    if (response.data.result.kycDetails != null){
                                        SavedPrefManager.saveStringPreferences(this@SettingsActivity,SavedPrefManager.UPI_ID,response.data.result.kycDetails.upiId)
                                    }

                                }
                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error ->{

                        }

                        is Resource.Empty ->{

                        }

                    }
                }

            }
        }


    }

    private fun clicks(){

        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }

        binding.MyProfileMenu.setOnClickListener {
            startActivity(Intent(this, ViewProfileActivity::class.java))

        }

        binding.RedeemCoinMenu.setOnClickListener {
            isForRedeemForFund = true
            finishAfterTransition()
        }
        binding.AddCoinMenu.setOnClickListener {
            val intent =  Intent(this, AddCoinActivity::class.java)
            startActivity(intent)
        }

        binding.AddressMenu.setOnClickListener {
            val intent =  Intent(this, ChooseDeliveryAddressActivity::class.java)
            intent.putExtra("isFrom", "Menu")
            startActivity(intent)
        }

        binding.ReferAndEarnMenu.setOnClickListener {
            startActivity(Intent(this, InviteFriendsActivity::class.java))

        }
        binding.OffersMenu.setOnClickListener {
            startActivity(Intent(this, OffersActivity::class.java))

        }
        binding.HelpAndSupportMenu.setOnClickListener {
            val intent =  Intent(this, StaticContentActivity::class.java)
            intent.putExtra("isFor", "Help")
            startActivity(intent)

        }
        binding.AboutUsMenu.setOnClickListener {
            val intent =  Intent(this, StaticContentActivity::class.java)
            intent.putExtra("isFor", "About")
            startActivity(intent)

        }
        binding.PrivacyPolicyMenu.setOnClickListener {
            val intent =  Intent(this, StaticContentActivity::class.java)
            intent.putExtra("isFor", "privacyPolicy")
            startActivity(intent)

        }

        binding.termsAndConditionMenu.setOnClickListener {
            val intent =  Intent(this, StaticContentActivity::class.java)
            intent.putExtra("isFor", "termsAndCondition")
            startActivity(intent)
        }



        binding.FaqMenu.setOnClickListener {
            val intent =  Intent(this, FaqActivity::class.java)
            intent.putExtra("isFrom", "Faqs")
            startActivity(intent)

        }

        binding.HowToPlayMenu.setOnClickListener {
            val intent =  Intent(this, FaqActivity::class.java)
            intent.putExtra("isFrom", "How To Play")
            startActivity(intent)
        }


        binding.CommunityGuidelineMenu.setOnClickListener {
            val intent =  Intent(this, StaticContentActivity::class.java)
            intent.putExtra("isFor", "Community GuideLines")
            startActivity(intent)
        }

        binding.LegalityMenu.setOnClickListener {
            val intent =  Intent(this, StaticContentActivity::class.java)
            intent.putExtra("isFor", "Legality")
            startActivity(intent)
        }

       binding.orderHistory.setOnClickListener {
           val intent =  Intent(this, OrderHistoryActivity::class.java)
           startActivity(intent)
       }

        binding.LogoutMenu.setOnClickListener {

            AndroidExtension.showDialogLogout(this,this)
        }

    }

    override fun applyClick() {

        val stepsCount = (SavedPrefManager.getFloatPreferences(applicationContext, SavedPrefManager.totalDistance) * 1.3).roundToInt()
        val distanceWalked =  SavedPrefManager.getFloatPreferences(applicationContext,SavedPrefManager.totalDistance)/1000

        val totalStepCount = formatDynamicValue(stepsCount.toString(), 2)
        val totalDistanceWalked =convertDistance(distanceWalked.toDouble())

        if (totalStepCount != "0"){
            createStep(stepsCount = totalStepCount, distanceWalked = totalDistanceWalked, unit =unitRequest)
        }

        if(SavedPrefManager.getBooleanPreferences(applicationContext, SavedPrefManager.isContestStart)){
            val stepsCountForContest = (SavedPrefManager.getFloatPreferences(applicationContext, SavedPrefManager.totalDistanceForContest) * 1.3).roundToInt()
            val distanceWalkedForContest =  SavedPrefManager.getFloatPreferences(applicationContext,SavedPrefManager.totalDistanceForContest)/1000

            val totalStepCountForContest = formatDynamicValue(stepsCountForContest.toString(), 2)
            val totalDistanceWalkedForContest =convertDistance(distanceWalkedForContest.toDouble())
            if(totalStepCountForContest != "0" && SavedPrefManager.getStringPreferences(applicationContext,SavedPrefManager.CONTEST_ID).toString().isNotEmpty()){
                createStepForContest(stepsCount = totalStepCountForContest, distanceWalked = totalDistanceWalkedForContest, unit =unitRequestContest)

            }

        }

        if (totalStepCount == "0" && !SavedPrefManager.getBooleanPreferences(applicationContext, SavedPrefManager.isContestStart)){
            val logoutRequest = LogOutRequest()
            logoutRequest.apply {
                deviceType= ""
                deviceToken =""
            }

            viewModel.LogoutApi(SavedPrefManager.getStringPreferences(this@SettingsActivity, SavedPrefManager.AccessToken).toString(),logoutRequest)

        }







    }

    private fun transactionResponseObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                transactionViewModel.transactionResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            try {
                                if (response.data?.responseCode== 200) {

                                    binding.myBalance.text = "₩ "+response.data.result.wallet.totalWinings.toInt()
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {

                        }

                        is Resource.Empty -> {
                        }

                    }

                }

            }
        }
    }

    private fun createStep(stepsCount:String,distanceWalked:String,unit:String) {
        val queue = Volley.newRequestQueue(this)
        val c = Calendar.getInstance().time

        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate: String = df.format(c)
        val request: StringRequest = object : StringRequest(
            Method.POST, Constants.SERVICE_URL,
            Response.Listener {
                if(!SavedPrefManager.getBooleanPreferences(applicationContext, SavedPrefManager.isContestStart)){
                    val logoutRequest = LogOutRequest()
                    logoutRequest.apply {
                        deviceType= ""
                        deviceToken =""
                    }

                    viewModel.LogoutApi(SavedPrefManager.getStringPreferences(this@SettingsActivity, SavedPrefManager.AccessToken).toString(),logoutRequest)

                }


            }, Response.ErrorListener { error -> // method to handle errors.


            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()

                params["stepCount"] = stepsCount
                params["distanceWalked"] = distanceWalked
                params["unit"] = unit
                params["date"] = formattedDate
                params["contestId"] = ""
                Log.d("TAG", "getParams: $params")
                return params
            }

            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["token"] = SavedPrefManager.getStringPreferences(this@SettingsActivity, SavedPrefManager.AccessToken).toString()
                return params
            }
        }
        queue.add(request)
    }
    private fun createStepForContest(stepsCount:String,distanceWalked:String,unit:String) {
        val queue = Volley.newRequestQueue(this)
        val c = Calendar.getInstance().time

        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate: String = df.format(c)
        val request: StringRequest = object : StringRequest(
            Method.POST, Constants.SERVICE_URL,
            Response.Listener {
                SavedPrefManager.stepCountDeleteOfContest(this@SettingsActivity)

                val logoutRequest = LogOutRequest()
                logoutRequest.apply {
                    deviceType= ""
                    deviceToken =""
                }

                viewModel.LogoutApi(SavedPrefManager.getStringPreferences(this@SettingsActivity, SavedPrefManager.AccessToken).toString(),logoutRequest)



            }, Response.ErrorListener { error -> // // method to handle errors.

                if (error.networkResponse.statusCode == 404){
                    val errorMessage = String(error.networkResponse.data)
                    if (errorMessage.contains("Contest is expired", ignoreCase = true)) {
                        SavedPrefManager.stepCountDeleteOfContest(this@SettingsActivity)
                    }
                }


            }
        ) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()


                params["stepCount"] = stepsCount
                params["distanceWalked"] = distanceWalked
                params["unit"] = unit
                params["date"] = formattedDate
                params["contestId"] = "${SavedPrefManager.getStringPreferences(this@SettingsActivity,SavedPrefManager.CONTEST_ID)}"

                return params
            }

            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["token"] = SavedPrefManager.getStringPreferences(this@SettingsActivity, SavedPrefManager.AccessToken).toString()
                return params
            }
        }
        queue.add(request)
    }

    private fun formatDynamicValue(value: String, decimalPlaces: Int): String {
        val parts = value.split(".")

        return if (parts.size == 2 && parts[1].length > decimalPlaces) {
            val truncatedDecimal = parts[1].substring(0, decimalPlaces)
            "${parts[0]}.${truncatedDecimal}"
        } else {
            value
        }
    }

    private fun convertDistance(value: Double): String {
        return if (value >= 1.0) {
            unitRequest = "Km"
            unitRequestContest = "Km"
            "%.1f".format(value)
        } else {
            unitRequest = "m"
            unitRequestContest = "m"
            "${(value * 1000).toInt()}"
        }
    }




}