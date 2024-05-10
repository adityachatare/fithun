package com.fithun.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.fithun.api.Constants.NO_INTERNET
import com.fithun.api.responses.OrderHistoryResponse
import com.fithun.api.responses.OrderTrackingResponse
import com.fithun.api.responses.PojoClass
import com.fithun.api.responses.ViewOrderHistoryResponse
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
class OrderHistoryViewModel @Inject constructor(app:Application, private val repo:DreamWalkRepository, private val networkHelper: NetworkHelper):AndroidViewModel(app) {

    private val orderListData: MutableStateFlow<Resource<OrderHistoryResponse>> = MutableStateFlow(Resource.Empty())
    val orderListResponseData: StateFlow<Resource<OrderHistoryResponse>> = orderListData

    private val viewOrderData: MutableStateFlow<Resource<ViewOrderHistoryResponse>> = MutableStateFlow(Resource.Empty())
    val viewOrderResponseData: StateFlow<Resource<ViewOrderHistoryResponse>> = viewOrderData

    private val trackOrderData: MutableStateFlow<Resource<OrderTrackingResponse>> = MutableStateFlow(Resource.Empty())
    val trackOrderResponseData: StateFlow<Resource<OrderTrackingResponse>> = trackOrderData




    fun orderListApi(token: String, page:Int, limit:Int) = viewModelScope.launch {
        orderListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.orderLisApi(token, page, limit)
                .catch { e ->
                    orderListData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    orderListData.value = orderListDetailsHandler(data)
                }
        } else {
            orderListData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun orderListDetailsHandler(response: Response<OrderHistoryResponse>): Resource<OrderHistoryResponse> {
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

    fun viewOrderApi(token: String, orderId:String) = viewModelScope.launch {
        viewOrderData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.viewOrderApi(token, orderId)
                .catch { e ->
                    viewOrderData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    viewOrderData.value = viewOrderDetailsHandler(data)
                }
        } else {
            viewOrderData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun viewOrderDetailsHandler(response: Response<ViewOrderHistoryResponse>): Resource<ViewOrderHistoryResponse> {
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



    fun trackOrderApi(shipmentId: String) = viewModelScope.launch {
        trackOrderData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.trackShipmentApi(shipmentId)
                .catch { e ->
                    trackOrderData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    trackOrderData.value = trackOrderDetailsHandler(data)
                }
        } else {
            trackOrderData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun trackOrderDetailsHandler(response: Response<OrderTrackingResponse>): Resource<OrderTrackingResponse> {
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



