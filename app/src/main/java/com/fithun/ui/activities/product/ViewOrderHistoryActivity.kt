package com.fithun.ui.activities.product

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.fithun.R
import com.fithun.api.responses.ShipmentTrackActivities
import com.fithun.api.responses.ViewProducts
import com.fithun.clickInterfaces.ClickForTracking
import com.fithun.databinding.ActivityViewOrderHistoryBinding
import com.fithun.ui.adapter.VIewOrderHistoryAdapter
import com.fithun.ui.hostFragment.FragmentHostActivity
import com.fithun.utils.AndroidExtension
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.utils.loadImageResource
import com.fithun.utils.setSafeOnClickListener
import com.fithun.viewModel.OrderHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewOrderHistoryActivity : AppCompatActivity(),ClickForTracking {

    private lateinit var binding: ActivityViewOrderHistoryBinding
    private val viewModel: OrderHistoryViewModel by viewModels()
    var orderId:String? = ""
    var orderStatus = ""

    var itemList: ArrayList<ShipmentTrackActivities>  = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_order_history)
        binding =  ActivityViewOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        intent.getStringExtra("orderId").let { orderId = it }
        intent?.getStringExtra("orderStatus")?.let { orderStatus = it }
        orderId?.let { viewModel.viewOrderApi(token= SavedPrefManager.getStringPreferences(this, SavedPrefManager.AccessToken).toString(), orderId = it) }
        viewOrderDetailsApi()
        binding.progressValue.setProgressColor(ContextCompat.getColor(this, R.color.theme_color))

        binding.backButton.setOnClickListener{
           finishAfterTransition()
        }

        if (orderStatus.lowercase() =="delivered"){
            binding.img.loadImageResource(R.drawable.filled_icon_tracking)
            binding.deliverIcon.loadImageResource(R.drawable.filled_icon_tracking)
            binding.progressValue.setProgress(100)
        }else if (orderStatus.lowercase() =="cancel"){
            binding.orderStatusType.text = "Cancel"
            binding.img.loadImageResource(R.drawable.filled_icon_tracking)
            binding.deliverIcon.loadImageResource(R.drawable.filled_icon_tracking)
            binding.progressValue.setProgress(100)
        }else{
            binding.img.loadImageResource(R.drawable.filled_icon_tracking)
            binding.deliverIcon.loadImageResource(R.drawable.ic_baseline_greycircle_24)
            binding.progressValue.setProgress(50)
        }


        binding.seeAllUpdate.setSafeOnClickListener {
            startActivity(Intent(this,ProductTrackingActivity::class.java))
        }

        binding.proceedShopping.setOnClickListener {
            val intent = Intent(this, FragmentHostActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    @SuppressLint("RepeatOnLifecycleWrongUsage")
    private fun viewOrderDetailsApi() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.viewOrderResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                                Progress.start(this@ViewOrderHistoryActivity)
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode== 200) {

                                    response.data.result.apply {
                                        setAdapter(products)
                                        binding.addressTitle.text = addressId.firstName+ " "+addressId.lastName
                                        binding.name.text = addressId.area
                                        binding.zipcode.text = addressId.zipCode
                                        binding.phone.text = addressId.zipCode
                                        binding.orderId.text = orderId
                                        binding.phone.text = userId.mobileNumber
                                        binding.address.text = addressId.area+ " "+addressId.city+  " "+ addressId.state+ " "+ addressId.country+ " "+ addressId.zipCode


                                        binding.subTotalAmount.text = "Rs $total"
                                        binding.discountAmount.text = "Rs $totalPayableAmount"


                                        val totalViewAmount =  total - totalPayableAmount.toInt()

                                        binding.totalAmount.text = "Rs $totalViewAmount"
                                        binding.shippingTotalAmount.text = "Rs $deliveryFee"



                                        val grandAmount = totalViewAmount + deliveryFee

                                        binding.grandTotalAmount.text = "Rs $grandAmount"


                                    }


                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            response.message?.let {
                                AndroidExtension.alertBox(it, this@ViewOrderHistoryActivity)
                            }
                        }

                        is Resource.Empty -> {

                        }
                    }
                }
            }
        }
    }

    private fun setAdapter(list:ArrayList<ViewProducts>) {

        binding.recyclerViewOrderview.layoutManager = LinearLayoutManager(this)
        val adapter = VIewOrderHistoryAdapter(this, list,this)
        binding.recyclerViewOrderview.adapter = adapter

    }

    override fun trackOrder(awb: String) {
        val intent =  Intent(this@ViewOrderHistoryActivity,ProductTrackingActivity::class.java)
        intent.putExtra("awb",awb)
        startActivity(intent)


    }




}