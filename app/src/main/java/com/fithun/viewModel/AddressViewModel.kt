package com.fithun.viewModel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder

import com.fithun.api.Constants.NO_INTERNET
import com.fithun.api.responses.AddressListResponse
import com.fithun.api.responses.CommonResponse
import com.fithun.api.responses.PojoClass
import com.fithun.api.responses.StateCityResponse
import com.fithun.api.responses.ViewAddressResponse

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

class AddressViewModel @Inject constructor(app:Application, private val repo:DreamWalkRepository, private val networkHelper: NetworkHelper):AndroidViewModel(app) {

        private val addAddressData: MutableStateFlow<Resource<CommonResponse>> = MutableStateFlow(Resource.Empty())
        val  addressResponseData: StateFlow<Resource<CommonResponse>> = addAddressData

        private val updateAddressData: MutableStateFlow<Resource<CommonResponse>> = MutableStateFlow(Resource.Empty())
        val  updateAddressResponseData: StateFlow<Resource<CommonResponse>> = updateAddressData

        private val deleteAddressData: MutableStateFlow<Resource<CommonResponse>> = MutableStateFlow(Resource.Empty())
        val  deleteResponseAddressData: StateFlow<Resource<CommonResponse>> = deleteAddressData

        private val viewAddressData: MutableStateFlow<Resource<ViewAddressResponse>> = MutableStateFlow(Resource.Empty())
        val  viewResponseAddressData: StateFlow<Resource<ViewAddressResponse>> = viewAddressData

        private val getAddressData: MutableStateFlow<Resource<AddressListResponse>> = MutableStateFlow(Resource.Empty())
        val  getAddressResponseData: StateFlow<Resource<AddressListResponse>> = getAddressData

        private val getStateCityData: MutableStateFlow<Resource<StateCityResponse>> = MutableStateFlow(Resource.Empty())
        val  getStateCityResponseData: StateFlow<Resource<StateCityResponse>> = getStateCityData

        private val addAddressForOrderData: MutableStateFlow<Resource<CommonResponse>> = MutableStateFlow(Resource.Empty())
        val addAddressForOrderResponseData: StateFlow<Resource<CommonResponse>> = addAddressForOrderData


    //    Add Address
        fun addAddress(token: String, firstName:String
                       ,lastName:String,houseNumber:String,
                       area:String,city:String,state:String,country:String,
                       zipCode:String,stateCode:String) = viewModelScope.launch {
            addAddressData.value = Resource.Loading()

            if (networkHelper.hasInternetConnection()) {

                repo.addAddressApi(token=token,firstName= firstName,
                    lastName=lastName, houseNumber=houseNumber, area=area, city=city, state=state, country=country, zipCode=zipCode, stateCode = stateCode)
                    .catch { e ->
                        addAddressData.value = Resource.Error(AndroidExtension.manageMessages(e))
                    }.collect { data ->
                        addAddressData.value = addAddressHandleResponse(data)
                    }
            } else {
                addAddressData.value = Resource.Error(NO_INTERNET)
            }

        }

        private fun addAddressHandleResponse(response: Response<CommonResponse>): Resource<CommonResponse> {
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

        //    Update Address
        fun updateAddressApi(token: String, firstName:String
                       ,lastName:String,houseNumber:String,
                       area:String,city:String,state:String,country:String,
                       zipCode:String, id:String,stateCode: String) = viewModelScope.launch {
            updateAddressData.value = Resource.Loading()

            if (networkHelper.hasInternetConnection()) {

                repo.updateAddressApi(token=token,firstName= firstName,
                    lastName=lastName, houseNumber=houseNumber, area=area, city=city, state=state, country=country, zipCode=zipCode,id=id,stateCode=stateCode)
                    .catch { e ->
                        updateAddressData.value = Resource.Error(AndroidExtension.manageMessages(e))
                    }.collect { data ->
                        updateAddressData.value = updateAddressHandleResponse(data)
                    }
            } else {
                updateAddressData.value = Resource.Error(NO_INTERNET)
            }

        }

        private fun updateAddressHandleResponse(response: Response<CommonResponse>): Resource<CommonResponse> {
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

        //    Delete Address

        fun deleteAddressApi(token: String,id:String) = viewModelScope.launch {
            deleteAddressData.value = Resource.Loading()

            if (networkHelper.hasInternetConnection()) {

                repo.deleteAddressApi(token=token,id=id)
                    .catch { e ->
                        deleteAddressData.value = Resource.Error(AndroidExtension.manageMessages(e))
                    }.collect { data ->
                        deleteAddressData.value = deleteAddressHandleResponse(data)
                    }
            } else {
                deleteAddressData.value = Resource.Error(NO_INTERNET)
            }

        }

        private fun deleteAddressHandleResponse(response: Response<CommonResponse>): Resource<CommonResponse> {
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


        //    View Address

        fun viewAddressApi(token: String,id:String) = viewModelScope.launch {
            viewAddressData.value = Resource.Loading()

            if (networkHelper.hasInternetConnection()) {

                repo.viewAddressApi(token=token,id=id)
                    .catch { e ->
                        viewAddressData.value = Resource.Error(AndroidExtension.manageMessages(e))
                    }.collect { data ->
                        viewAddressData.value = viewAddressHandleResponse(data)
                    }
            } else {
                viewAddressData.value = Resource.Error(NO_INTERNET)
            }

        }

        private fun viewAddressHandleResponse(response: Response<ViewAddressResponse>): Resource<ViewAddressResponse> {
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

        //    Get Address

        fun getAddressApi(token: String) = viewModelScope.launch {
            getAddressData.value = Resource.Loading()

            if (networkHelper.hasInternetConnection()) {

                repo.listAddress(token=token)
                    .catch { e ->
                        getAddressData.value = Resource.Error(AndroidExtension.manageMessages(e))
                    }.collect { data ->
                        getAddressData.value = getAddressHandleResponse(data)
                    }
            } else {
                getAddressData.value = Resource.Error(NO_INTERNET)
            }

        }

        private fun getAddressHandleResponse(response: Response<AddressListResponse>): Resource<AddressListResponse> {
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

        //    Add Address

        fun stateCityApi(code: String) = viewModelScope.launch {
            getStateCityData.value = Resource.Loading()

            if (networkHelper.hasInternetConnection()) {

                repo.stateAndCityApi(code=code)
                    .catch { e ->
                        getStateCityData.value = Resource.Error(AndroidExtension.manageMessages(e))
                    }.collect { data ->
                        getStateCityData.value = stateCityHandleResponse(data)
                    }
            } else {
                getStateCityData.value = Resource.Error(NO_INTERNET)
            }
        }

        private fun stateCityHandleResponse(response: Response<StateCityResponse>): Resource<StateCityResponse> {
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

    // Add Order Address Api


    fun addOrderAddressAPi(token: String,addressId:String) = viewModelScope.launch {
        addAddressForOrderData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.addAddressForOrderApi(token=token,addressId=addressId)
                .catch { e ->
                    addAddressForOrderData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    addAddressForOrderData.value = addOrderAddressResponse(data)
                }
        } else {
            addAddressForOrderData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun addOrderAddressResponse(response: Response<CommonResponse>): Resource<CommonResponse> {
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



