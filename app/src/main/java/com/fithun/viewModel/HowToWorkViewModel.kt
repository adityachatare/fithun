package com.fithun.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.fithun.api.Constants.NO_INTERNET
import com.fithun.api.responses.HowItWorkResponse
import com.fithun.api.responses.PojoClass
import com.fithun.api.responses.ReferAndEarnResponse
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
class HowToWorkViewModel @Inject constructor(app:Application, private val repo:DreamWalkRepository, private val networkHelper: NetworkHelper):AndroidViewModel(app) {

    private val howToWorkDetailsData: MutableStateFlow<Resource<HowItWorkResponse>> = MutableStateFlow(Resource.Empty())
    val howToWorkDetailsResponseData: StateFlow<Resource<HowItWorkResponse>> = howToWorkDetailsData


    private val getReferCodeData: MutableStateFlow<Resource<ReferAndEarnResponse>> = MutableStateFlow(Resource.Empty())
    val getReferCodeResponseData: StateFlow<Resource<ReferAndEarnResponse>> = getReferCodeData

    fun howToWorkApi(token: String) = viewModelScope.launch {
        howToWorkDetailsData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.howToWorkDetailApi(token)
                .catch { e ->
                    howToWorkDetailsData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    howToWorkDetailsData.value = howToWorkDetailsHandler(data)
                }
        } else {
            howToWorkDetailsData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun howToWorkDetailsHandler(response: Response<HowItWorkResponse>): Resource<HowItWorkResponse> {
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

    fun getReferCodeApi(token: String) = viewModelScope.launch {
        getReferCodeData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.getReferCodeApi(token)
                .catch { e ->
                    getReferCodeData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    getReferCodeData.value = getReferCodeHandler(data)
                }
        } else {
            getReferCodeData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun getReferCodeHandler(response: Response<ReferAndEarnResponse>): Resource<ReferAndEarnResponse> {
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



