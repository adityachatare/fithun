package com.fithun.ui.activities.contest

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.fithun.R
import com.fithun.api.Constants
import com.fithun.api.responses.LeaderBoardList
import com.fithun.api.responses.ViewCurrentPriceAndExpectedPool
import com.fithun.databinding.ActivityViewStepContestBinding
import com.fithun.ui.adapter.ContestViewPagerAdapter
import com.fithun.utils.AndroidExtension
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.utils.displayToast
import com.fithun.utils.setSafeOnClickListener
import com.fithun.utils.setTextViewValue
import com.fithun.viewModel.ContestViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class ViewStepContestActivity : AppCompatActivity() , PaymentResultWithDataListener {

    private lateinit var binding: ActivityViewStepContestBinding
    var token = ""
    var id = ""
    var endDate = ""
    var startDate = ""
    var heading = ""
    var firstPrice = ""
    private val viewModel: ContestViewModel by viewModels()
    var list : ArrayList<ViewCurrentPriceAndExpectedPool> = arrayListOf()
    var typeSpot :Boolean = false
    var leaderBoardList : List<LeaderBoardList> = listOf()

    private var selectedPageLeaderBoard = 0

    var maxSpotValue = 0
    var minSpotValue = 0
    private lateinit var startForResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        
        super.onCreate(savedInstanceState)
        binding =  ActivityViewStepContestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        intent.getStringExtra("id")?.let { id=it }
        intent.getStringExtra("startDate")?.let { startDate=it }
        intent.getStringExtra("endDate")?.let { endDate=it }
        intent.getStringExtra("heading")?.let { heading=it }
        intent.getStringExtra("firstPrice")?.let { firstPrice=it }


        binding.firstPrice.text = firstPrice

        token = SavedPrefManager.getStringPreferences(this,SavedPrefManager.AccessToken).toString()


        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val addedCoin = data?.getBooleanExtra("isPaymentDone",false)
                if (addedCoin == true){
                    val intent = Intent(this, StepContestUpdateActivity::class.java)
                    intent.putExtra("id", id)
                    intent.putExtra("endDate", endDate)
                    intent.putExtra("startDate", startDate)
                    intent.putExtra("heading", heading)
                    startActivity(intent)
                    finishAfterTransition()
                }
            }
        }




        binding.tablayout.removeAllTabs()

        binding.tablayout.addTab(binding.tablayout.newTab().setText("Winnings"))
        binding.tablayout.addTab(binding.tablayout.newTab().setText("Leaderboard"))
        binding.tablayout.tabGravity = TabLayout.GRAVITY_FILL


        binding.backButton.setSafeOnClickListener {
            finishAfterTransition()
        }

        binding.JoinNowClick.setSafeOnClickListener {
            if (maxSpotValue == minSpotValue){
                displayToast("No Spot available to join this contest")
                return@setSafeOnClickListener
            }


            viewModel.joinContestApi(token = token, contestId = id,"${Constants.PAYMENT_URl}/api/payment/success")
            lifecycleScope.launch {
                delay(2000)
            }

        }


        viewContestResponseObserver()
        cartCheckoutResponseObserver()

    }

    override fun onStart() {
        super.onStart()
        viewModel.joinContestViewApi(token=token,id=id)

    }


    private fun setTabs() {


        val adapter = ContestViewPagerAdapter(
            this,
            binding.tablayout.tabCount,
            id,
            typeSpot,
            isJoinedUser = false,

        )
        binding.viewPager.adapter = adapter


        TabLayoutMediator(binding.tablayout, binding.viewPager) {tab, position ->

            when (position) {
                0 -> tab.text = "Winnings"
                1 -> tab.text = "Leaderboard"
            }

        }.attach()
        binding.viewPager.setCurrentItem(selectedPageLeaderBoard, false)


        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                selectedPageLeaderBoard  = position
            }
        })
    }

    private fun viewContestResponseObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.joinedContestResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(this@ViewStepContestActivity)
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                response.data?.apply {
                                    if(responseCode== 200) {
                                        list.clear()
                                        result.apply {


                                            val poolValue = 0.2 * (entryFee.toInt()* maximumSpot.toInt())
                                            val finalAmount = (entryFee.toInt()* maximumSpot.toInt()) - poolValue
                                            val setValue = finalAmount.toInt()
                                            binding.maximumPoolP.text= "₩$setValue"


                                            binding.totalSpot.setTextViewValue("Total $maximumSpot Spot")
                                            binding.leftSpot.setTextViewValue("${maximumSpot.toInt() - spot.toInt()} Spot left")


                                            maxSpotValue =  maximumSpot.toInt()
                                            minSpotValue =  spot.toInt()

                                            if (maxSpotValue == minSpotValue){
                                                binding.joinButtonName.text = "Spot Full"
                                            }





                                            binding.progressValue.setMaxProgress(maximumSpot.toInt())
                                            binding.progressValue.setProgress(spot.toInt())
                                            binding.joiningFee.text = "₩$entryFee"
                                            binding.deductionAmount.text = "₩$walletDeduction"
                                            setTabs()

                                            val winningProb = if (spot.toInt() != 0){
                                                  (numberOfWinner.toDouble() / spot.toInt()) * 100
                                            }else{
                                                100
                                            }


                                            binding.winningProbability.text = "$winningProb %"


                                        }
                                    }
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }


                        is Resource.Error -> {
                            Progress.stop()
                            response.message?.let {
                                AndroidExtension.alertBox(it, this@ViewStepContestActivity)
                            }

                        }

                        is Resource.Empty -> {

                        }

                    }

                }

            }
        }
    }

    private fun cartCheckoutResponseObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.checkOutResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(this@ViewStepContestActivity)
                        }


                        is Resource.Success -> {
                            try {
                                if (response.data?.responseCode== 200) {
                                    Progress.stop()
                                    if (response.data.resultCheckOutCart.orderId.isEmpty()){
                                        val intent = Intent(this@ViewStepContestActivity, StepContestUpdateActivity::class.java)
                                        intent.putExtra("id", id)
                                        intent.putExtra("endDate", endDate)
                                        intent.putExtra("startDate", startDate)
                                        intent.putExtra("heading", heading)
                                        SavedPrefManager.saveStringPreferences(this@ViewStepContestActivity,SavedPrefManager.START_DATE,startDate)
                                        SavedPrefManager.saveStringPreferences(this@ViewStepContestActivity,SavedPrefManager.END_DATE,endDate)
                                        SavedPrefManager.saveStringPreferences(this@ViewStepContestActivity,SavedPrefManager.CONTEST_ID,id)


                                        startActivity(intent)
                                        finishAfterTransition()

                                    }else{
//                                        val intent = Intent(this@ViewStepContestActivity, PaymentWebViewActivity::class.java)
//                                        intent.putExtra("orderId",response.data.resultCheckOutCart.orderId)
//                                        intent.putExtra("fromScreen","Contest")
//                                        startForResult.launch(intent)
//                                            AndroidExtension.alertBox("COMING SOON...", this@ViewStepContestActivity)
                                        startPayment(checkOutId = response.data.resultCheckOutCart.orderId)

                                    }


                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            response.message?.let {
                                if (it == "Already paid"){
                                    val intent = Intent(this@ViewStepContestActivity, StepContestUpdateActivity::class.java)
                                    intent.putExtra("id", id)
                                    intent.putExtra("endDate", endDate)
                                    intent.putExtra("startDate", startDate)
                                    intent.putExtra("heading", heading)

                                    SavedPrefManager.saveStringPreferences(this@ViewStepContestActivity,SavedPrefManager.START_DATE,startDate)
                                    SavedPrefManager.saveStringPreferences(this@ViewStepContestActivity,SavedPrefManager.END_DATE,endDate)
                                    SavedPrefManager.saveStringPreferences(this@ViewStepContestActivity,SavedPrefManager.CONTEST_ID,id)

                                    startActivity(intent)
                                    finishAfterTransition()
                                }else{
                                    AndroidExtension.alertBox(it, this@ViewStepContestActivity)
                                }

                            }

                        }

                        is Resource.Empty -> {

                        }


                    }


                }

            }
        }
    }

    private fun startPayment(checkOutId: String) {

        if (checkOutId.isEmpty()){
            Toast.makeText( this@ViewStepContestActivity, "Please Try Again", Toast.LENGTH_SHORT).show()
            return
        }

        val co = Checkout()


        try {

            val options = JSONObject()
            options.put("name", "Fit Hun")

            options.put("image", "https://s3-for-backend.s3.amazonaws.com/ic_launcher_square.png")
//            options.put("theme.color", )
            options.put("currency", "INR")
            options.put("order_id", checkOutId)

            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)

            co.open(this, options)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        val intent = Intent(this, StepContestUpdateActivity::class.java)
        intent.putExtra("id", id)
        intent.putExtra("endDate", endDate)
        intent.putExtra("startDate", startDate)
        intent.putExtra("heading", heading)

        SavedPrefManager.saveStringPreferences(this@ViewStepContestActivity,SavedPrefManager.START_DATE,startDate)
        SavedPrefManager.saveStringPreferences(this@ViewStepContestActivity,SavedPrefManager.END_DATE,endDate)
        SavedPrefManager.saveStringPreferences(this@ViewStepContestActivity,SavedPrefManager.CONTEST_ID,id)
        startActivity(intent)
        finishAfterTransition()
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        when (p0) {
            5 -> {
                AndroidExtension.alertBox("Payment cancelled",this)
            }

            2 -> {
                AndroidExtension.alertBox("Please check your internet connection",this)
            }
        }
    }




}