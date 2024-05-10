package com.fithun.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.fithun.api.Constants.NO_INTERNET
import com.fithun.api.responses.CommonResponse
import com.fithun.api.responses.KycListResponse
import com.fithun.api.responses.PojoClass
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
class KycViewModel @Inject constructor(app:Application, private val repo:DreamWalkRepository, private val networkHelper: NetworkHelper):AndroidViewModel(app) {

    private val createKycData: MutableStateFlow<Resource<CommonResponse>> = MutableStateFlow(Resource.Empty())
    val createKycResponseData: StateFlow<Resource<CommonResponse>> = createKycData

    private val getKycApi: MutableStateFlow<Resource<KycListResponse>> = MutableStateFlow(Resource.Empty())
    val getKycApiResponseData: StateFlow<Resource<KycListResponse>> = getKycApi

    fun createKycApi(token: String,name:String, panNumber:String, birthDate:String, upiId: String) = viewModelScope.launch {
        createKycData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.createKYCApi(token=token,name=name, panNumber=panNumber, birthDate=birthDate, upiId= upiId)
                .catch { e ->
                    createKycData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    createKycData.value = orderListDetailsHandler(data)
                }
        } else {
            createKycData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun orderListDetailsHandler(response: Response<CommonResponse>): Resource<CommonResponse> {
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

    fun getKycApi(token: String) = viewModelScope.launch {
        getKycApi.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.getKycApi(token=token)
                .catch { e ->
                    getKycApi.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    getKycApi.value = getKycDetailsHandler(data)
                }
        } else {
            getKycApi.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun getKycDetailsHandler(response: Response<KycListResponse>): Resource<KycListResponse> {
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



