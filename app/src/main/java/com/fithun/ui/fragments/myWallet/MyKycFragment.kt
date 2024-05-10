package com.fithun.ui.fragments.myWallet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.fithun.R
import com.fithun.databinding.FragmentMyKycBinding
import com.fithun.ui.activities.wallet.VerifyAccountActivity
import com.fithun.ui.bottomSheet.WalletDialog
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.viewModel.HomeFlowViewModel
import com.fithun.viewModel.KycViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyKycFragment : Fragment() {

    private var _binding: FragmentMyKycBinding? = null
    private val binding get() = _binding!!
    private val viewModel: KycViewModel by viewModels()
    private val viewModel1: HomeFlowViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyKycBinding.inflate(layoutInflater, container, false)
        viewModel1.userProfile(SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.AccessToken).toString())
        viewModel.getKycApi(
            SavedPrefManager.getStringPreferences(
                requireContext(),
                SavedPrefManager.AccessToken
            ).toString()
        )

        binding.verifyKyc.setOnClickListener {
            startActivity(Intent(requireContext(), VerifyAccountActivity::class.java))
        }


        binding.imgUpiId.setOnClickListener {
            parentFragmentManager.let {
                WalletDialog(
                    "Great news!",
                    "You've verified your UPI (Unified Payments Interface) account and then all your transactions will be securely processed through this verified UPI account. Enjoy seamless and convenient payments with confidence!"
                ).show(it, "MyCustomFragment")
            }
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getKycApi()
        profileObserver()
    }


    private fun getKycApi() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getKycApiResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                        }

                        is Resource.Success -> {
                            try {
                                if (response.data!!.responseCode == 200) {
                                    if (response.data.result.upiId.isNotEmpty() && response.data.result.panNumber.isNotEmpty()) {
                                        binding.verificationLayout.isVisible = true
                                        binding.notFound.isVisible = false

                                        binding.txtUpiId.text = response.data.result.upiId
                                        binding.txtPanCard.text = response.data.result.panNumber


                                    } else {
                                        binding.verificationLayout.isVisible = false
                                        binding.notFound.isVisible = true
                                    }

                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            binding.verificationLayout.isVisible = false
                            binding.notFound.isVisible = true

                        }

                        is Resource.Empty -> {

                        }
                    }
                }
            }
        }
    }



    private fun profileObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel1.profileApiResponseData.collect { response ->
                    when(response){

                        is Resource.Loading ->{}

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200){


                                    with(response.data.result){
                                        binding.mobileNumber.text = "${response.data.result.mobileCode} ${response.data!!.result.mobileNumber}"
                                        binding.emailAddress.text = response.data.result.email

                                        if (kycDetails != null){
                                            binding.txtUpiId.text = kycDetails.upiId
                                        }else{
                                            binding.txtUpiId.text = additionalDetails.upiId

                                        }




                                        name = response.data.result.name

                                    }

                                }

                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error ->{
                            Progress.stop()
                        }

                        is Resource.Empty ->{

                        }
                    }
                }
            }
        }
    }

}