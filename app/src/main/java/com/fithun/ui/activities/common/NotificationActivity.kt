package com.fithun.ui.activities.common

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.fithun.R
import com.fithun.api.responses.NotificationDocs
import com.fithun.databinding.ActivityNotificationBinding
import com.fithun.ui.adapter.notification.NotificationAdapter
import com.fithun.utils.AndroidExtension
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.viewModel.HomeFlowViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationActivity : AppCompatActivity() {

    private lateinit var binding:ActivityNotificationBinding
    private val viewModel: HomeFlowViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        binding.backButton.setOnClickListener {
            finishAfterTransition()
        }


        viewModel.listNotificationApi(token= SavedPrefManager.getStringPreferences(this, SavedPrefManager.AccessToken).toString())
        listNotification()
    }




    private fun setAdapter(list : ArrayList<NotificationDocs>){
        binding.notificationList.layoutManager = LinearLayoutManager(this)
        val adapter = NotificationAdapter(context = this,data=  list)
        binding.notificationList.adapter =  adapter
    }

    private fun listNotification() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.listNotificationResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(this@NotificationActivity)
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data!!.result.docs.size>0) {
                                    setAdapter(response.data.result.docs)
                                    binding.notificationList.visibility = View.VISIBLE
                                    binding.notFound.visibility = View.GONE
                                }else{
                                    binding.notificationList.visibility = View.GONE
                                    binding.notFound.visibility = View.VISIBLE
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            response.message?.let {
                                AndroidExtension.alertBox(it,this@NotificationActivity)
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