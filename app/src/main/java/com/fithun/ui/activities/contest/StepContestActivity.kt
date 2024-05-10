package com.fithun.ui.activities.contest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.fithun.R
import com.fithun.api.responses.ViewContestListDocs
import com.fithun.clickInterfaces.EventFilterClick
import com.fithun.clickInterfaces.JoinContest
import com.fithun.databinding.ActivityStepContestBinding
import com.fithun.ui.adapter.BannerAdapter
import com.fithun.ui.adapter.StepContestTabAdapter
import com.fithun.ui.bottomSheet.FilterEvents
import com.fithun.uiModalClass.BannerImages
import com.fithun.utils.AndroidExtension
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.viewModel.ContestViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StepContestActivity : AppCompatActivity(), JoinContest, EventFilterClick {

    private lateinit var binding:ActivityStepContestBinding


    private val viewModel: ContestViewModel by viewModels()
    var page = 1
    var pages = 0
    var limit = 10
    var token = ""
    private var dataLoadFlag = false
    private var loaderFlag = true

    private var categoryId = ""
    var search = ""
    private var contestListType = "UPCOMING"
    private var contestDataList: ArrayList<ViewContestListDocs> = arrayListOf()
    private  val sliderHandler: Handler = Handler()


    var contestTypeReq = ""
    var dayReq = ""
    var entryFeeReq = ""
    var spotReq = ""


    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStepContestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        intent.getStringExtra("categoryId")?.let { categoryId = it }
        token = SavedPrefManager.getStringPreferences(this,SavedPrefManager.AccessToken).toString()


        setBannerImageAdaptor()


        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }

        binding.filter.setOnClickListener {
            val bottomSheetFragment = FilterEvents(this)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
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


        binding.scrollViewPost.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                dataLoadFlag = true
                page++
                binding.ProgressBarScroll.isVisible = true
                if (page > pages) {
                    binding.ProgressBarScroll.isVisible = false
                } else {
                    viewModel.contestListApi(token = token, page = page, limit = limit, categoryId = categoryId, search = search, contestListType = dayReq.uppercase(),spots=spotReq, entryFees = entryFeeReq)


                }
            }
        })



        binding.swipeRefresh.setOnRefreshListener {
            page = 1
            limit = 10
            search = ""
            dataLoadFlag = false
            loaderFlag = true
            contestListType = "UPCOMING"
            viewModel.contestListApi(token = token, page = page, limit = limit, categoryId = categoryId, search = search, contestListType = contestListType,"","")
            binding.swipeRefresh.isRefreshing = false

        }





        contestListObserver()


    }


    override fun onStart() {
        super.onStart()
        viewModel.contestListApi(token = token, page = page, limit = limit*page, categoryId = categoryId, search = search, contestListType = contestListType,"","")

    }

    private fun setBannerImageAdaptor() {

        val bannerAdapter = BannerAdapter(this@StepContestActivity, BannerImages.data)
        binding.storeViewpager.adapter = bannerAdapter
        binding.indicator.setViewPager(binding.storeViewpager)

    }

    private fun setContestAdapter() {
        binding.stepContestRecycler.layoutManager = LinearLayoutManager(this@StepContestActivity)
        val adapter = StepContestTabAdapter(this@StepContestActivity,contestDataList, click = this)
        binding.stepContestRecycler.adapter = adapter
    }



    private fun contestListObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.contestListResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            if (loaderFlag){
                                Progress.start(this@StepContestActivity)
                            }
                        }

                        is Resource.Success -> {

                            try {
                                Progress.stop()
                                binding.ProgressBarScroll.isVisible = false
                                if (response.data?.responseCode == 200) {


                                    loaderFlag = false
                                    if (!dataLoadFlag) {
                                        contestDataList.clear()
                                    }
                                    if (response.data.result.docs.isNotEmpty()) {
                                        binding.mainLayout.isVisible = true
                                        binding.NotFound.isVisible = false

                                        contestDataList.addAll(response.data.result.docs)
                                        pages = response.data.result.pages
                                        page = response.data.result.page
                                        setContestAdapter()
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
                            binding.ProgressBarScroll.isVisible = false
                            binding.mainLayout.isVisible = false
                            binding.NotFound.isVisible = true
                            response.message?.let {
                                AndroidExtension.alertBox(it,this@StepContestActivity)
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
        val intent= Intent(this@StepContestActivity, ViewStepContestActivity::class.java)
        intent.putExtra("id",id)
        intent.putExtra("startDate",startDate)
        intent.putExtra("endDate",endDate)
        intent.putExtra("heading",heading)
        intent.putExtra("firstPrice",firstPrice)
        startActivity(intent)

    }




    override fun filterEvents(contestType: String, day: String, entryFee: String, spot: String) {
        contestTypeReq = when (contestType) {
            "Choose Contest Type" -> {
                ""
            }
            "Walk A Thon" -> {
                "Marathon"
            }
            else -> {
                contestType
            }
        }

        dayReq = when (day) {
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


        entryFeeReq =  if (entryFee == "Select Entry Fee"){
            ""
        }else{
            entryFee
        }


        spotReq =  if (spot== "Select Spot"){
            ""
        }else{
            spot
        }

        page =  1
        limit = 10
        search = ""
        loaderFlag = true
        dataLoadFlag = false
        contestDataList.clear()
        viewModel.contestListApi(token = token, page = page, limit = limit, categoryId = categoryId, search = search, contestListType = dayReq.uppercase(),spots=spotReq, entryFees = entryFeeReq)



    }

}

