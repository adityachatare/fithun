package com.fithun.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.fithun.api.responses.LeaderBoardList
import com.fithun.databinding.FragmentLeaderboardBinding
import com.fithun.ui.adapter.LeaderboardContestAdapter
import com.fithun.utils.AndroidExtension
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.viewModel.ContestViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LeaderboardFragment(val id:String, private val isJoinedUser:Boolean) : Fragment() {

    private var _binding: FragmentLeaderboardBinding? =  null
    private val binding get() = _binding!!


    var list : ArrayList<LeaderBoardList> = arrayListOf()
    var pages = 0
    var page = 1
    var limit = 50
    var token =  ""

    private var dataLoadFlag = false
    private var loaderFlag = false



    private val viewModel: ContestViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeaderboardBinding.inflate(layoutInflater, container, false)
        token = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.AccessToken).toString()


        viewModel.leaderboardLUserListApi(token = token, contestId = id,page =  page, limit=limit)


        binding.scrollViewPost.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                dataLoadFlag = true

                loaderFlag = true
                page++
                if (page > pages) {
                    binding.ProgressBarScroll.isVisible = false
                } else {
                    viewModel.leaderboardLUserListApi(token = token, contestId = id,page =  page, limit=limit)
                }
            }
        })



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLeaderBoardUserResponseObserver()
    }


    private fun setLeaderboardContestAdapter() {
        binding.LeaderboardRecycler.layoutManager = LinearLayoutManager(requireContext())
        val adapter = LeaderboardContestAdapter(requireContext(), list,isJoinedUser)
        binding.LeaderboardRecycler.adapter = adapter

    }






    private fun viewLeaderBoardUserResponseObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.leaderBoardResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            binding.ProgressBarScroll.isVisible = loaderFlag

                        }

                        is Resource.Success -> {
                            try {
                                binding.ProgressBarScroll.isVisible = false
                                if (response.data?.responseCode== 200) {

                                    loaderFlag = false
//                                    if (!dataLoadFlag) {
                                        list.clear()
//                                    }

                                    pages = response.data.result.pages
                                    page = response.data.result.page
                                    if (response.data.result.docs.isNotEmpty()) {
                                        binding.mainLayout.isVisible = true
                                        binding.NoDataFound.isVisible = false

                                        list.addAll(response.data.result.docs)

                                        setLeaderboardContestAdapter()
                                    } else {
                                        binding.mainLayout.isVisible = false
                                        binding.NoDataFound.isVisible = true

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




}