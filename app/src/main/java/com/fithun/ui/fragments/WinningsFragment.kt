package com.fithun.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.fithun.api.responses.WinnerPriceData
import com.fithun.databinding.FragmentWinningsBinding
import com.fithun.ui.adapter.WinningsContestAdapter
import com.fithun.utils.AndroidExtension
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.viewModel.ContestViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class WinningsFragment(var typeSpot : Boolean, val contestId:String) : Fragment() {

    private var _binding: FragmentWinningsBinding? =  null
    private val binding get() = _binding!!


    lateinit var adapterWinningsContest : WinningsContestAdapter

    private val viewModel: ContestViewModel by viewModels()

    var list : ArrayList<WinnerPriceData> = arrayListOf()
    var pages = 0
    var page = 1
    var limit = 50
    var token =  ""
    var winnerPriceType =  ""
    private var dataLoadFlag = false
    private var loaderFlag = false

    lateinit var layoutManager :LinearLayoutManager


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentWinningsBinding.inflate(layoutInflater, container, false)

        layoutManager = LinearLayoutManager(requireContext())

        token =  SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.AccessToken).toString()


        winnerPriceType = if (typeSpot){
            "CURRENT"
        }else{
            "EXPECTED"
        }

        viewModel.priceWinnerListApi(token = token,contestId, page = page, limit =limit, winnerPriceType)


        binding.scrollViewPost.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                dataLoadFlag = true

                loaderFlag = true
                page++
                if (page > pages) {
                    binding.ProgressBarScroll.isVisible = false
                } else {
                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.priceWinnerListApi(token = token, contestId, page = page, limit = limit, winnerPriceType)
                    }
                }
            }
        })

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        priceListResponseObserver()
    }

    private fun priceListResponseObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.priceWinnerListResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            binding.ProgressBarScroll.isVisible = loaderFlag

                        }

                        is Resource.Success -> {
                            try {
                                binding.ProgressBarScroll.isVisible = false
                                if (response.data?.responseCode== 200) {

                                    withContext(Dispatchers.Main) {

                                    loaderFlag = false
                                    if (!dataLoadFlag) {
                                        list.clear()
                                    }

                                    pages = response.data.result.remainingItems
                                    page = response.data.result.page
                                    if (response.data.result.docs.isNotEmpty()) {
                                        binding.mainLayout.isVisible = true
                                        binding.NoDataFound.isVisible = false

                                        list.addAll(response.data.result.docs)
                                        setWinnerContestAdapter()


                                    } else {
                                        binding.mainLayout.isVisible = false
                                        binding.NoDataFound.isVisible = true

                                    }

                                    }



                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            binding.mainLayout.isVisible = false
                            binding.NoDataFound.isVisible = true
                            binding.ProgressBarScroll.isVisible = false
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


    private fun setWinnerContestAdapter() {
        binding.WinningsRecycler.layoutManager = LinearLayoutManager(requireContext())
        adapterWinningsContest = WinningsContestAdapter(requireContext(), list, typeSpot)
        binding.WinningsRecycler.adapter = adapterWinningsContest

    }

}