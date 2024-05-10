package com.fithun.ui.bottomTab

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.fithun.R
import com.fithun.api.responses.ContestDetails
import com.fithun.clickInterfaces.JoinStepContest
import com.fithun.databinding.FragmentStepContestTabBinding
import com.fithun.ui.activities.contest.StepContestActivity
import com.fithun.ui.adapter.BannerAdapter
import com.fithun.ui.adapter.StepContestAdapter
import com.fithun.uiModalClass.BannerImages
import com.fithun.utils.AndroidExtension
import com.fithun.utils.ManageStack
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.viewModel.ContestViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@AndroidEntryPoint
class StepContestTabFragment : Fragment(), JoinStepContest {
    private var _binding: FragmentStepContestTabBinding? =  null
    private val binding get() = _binding!!

    var page = 1
    var limit = 10
    var pages = 0
    var dataLoadFlag = false
    var loaderFlag = true


    var token = ""
    private var contestDataList: ArrayList<ContestDetails> = arrayListOf()

    private  val sliderHandler:Handler=Handler()

    private val viewModel: ContestViewModel by viewModels()


    companion object{ @JvmStatic
        fun newInstance() = StepContestTabFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentStepContestTabBinding.inflate(layoutInflater, container, false)






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
                binding.ProgressBarScroll.visibility = View.VISIBLE
                if (page >= pages) {

                    binding.ProgressBarScroll.visibility = View.GONE
                } else {
                    page++
                    viewModel.myContestListApi(token= SavedPrefManager.getStringPreferences(requireContext(),
                        SavedPrefManager.AccessToken).toString(), page = page, limit =  limit)
                }
            }
        })

        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.AccessToken).toString()






        binding.swipeRefresh.setOnRefreshListener {
            page = 1
            limit = 10
            dataLoadFlag = false
            loaderFlag = true

            viewModel.contestCategoryListApi(token=token, page=page,limit=limit, search="")
            binding.swipeRefresh.isRefreshing= false
        }






        return binding.root
    }



    override fun onStart() {
        super.onStart()
        viewModel.contestCategoryListApi(token=token, page=1,limit=10, search="")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contextCategoryList()
    }







    private fun contextCategoryList() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.contestCatListApiResponseData.collectLatest { response ->
                    when (response) {

                        is Resource.Loading -> {
                            if (loaderFlag){
                                Progress.start(requireContext())
                            }

                        }


                        is Resource.Success -> {
                            Progress.stop()
                            binding.ProgressBarScroll.visibility = View.GONE
                            if (response.data?.responseCode == 200) {
                                loaderFlag = false
                                if (!dataLoadFlag) {
                                        contestDataList.clear()
                                }
                                if (response.data.result.docs.isNotEmpty()) {
                                        pages = response.data.result.pages!!
                                        page = response.data.result.page!!
                                        contestDataList = response.data.result.docs
                                        setContestAdapter()
                                }

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
    private fun setBannerImageAdaptor() {

        val bannerAdapter = BannerAdapter(requireContext(), BannerImages.data)
        binding.storeViewpager.adapter = bannerAdapter
        binding.indicator.setViewPager(binding.storeViewpager)

    }

    private fun setContestAdapter() {
        binding.contestRecycler.layoutManager = LinearLayoutManager(requireContext())
        val adapter = StepContestAdapter(requireContext(),contestDataList, click= this@StepContestTabFragment)
        binding.contestRecycler.adapter = adapter
    }




    override fun join(id: String?) {

        val intent = Intent(requireContext(),StepContestActivity::class.java)
        intent.putExtra("categoryId",id)
        startActivity(intent)


    }


    override fun onResume() {
        super.onResume()
        setBannerImageAdaptor()
    }




}