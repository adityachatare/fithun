package com.fithun.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.fithun.api.Constants.NO_INTERNET
import com.fithun.api.responses.PojoClass
import com.fithun.api.responses.TransactionHistoryResponse
import com.fithun.repository.DreamWalkRepository
import com.fithun.utils.AndroidExtension
import com.fithun.utils.NetworkHelper
import com.fithun.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(app:Application, private val repo:DreamWalkRepository, private val networkHelper: NetworkHelper):AndroidViewModel(app) {

        private val transactionData: MutableStateFlow<Resource<TransactionHistoryResponse>> = MutableStateFlow(Resource.Empty())
        val  transactionResponseData: StateFlow<Resource<TransactionHistoryResponse>> = transactionData

        //    Add Address
        fun transactionList(token: String, page:Int
                       ,limit:Int,transactionType:String,
                            transactionStatus:String,fromDate:String,toDate:String) = viewModelScope.launch {
            transactionData.value = Resource.Loading()

            if (networkHelper.hasInternetConnection()) {

                repo.transactionListApi(token=token,page= page,
                    limit=limit, transactionType=transactionType, transactionStatus=transactionStatus,fromDate=fromDate, toDate=toDate)
                    .catch { e ->
                        transactionData.value = Resource.Error(AndroidExtension.manageMessages(e))
                    }.collect { data ->
                        transactionData.value = transactionHandleResponse(data)
                    }
            } else {
                transactionData.value = Resource.Error(NO_INTERNET)
            }
        }

        private fun transactionHandleResponse(response: Response<TransactionHistoryResponse>): Resource<TransactionHistoryResponse> {
            if (response.isSuccessful) {
                response.body()?.let { data ->
                    return Resource.Success(data)
                }
            }
            val gson = GsonBuilder().create()
            var pojo = PojoClass()

            try {
                pojo = gson.fromJson(response.errorBody()!!.string(), pojo::class.java)

            } catch (e: Exception) {
                e.printStackTrace()
            }
            return Resource.Error(pojo.responseMessage)
        }
}



