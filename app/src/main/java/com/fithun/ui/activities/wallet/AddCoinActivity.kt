package com.fithun.ui.activities.wallet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.fithun.R
import com.fithun.api.responses.AddCoinList
import com.fithun.clickInterfaces.AddCoin
import com.fithun.databinding.ActivityAddCoinBinding
import com.fithun.ui.adapter.wallet.AddCoinAdapter
import com.fithun.utils.AndroidExtension
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.viewModel.RedeemViewModel
import com.fithun.viewModel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class AddCoinActivity : AppCompatActivity() , AddCoin , PaymentResultWithDataListener {

    private lateinit var binding: ActivityAddCoinBinding
    private val viewModel: RedeemViewModel by viewModels()
    private val transactionViewModel: TransactionViewModel by viewModels()

    var walletAmount = 0
    var amount = ""
    var list : ArrayList<AddCoinList> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =  ActivityAddCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations =  R.style.Fade


        transactionViewModel.transactionList(token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.AccessToken).toString(), page = 1, limit =11, transactionType = "WALLET", transactionStatus = "PAID",fromDate = "", toDate = "")


        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }

        binding.NextButton.setOnClickListener {
            if (binding.etEnterAmount.text.toString().isEmpty()){
                AndroidExtension.alertBox("Please select amount",this)
                return@setOnClickListener
            }


            viewModel.addMoneyToWalletApi(token= SavedPrefManager.getStringPreferences(this, SavedPrefManager.AccessToken).toString(), amount = amount)
        }

        setAddCoinAdapter()
        transactionResponseObserver()
        addMoneyToWalletObserver()
    }

    private fun setAddCoinAdapter() {

        list.add(AddCoinList("10", false))
        list.add(AddCoinList("50", false))
        list.add(AddCoinList("100", false))
        list.add(AddCoinList("200", false))
        list.add(AddCoinList("300", false))
        list.add(AddCoinList("400", false))
        list.add(AddCoinList("500", false))
        list.add(AddCoinList("1000", false))
        list.add(AddCoinList("2000", false))

        val adapter = AddCoinAdapter(this, list, this,0,"")
        binding.addCoinRecycler.adapter = adapter

    }

    private fun addMoneyToWalletObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.addMoneyToWalletResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(this@AddCoinActivity)
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode== 200) {
                                    startPayment(checkOutId = response.data.resultCheckOutCart.orderId)
//                                    AndroidExtension.alertBox("COMING SOON...", this@AddCoinActivity)
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            response.message?.let {
                                AndroidExtension.alertBox(it, this@AddCoinActivity)
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

        if (checkOutId == ""){
            Toast.makeText(this, "Please Try Again", Toast.LENGTH_SHORT).show()
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


    override fun addCoin(price:String) {
        binding.etEnterAmount.setText(price)
        amount = price
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        coinAddedIntent()

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
                                    binding.walletBalance.text =  "₩ ${response.data.result.wallet.totalDeposits}"
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


    private fun coinAddedIntent() {
        val data = Intent()
        data.putExtra("isAddedCoin", true)
        setResult(Activity.RESULT_OK, data)
        finishAfterTransition()
    }


}