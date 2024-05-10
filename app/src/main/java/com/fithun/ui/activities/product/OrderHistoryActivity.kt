package com.fithun.ui.activities.product

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.fithun.R
import com.fithun.api.responses.Lists
import com.fithun.clickInterfaces.ViewOrderHistory
import com.fithun.databinding.ActivityOrderHistoryBinding
import com.fithun.ui.adapter.OrderHistoryAdapter
import com.fithun.utils.AndroidExtension
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.viewModel.OrderHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderHistoryActivity : AppCompatActivity(), ViewOrderHistory {

    private lateinit var binding: ActivityOrderHistoryBinding

    private val viewModel: OrderHistoryViewModel by viewModels()

    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag = false
    var loaderFlag = true
    var docs:ArrayList<Lists> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade



        viewModel.orderListApi(
            token = SavedPrefManager.getStringPreferences(
                this,
                SavedPrefManager.AccessToken
            ).toString(), page = page, limit = limit
        )
        myContestListApi()





        binding.nsOrderHistory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                dataLoadFlag = true
                page++
                if (page > pages) {
                } else {
                    viewModel.orderListApi(
                        token = SavedPrefManager.getStringPreferences(
                            this,
                            SavedPrefManager.AccessToken
                        ).toString(), page = page, limit = limit
                    )
                }
            }
        })

        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }
    }

    private fun myContestListApi() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.orderListResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            if (loaderFlag) {
                                Progress.start(this@OrderHistoryActivity)
                            }
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200) {
                                    loaderFlag = false
                                    if (!dataLoadFlag) {
                                        docs.clear()
                                    }

                                    pages = response.data.result.pages
                                    page = response.data.result.page
                                    if (response.data.result.docs.isNotEmpty()) {
                                        binding.nsOrderHistory.isVisible = true
                                        binding.NotFound.isVisible = false

                                        docs.addAll(response.data.result.docs)

                                        setAdapter()
                                    } else {
                                        binding.nsOrderHistory.isVisible = false
                                        binding.NotFound.isVisible = true

                                    }




                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            binding.nsOrderHistory.isVisible = false
                            binding.NotFound.isVisible = true
                            response.message?.let {
                                AndroidExtension.alertBox(it, this@OrderHistoryActivity)
                            }
                        }

                        is Resource.Empty -> {

                        }
                    }
                }
            }
        }
    }

    private fun setAdapter() {
        binding.recyclerViewOH.layoutManager = LinearLayoutManager(this)
        val adapter = OrderHistoryAdapter(this, this, docs)
        binding.recyclerViewOH.adapter = adapter

    }

    override fun viewOrder(orderId: String, orderStatus: String) {
        val intent = Intent(this, ViewOrderHistoryActivity::class.java)
        intent.putExtra("orderId", orderId)
        intent.putExtra("orderStatus", orderStatus)
        startActivity(intent)
    }


}