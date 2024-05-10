package com.fithun.ui.activities.contest

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.fithun.R
import com.fithun.api.responses.MyContestWinnerList
import com.fithun.clickInterfaces.WalletFilter
import com.fithun.databinding.ActivityWinnerUsersBinding
import com.fithun.ui.adapter.LeaderboardContestLiveAdapter
import com.fithun.ui.bottomSheet.HomeWinners
import com.fithun.utils.AndroidExtension
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.utils.setSafeOnClickListener
import com.fithun.viewModel.ContestViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WinnerUsersActivity : AppCompatActivity(), WalletFilter {

    private lateinit var binding: ActivityWinnerUsersBinding
    private val viewModel: ContestViewModel by viewModels()
    var contestId:String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWinnerUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade
        intent.getStringExtra("contestId").let { contestId = it }

        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }
        viewModel.myContestWinnerListApi(token= SavedPrefManager.getStringPreferences(this, SavedPrefManager.AccessToken).toString(), contestId = contestId!!)
        myContestWinnerListApi()

        binding.filter.setSafeOnClickListener {
            val bottomSheetFragment = HomeWinners(this)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

    }

    private fun myContestWinnerListApi() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.myContestWinnerListResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            binding.Header.isInvisible = true
                            Progress.start(this@WinnerUsersActivity)
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data!!.result.isNotEmpty()){
                                    binding.NotFound.isVisible = false
                                    binding.Header.isInvisible = false
                                    setWinnerContestAdapter(response.data.result)
                                }else{
                                    binding.NotFound.isVisible = true
                                    binding.Header.isInvisible = true
                                }


                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()

                            binding.NotFound.isVisible = true
                            binding.Header.isInvisible = true

                            response.message?.let {
                                AndroidExtension.alertBox(it, this@WinnerUsersActivity)
                            }
                        }

                        is Resource.Empty -> {

                        }


                    }
                }
            }
        }
    }


    private fun setWinnerContestAdapter(list:ArrayList<MyContestWinnerList>) {
        binding.WinningsRecycler.layoutManager = LinearLayoutManager(this)
        val adapter = LeaderboardContestLiveAdapter(context = this,isFrom = "Home", list)
        binding.WinningsRecycler.adapter = adapter
    }

    override fun filterWalletClick(startDate: String, endDate: String, type: String) {

    }

}