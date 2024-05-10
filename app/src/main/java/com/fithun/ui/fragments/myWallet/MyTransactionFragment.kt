package com.fithun.ui.fragments.myWallet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.fithun.api.responses.TransactionList
import com.fithun.databinding.FragmentMyTransactionBinding
import com.fithun.ui.activities.wallet.TransactionHistoryActivity
import com.fithun.ui.adapter.wallet.MyTransactionAdapter
import com.fithun.uiModalClass.MyWallet
import com.fithun.utils.AndroidExtension
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.viewModel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyTransactionFragment : Fragment() {

    private var _binding: FragmentMyTransactionBinding? =  null
    private val viewModel: TransactionViewModel by viewModels()
    private val binding get() = _binding!!

    var walletData: List<MyWallet> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentMyTransactionBinding.inflate(layoutInflater,container,false)

        viewModel.transactionList(token = SavedPrefManager.getStringPreferences(context, SavedPrefManager.AccessToken).toString(), page = 1, limit =40, transactionType = "ALL", transactionStatus = "PAID", fromDate = "", toDate = "")
        transactionResponseObserver()

        binding.ViewAll.setOnClickListener {
            startActivity(Intent(requireContext(), TransactionHistoryActivity::class.java))
        }

        return binding.root

    }

    private fun transactionResponseObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.transactionResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            context?.let { Progress.start(it) }
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode== 200) {
                                    setMyTransactionAdapter(response.data.result.docs)
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            response.message?.let {
                                context?.let { it1 -> AndroidExtension.alertBox(it, it1) }
                            }
                        }

                        is Resource.Empty -> {
                        }

                    }

                }

            }
        }
    }


    private fun setMyTransactionAdapter(list : ArrayList<TransactionList>) {
        binding.myTransactionList.layoutManager = LinearLayoutManager(requireContext())
        val adapter = MyTransactionAdapter(requireContext(), list)
        binding.myTransactionList.adapter = adapter
    }


}