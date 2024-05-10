package com.fithun.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.fithun.api.Constants.NO_INTERNET
import com.fithun.api.responses.FAQResponse
import com.fithun.api.responses.PojoClass
import com.fithun.api.responses.ViewStaticResponse
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
class StaticViewModel @Inject constructor(app:Application, private val repo:DreamWalkRepository,private val networkHelper: NetworkHelper):AndroidViewModel(app) {

    private val viewProfileData: MutableStateFlow<Resource<ViewStaticResponse>> = MutableStateFlow(Resource.Empty())
    val  viewProfileApiResponseData: StateFlow<Resource<ViewStaticResponse>> = viewProfileData

    private val faqListApiData: MutableStateFlow<Resource<FAQResponse>> = MutableStateFlow(Resource.Empty())
    val faqListApiResponseData: StateFlow<Resource<FAQResponse>> = faqListApiData

    private val howPlayListData: MutableStateFlow<Resource<FAQResponse>> = MutableStateFlow(Resource.Empty())
    val howPlayListApiResponseData: StateFlow<Resource<FAQResponse>> = howPlayListData

        //    UserProfile
        fun viewStaticData(type: String) = viewModelScope.launch {
            viewProfileData.value = Resource.Loading()

            if (networkHelper.hasInternetConnection()) {

                repo.viewStaticData(type=type)
                    .catch { e ->
                        viewProfileData.value = Resource.Error(AndroidExtension.manageMessages(e))
                    }.collect { data ->
                        viewProfileData.value = viewstaticResponse(data)
                    }
            } else {
                viewProfileData.value = Resource.Error(NO_INTERNET)
            }
        }

        private fun viewstaticResponse(response: Response<ViewStaticResponse>): Resource<ViewStaticResponse> {
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


    fun faqListApi(search:String) = viewModelScope.launch {
        faqListApiData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.faqListApi(search)
                .catch { e ->
                    faqListApiData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    faqListApiData.value = FAQResponseHandle(data)
                }
        }else{
            faqListApiData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun FAQResponseHandle(response: Response<FAQResponse>): Resource<FAQResponse> {

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


//    how to play
    fun howPlayList(page :Int,limit :Int, search :String) = viewModelScope.launch {
        howPlayListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.howtoPlay( page=page, limit=limit, search=search)
                .catch { e ->
                    howPlayListData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    howPlayListData.value = getListDataProductResponse(data)
                }
        } else {
            howPlayListData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun getListDataProductResponse(response: Response<FAQResponse>): Resource<FAQResponse> {
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



