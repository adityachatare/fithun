package com.fithun.ui.activities.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.fithun.R
import com.fithun.api.responses.ShipmentTrackActivities
import com.fithun.databinding.ActivityProductTrackingBinding
import com.fithun.ui.adapter.LastOrderAdapter
import com.fithun.utils.AndroidExtension
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.viewModel.OrderHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductTrackingActivity : AppCompatActivity() {


    private lateinit var binding: ActivityProductTrackingBinding
    private val viewModel: OrderHistoryViewModel by viewModels()
    private var shipmentId = ""
    var itemList: ArrayList<ShipmentTrackActivities>  = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityProductTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations =  R.style.Fade
        intent.getStringExtra("awb")?.let { shipmentId = it }


        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }


        viewModel.trackOrderApi(shipmentId=shipmentId)

        trackOrderApiObserver()
    }


    private fun trackOrderApiObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.trackOrderResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(this@ProductTrackingActivity)
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode== 200) {
                                    itemList =  response.data.result
                                    setupRecycler(itemList)

                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            response.message?.let {
                                AndroidExtension.alertBox(it, this@ProductTrackingActivity)
                            }
                        }

                        is Resource.Empty -> {

                        }
                    }
                }
            }
        }
    }
    

    private fun setupRecycler(orderTracking: ArrayList<ShipmentTrackActivities>) {
        val startingIndex = 0

        for (i in 0 until  orderTracking.size) {
            if (i == startingIndex) {
                orderTracking[i].progressValue = 50
            } else if (orderTracking[i].trackStatus) {
                orderTracking[i - 1].progressValue = 100
                orderTracking[i].progressValue = 50
            } else {

            }
        }

        binding.orderTracking.layoutManager = LinearLayoutManager(this)
        val adapter = LastOrderAdapter(this, orderTracking)
        binding.orderTracking.adapter = adapter
    }



}