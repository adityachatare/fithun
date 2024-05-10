package com.fithun.ui.activities.wallet

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.fithun.R
import com.fithun.api.responses.TransactionList
import com.fithun.clickInterfaces.WalletFilter
import com.fithun.databinding.ActivityTransactionHistoryBinding
import com.fithun.ui.adapter.wallet.MyTransactionAdapter
import com.fithun.ui.bottomSheet.HomeWinners
import com.fithun.utils.AndroidExtension
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.utils.setSafeOnClickListener
import com.fithun.viewModel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransactionHistoryActivity : AppCompatActivity(), WalletFilter {

    private lateinit var binding: ActivityTransactionHistoryBinding
    private val viewModel: TransactionViewModel by viewModels()
    var token = ""
    var transactionType = "ALL"
    var transactionList: ArrayList<TransactionList> = arrayListOf()
    var pages = 0
    var page = 1
    var limit = 20
    private var dataLoadFlag = false
    private var loaderFlag = true

    private var startDateReq = ""
    private var endDateReq = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        token = SavedPrefManager.getStringPreferences(this, SavedPrefManager.AccessToken).toString()
        viewModel.transactionList(
            token = token,
            page = page,
            limit = limit,
            transactionType = transactionType,
            transactionStatus = "PAID",
            fromDate = startDateReq,
            toDate = endDateReq
        )

        transactionResponseObserver()

        binding.filter.setSafeOnClickListener {
            val bottomSheetFragment = HomeWinners(this)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        binding.allButton.setOnClickListener {
            binding.scrollViewPost.scrollY = 0
            pages = 0
            limit = 20
            page = 1
            dataLoadFlag = false
            loaderFlag = true
            transactionType = "ALL"
            viewModel.transactionList(
                token = SavedPrefManager.getStringPreferences(
                    this,
                    SavedPrefManager.AccessToken
                ).toString(),
                page = page,
                limit = limit,
                transactionType = "ALL",
                transactionStatus = "PAID",
                fromDate = startDateReq,
                toDate = endDateReq
            )
            binding.allButton.setBackgroundResource(R.drawable.theme_background)
            binding.withdrawButton.setBackgroundResource(R.drawable.theme_border_background)
            binding.depositButton.setBackgroundResource(R.drawable.theme_border_background)
            binding.txtAll.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.txtWithdraw.setTextColor(ContextCompat.getColor(this, R.color.theme_color))
            binding.txtDeposit.setTextColor(ContextCompat.getColor(this, R.color.theme_color))
        }

        binding.withdrawButton.setOnClickListener {
            binding.scrollViewPost.scrollY = 0
            pages = 0
            limit = 20
            page = 1
            dataLoadFlag = false
            loaderFlag = true
            transactionType = "WITHDRAW"
            viewModel.transactionList(
                token = SavedPrefManager.getStringPreferences(
                    this,
                    SavedPrefManager.AccessToken
                ).toString(),
                page = page,
                limit = limit,
                transactionType = transactionType,
                transactionStatus = "PAID",
                fromDate = startDateReq,
                toDate = endDateReq
            )
            binding.withdrawButton.setBackgroundResource(R.drawable.theme_background)
            binding.allButton.setBackgroundResource(R.drawable.theme_border_background)
            binding.depositButton.setBackgroundResource(R.drawable.theme_border_background)
            binding.txtAll.setTextColor(ContextCompat.getColor(this, R.color.theme_color))
            binding.txtWithdraw.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.txtDeposit.setTextColor(ContextCompat.getColor(this, R.color.theme_color))
        }

        binding.depositButton.setOnClickListener {
            binding.scrollViewPost.scrollY = 0
            pages = 0
            limit = 20
            page = 1
            dataLoadFlag = false
            loaderFlag = true
            transactionType = "DEPOSIT"
            viewModel.transactionList(
                token = SavedPrefManager.getStringPreferences(
                    this,
                    SavedPrefManager.AccessToken
                ).toString(),
                page = page,
                limit = limit,
                transactionType = transactionType,
                transactionStatus = "PAID",
                fromDate = startDateReq,
                toDate = endDateReq
            )
            binding.depositButton.setBackgroundResource(R.drawable.theme_background)
            binding.allButton.setBackgroundResource(R.drawable.theme_border_background)
            binding.withdrawButton.setBackgroundResource(R.drawable.theme_border_background)
            binding.txtAll.setTextColor(ContextCompat.getColor(this, R.color.theme_color))
            binding.txtWithdraw.setTextColor(ContextCompat.getColor(this, R.color.theme_color))
            binding.txtDeposit.setTextColor(ContextCompat.getColor(this, R.color.white))
        }

        binding.scrollViewPost.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                dataLoadFlag = true

                binding.ProgressBarScroll.isVisible = true
                page++
                if (page > pages) {
                    binding.ProgressBarScroll.isVisible = false
                } else {
                    viewModel.transactionList(
                        token = token,
                        page = page,
                        limit = limit,
                        transactionType = transactionType,
                        transactionStatus = "PAID",
                        fromDate = startDateReq,
                        toDate = endDateReq
                    )
                }
            }
        })



        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }
    }

    private fun transactionResponseObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.transactionResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            binding.ProgressBarScroll.isVisible = false
                            if (loaderFlag) {
                                Progress.start(this@TransactionHistoryActivity)
                            }
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                binding.ProgressBarScroll.isVisible = false
                                if (response.data?.responseCode == 200) {

                                    loaderFlag = false
                                    if (!dataLoadFlag) {
                                        transactionList.clear()
                                    }

                                    pages = response.data.result.pages
                                    page = response.data.result.page

                                    transactionList.addAll(response.data.result.docs)

                                    setMyTransactionAdapter()


                                    if (response.data.result.docs.isNotEmpty()) {
                                        binding.mainLayout.isVisible = true
                                        binding.NotFound.isVisible = false


                                    } else {
                                        binding.mainLayout.isVisible = false
                                        binding.NotFound.isVisible = true

                                    }


                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            binding.mainLayout.isVisible = false
                            binding.NotFound.isVisible = true
                            binding.ProgressBarScroll.isVisible = false
                            response.message?.let {
                                AndroidExtension.alertBox(it, this@TransactionHistoryActivity)
                            }
                        }

                        is Resource.Empty -> {
                        }
                    }
                }
            }
        }
    }

    private fun setMyTransactionAdapter() {
        binding.transactionHistory.layoutManager = LinearLayoutManager(this)
        val adapter = MyTransactionAdapter(this, transactionList)
        binding.transactionHistory.adapter = adapter
    }

    override fun filterWalletClick(startDate: String, endDate: String, type: String) {
        page = 1
        limit = 20
        dataLoadFlag = false
        loaderFlag = true
        startDateReq = startDate
        endDateReq = endDate
        val transactionType = if (type == "Choose" || type == "All") {
            "ALL"
        } else {
            type.uppercase()
        }

        when (transactionType) {
            "ALL" -> {
                binding.allButton.setBackgroundResource(R.drawable.theme_background)
                binding.withdrawButton.setBackgroundResource(R.drawable.theme_border_background)
                binding.depositButton.setBackgroundResource(R.drawable.theme_border_background)
                binding.txtAll.setTextColor(ContextCompat.getColor(this, R.color.white))
                binding.txtWithdraw.setTextColor(ContextCompat.getColor(this, R.color.theme_color))
                binding.txtDeposit.setTextColor(ContextCompat.getColor(this, R.color.theme_color))
            }

            "WITHDRAW" -> {
                binding.withdrawButton.setBackgroundResource(R.drawable.theme_background)
                binding.allButton.setBackgroundResource(R.drawable.theme_border_background)
                binding.depositButton.setBackgroundResource(R.drawable.theme_border_background)
                binding.txtAll.setTextColor(ContextCompat.getColor(this, R.color.theme_color))
                binding.txtWithdraw.setTextColor(ContextCompat.getColor(this, R.color.white))
                binding.txtDeposit.setTextColor(ContextCompat.getColor(this, R.color.theme_color))
            }

            "DEPOSIT" -> {
                binding.depositButton.setBackgroundResource(R.drawable.theme_background)
                binding.allButton.setBackgroundResource(R.drawable.theme_border_background)
                binding.withdrawButton.setBackgroundResource(R.drawable.theme_border_background)
                binding.txtAll.setTextColor(ContextCompat.getColor(this, R.color.theme_color))
                binding.txtWithdraw.setTextColor(ContextCompat.getColor(this, R.color.theme_color))
                binding.txtDeposit.setTextColor(ContextCompat.getColor(this, R.color.white))
            }

        }




        viewModel.transactionList(
            token = SavedPrefManager.getStringPreferences(
                this,
                SavedPrefManager.AccessToken
            ).toString(),
            page = page,
            limit = limit,
            transactionType = transactionType,
            transactionStatus = "PAID",
            fromDate = startDateReq,
            toDate = endDateReq
        )

    }
}