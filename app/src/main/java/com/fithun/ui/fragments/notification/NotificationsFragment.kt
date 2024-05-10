package com.fithun.ui.fragments.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.fithun.api.responses.NotificationDocs
import com.fithun.databinding.FragmentNotificationsBinding
import com.fithun.ui.adapter.notification.NotificationAdapter
import com.fithun.utils.AndroidExtension
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.viewModel.HomeFlowViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? =  null
    private val binding get() = _binding!!
    private val viewModel: HomeFlowViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        _binding = FragmentNotificationsBinding.inflate(layoutInflater, container, false)
        viewModel.listNotificationApi(token= SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.AccessToken).toString())

        listNotification()
        return binding.root
    }

    private fun setAdapter(list : ArrayList<NotificationDocs>){
        binding.notificationList.layoutManager = LinearLayoutManager(requireContext())
        val adapter = NotificationAdapter(context = requireContext(),data=  list)
        binding.notificationList.adapter =  adapter
    }

    private fun listNotification() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.listNotificationResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(requireContext())
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data!!.result.docs.size>0) {
                                    setAdapter(response.data.result.docs)
                                    binding.notificationList.visibility =View.VISIBLE
                                    binding.notFound.visibility =View.GONE
                                }else{
                                    binding.notificationList.visibility =View.GONE
                                    binding.notFound.visibility =View.VISIBLE
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
}