package com.fithun.ui.bottomTab

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.fithun.R
import com.fithun.api.responses.AddCoinList
import com.fithun.clickInterfaces.AddCoin
import com.fithun.databinding.FragmentFundTabBinding
import com.fithun.ui.activities.wallet.RedeemCoinActivity
import com.fithun.ui.adapter.BannerAdapter
import com.fithun.ui.adapter.wallet.AddCoinAdapter
import com.fithun.uiModalClass.BannerImages
import com.fithun.utils.AndroidExtension
import com.fithun.utils.ManageStack
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.utils.setSafeOnClickListener
import com.fithun.viewModel.HomeFlowViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FundTabFragment : Fragment() , AddCoin {

    private var _binding: FragmentFundTabBinding? =  null

    private val viewModel: HomeFlowViewModel by viewModels()

    private var walletAmount = 0
    private var isRedeemRequestToday = false
    var amount = 0
    private val binding get() = _binding!!



    private val sliderHandler: Handler = Handler()
    var list : ArrayList<AddCoinList> = arrayListOf()


    companion object{
        @JvmStatic
        fun newInstance() = FundTabFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFundTabBinding.inflate(layoutInflater, container, false)
        viewModel.stepsDetailsApi(token= SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.AccessToken).toString())
        addMoneyToWalletObserver()
        ManageStack.isForRedeemForFund = false




        binding.swipeRefresh.setOnRefreshListener {
            viewModel.stepsDetailsApi(token= SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.AccessToken).toString())
            binding.swipeRefresh.isRefreshing = false
        }




        binding.NextButton.setSafeOnClickListener {


            if (amount == 0){
                AndroidExtension.alertBox("Please select coin to redeem.", requireContext())
                return@setSafeOnClickListener
            }

            if (isRedeemRequestToday){
                AndroidExtension.alertBox("Your daily limit reached for withdraw.", requireContext())
                return@setSafeOnClickListener
            }


            val intent = Intent(context, RedeemCoinActivity::class.java)
            intent.putExtra("wallet", "$walletAmount")
            intent.putExtra("amount", amount)
            startActivity(intent)
        }

        val sliderRunnable = Runnable {
            lifecycleScope.launch {
                val currentItem = binding.storeViewpager.currentItem
                val lastItem = binding.storeViewpager.adapter?.itemCount?.minus(1) ?: 0


                if (currentItem == lastItem) {
                    binding.storeViewpager.currentItem = 0
                } else {
                    binding.storeViewpager.currentItem = currentItem + 1
                }
            }
        }

        val callback: ViewPager2.OnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 2000)
            }
        }
        binding.storeViewpager.registerOnPageChangeCallback(callback)

        return binding.root
    }

    private fun addMoneyToWalletObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.stepsDetailsApiResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(requireContext())
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode== 200) {
                                    response.data.result.apply {
                                        binding.totalSteps.text  = last30DaysSteps.toString()
                                        binding.redeemNumber.text  = reedeemCoin.toString()
                                        binding.earnedCoin.text  = "${totalWinings.toInt()}"
                                        walletAmount = totalWinings.toInt()
                                        isRedeemRequestToday = isRequested
                                    }

                                    setAddCoinAdapter()
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            response.message?.let {
                                AndroidExtension.alertBox(it, requireContext())
                            }
                        }

                        is Resource.Empty -> {

                        }
                    }
                }
            }
        }
    }


    private fun setAddCoinAdapter() {
        list.clear()
        list.add(AddCoinList("200", false))
        list.add(AddCoinList("500", false))
        list.add(AddCoinList("1500", false))
        val adapter = AddCoinAdapter(requireContext(),list, this,walletAmount,"Fund")
        binding.addCoinRecycler.adapter = adapter
    }

    private fun setBannerImageAdaptor() {
        val bannerAdapter = BannerAdapter(requireContext(), BannerImages.data)
        binding.storeViewpager.adapter = bannerAdapter
        binding.indicator.setViewPager(binding.storeViewpager)
    }


    override fun addCoin(price: String) {
        amount = price.toInt()
    }


    override fun onResume() {
        super.onResume()
        setBannerImageAdaptor()
    }

}