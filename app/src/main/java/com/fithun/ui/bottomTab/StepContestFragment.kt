package com.fithun.ui.bottomTab

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.fithun.R
import com.fithun.api.responses.ViewContestListDocs
import com.fithun.clickInterfaces.EventFilterClick
import com.fithun.clickInterfaces.JoinContest
import com.fithun.databinding.FragmentStepContestBinding
import com.fithun.ui.activities.contest.ViewStepContestActivity
import com.fithun.ui.adapter.BannerAdapter
import com.fithun.ui.adapter.StepContestTabAdapter
import com.fithun.ui.bottomSheet.FilterEvents
import com.fithun.uiModalClass.BannerImages
import com.fithun.utils.AndroidExtension
import com.fithun.utils.ManageStack
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.viewModel.ContestViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StepContestFragment : Fragment() ,JoinContest,EventFilterClick {

    private var _binding: FragmentStepContestBinding? =  null
    private val binding get() = _binding!!


    lateinit var selectedHome: ImageView
    lateinit var unSelectedHome: ImageView
    lateinit var homeTV: TextView

    lateinit var selectedContest: ImageView
    lateinit var unSelectedContest: ImageView
    lateinit var contestTV: TextView

    lateinit var selectedCounter: ImageView
    lateinit var unSelectedCounter: ImageView
    lateinit var counterTV: TextView

    lateinit var selectedFund: ImageView
    lateinit var unSelectedFund: ImageView
    lateinit var fundTV: TextView

    lateinit var productsTV: TextView


    private lateinit var cartClick: ImageView
    private lateinit var notificationClick: ImageView
    private lateinit var walletClick: ImageView
    private lateinit var medalClick: ImageView
    private  val sliderHandler:Handler=Handler()

    private val viewModel: ContestViewModel by viewModels()
    var page = 1
    var limit = 10
    var token = ""
    private var categoryId = ""
    var search = ""
    private var contestListType = "UPCOMING"
    private var contestDataList: ArrayList<ViewContestListDocs> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentStepContestBinding.inflate(layoutInflater, container, false)
        handleToolAndBottom()

        arguments?.getString("categoryId")?.let { categoryId = it }
        token = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.AccessToken).toString()

        setBannerImageAdaptor()



        binding.swipeRefresh.setOnRefreshListener {
            page = 1
            limit = 10
            categoryId = ""
            search = ""
            contestListType = "UPCOMING"

            viewModel.contestListApi(token = token, page = page, limit = limit, categoryId = categoryId, search = search, contestListType = contestListType,"","")
            binding.swipeRefresh.isRefreshing = false
        }



        viewModel.contestListApi(token = token, page = page, limit = limit, categoryId = categoryId, search = search, contestListType = contestListType,"","")

        binding.filter.setOnClickListener {
            val bottomSheetFragment = FilterEvents(this)
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
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





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contestListObserver()
    }

    private fun setBannerImageAdaptor() {

        val bannerAdapter = BannerAdapter(requireContext(), BannerImages.data)
        binding.storeViewpager.adapter = bannerAdapter
        binding.indicator.setViewPager(binding.storeViewpager)

    }

    private fun setContestAdapter() {
        binding.stepContestRecycler.layoutManager = LinearLayoutManager(requireContext())
        val adapter = StepContestTabAdapter(requireContext(),contestDataList, click = this)
        binding.stepContestRecycler.adapter = adapter
    }

    private fun handleToolAndBottom(){
        cartClick = activity?.findViewById(R.id.cartClick)!!
        notificationClick = activity?.findViewById(R.id.notificationClick)!!
        walletClick = activity?.findViewById(R.id.walletClick)!!
        medalClick = activity?.findViewById(R.id.medalClick)!!
        handleToolBar()
        cartClick.isVisible =  true
        notificationClick.isVisible =  true
        walletClick.isVisible =  true
        medalClick.isVisible =  false
    }

    private fun contestListObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.contestListResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(requireContext())
                        }

                        is Resource.Success -> {

                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200) {
                                    contestDataList = response.data.result.docs
                                    setContestAdapter()

                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            response.message?.let {
                                AndroidExtension.alertBox(it,requireContext())
                            }

                        }

                        is Resource.Empty -> {

                        }

                    }

                }

            }
        }
    }

    override fun joinNow(
        id: String,
        endDate: String,
        startDate: String,
        heading: String,
        firstPrice: String
    ) {
        val intent= Intent(requireContext(), ViewStepContestActivity::class.java)
        intent.putExtra("id",id)
        intent.putExtra("startDate",startDate)
        intent.putExtra("endDate",endDate)
        intent.putExtra("heading",heading)
        intent.putExtra("firstPrice", firstPrice)
        startActivity(intent)

    }

    private fun handleToolBar(){
        selectedHome = activity?.findViewById(R.id.SelectedHome)!!
        unSelectedHome = activity?.findViewById(R.id.UnSelectedHome)!!

        unSelectedContest = activity?.findViewById(R.id.UnSelectedContest)!!
        selectedContest = activity?.findViewById(R.id.SelectedContest)!!

        unSelectedCounter = activity?.findViewById(R.id.UnSelectedCounter)!!
        selectedCounter = activity?.findViewById(R.id.SelectedCounter)!!

        unSelectedFund = activity?.findViewById(R.id.UnSelectedFund)!!
        selectedFund = activity?.findViewById(R.id.SelectedFund)!!

        homeTV = activity?.findViewById(R.id.HomeTV)!!
        contestTV = activity?.findViewById(R.id.ContestTV)!!
        productsTV = activity?.findViewById(R.id.ProductsTV)!!
        counterTV = activity?.findViewById(R.id.CounterTV)!!
        fundTV = activity?.findViewById(R.id.FundTV)!!

        cartClick = activity?.findViewById(R.id.cartClick)!!
        notificationClick = activity?.findViewById(R.id.notificationClick)!!
        walletClick = activity?.findViewById(R.id.walletClick)!!
        medalClick = activity?.findViewById(R.id.medalClick)!!
    }

    override fun onResume() {
        super.onResume()
    }

    override fun filterEvents(contestType: String, day: String, entryFee: String, spot: String) {
        val contestTypeReq =   if (contestType == "Choose Contest Type" ){
            ""
        }else{
            contestType
        }

        val dayReq = when (day) {
            "Select Day" -> {
                ""
            }
            "All" -> {
                "UPCOMING"
            }
            else -> {
                day
            }
        }


        val entryFeeReq =  if (entryFee == "Select Entry Fee"){
            ""
        }else{
            entryFee
        }


        val spotReq =  if (spot== "Select Spot"){
            ""
        }else{
            spot
        }

        page =  1
        limit = 10
        search = ""
        viewModel.contestListApi(token = token, page = page, limit = limit, categoryId = categoryId, search = search, contestListType = dayReq.uppercase(),spots=spotReq, entryFees = entryFeeReq)


    }

}