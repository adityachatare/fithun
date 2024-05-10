package com.fithun.ui.activities.contest

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
import com.fithun.api.responses.CompletedContest
import com.fithun.databinding.ActivityCompletedContestBinding
import com.fithun.ui.adapter.CompletedContestAdapter
import com.fithun.utils.AndroidExtension
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.viewModel.ContestViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CompletedContestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompletedContestBinding
    private val viewModel: ContestViewModel by viewModels()
    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag = false
    var loaderFlag = true

    val docs: ArrayList<CompletedContest> = arrayListOf()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompletedContestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade


        viewModel.myContestListApi(token= SavedPrefManager.getStringPreferences(this, SavedPrefManager.AccessToken).toString(), page = page, limit =  limit)
        myContestListApi()
        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }
        binding.scrollViewPost.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                dataLoadFlag = true
                page++
                binding.ProgressBarScroll.isVisible = true
                if (page > pages) {
                    binding.ProgressBarScroll.isVisible = false
                } else {
                    viewModel.myContestListApi(token= SavedPrefManager.getStringPreferences(this, SavedPrefManager.AccessToken).toString(), page = page, limit =  limit)
                }
            }
        })
    }

    private fun myContestListApi() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.myContestListResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            binding.ProgressBarScroll.isVisible = false
                            binding.NotFound.isVisible = false
                            if (loaderFlag) {
                                Progress.start(this@CompletedContestActivity)
                            }
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                binding.ProgressBarScroll.isVisible = false
                                if (response.data?.responseCode== 200) {

                                    loaderFlag = false
                                    if (!dataLoadFlag) {
                                        docs.clear()
                                    }

                                    pages = response.data.result.pages
                                    page = response.data.result.page
                                    if (response.data.result.docs.isNotEmpty()) {
                                        binding.mainLayout.isVisible = true
                                        binding.NotFound.isVisible = false

                                        docs.addAll(response.data.result.docs)

                                        setLeaderboardContestAdapter()
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
                                AndroidExtension.alertBox(it, this@CompletedContestActivity)
                            }
                        }

                        is Resource.Empty -> {

                        }
                    }
                }
            }
        }
    }

    private fun setLeaderboardContestAdapter() {
        binding.CompletedContestRecycler.layoutManager = LinearLayoutManager(this)
        val adapter = CompletedContestAdapter(this, docs)
        binding.CompletedContestRecycler.adapter = adapter
    }
    
}