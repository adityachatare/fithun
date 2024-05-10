package com.fithun.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.fithun.api.Constants
import com.fithun.api.Constants.NO_INTERNET
import com.fithun.api.responses.CheckOutCartResponse
import com.fithun.api.responses.PojoClass
import com.fithun.api.responses.ResendOtpResponse
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
class RedeemViewModel @Inject constructor(app:Application, private val repo:DreamWalkRepository, private val networkHelper: NetworkHelper):AndroidViewModel(app) {

    private val sendOtpOnMobile: MutableStateFlow<Resource<ResendOtpResponse>> =
        MutableStateFlow(Resource.Empty())
    val resendOtpApiResponseData: StateFlow<Resource<ResendOtpResponse>> = sendOtpOnMobile

    private val verifyOTPMobileEmailData: MutableStateFlow<Resource<ResendOtpResponse>> =
        MutableStateFlow(Resource.Empty())
    val verifyOTPMobileEmailResponseData: StateFlow<Resource<ResendOtpResponse>> = verifyOTPMobileEmailData

    private val redeemCoinData: MutableStateFlow<Resource<CheckOutCartResponse>> = MutableStateFlow(Resource.Empty())
    val redeemCoinResponseData: StateFlow<Resource<CheckOutCartResponse>> = redeemCoinData

    private val addMoneyToWallet: MutableStateFlow<Resource<CheckOutCartResponse>> = MutableStateFlow(Resource.Empty())
    val addMoneyToWalletResponseData: StateFlow<Resource<CheckOutCartResponse>> = addMoneyToWallet

    fun sendOtpOnMobileApi(mobile: String) = viewModelScope.launch {
        sendOtpOnMobile.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.sendOtpOnMobile(mobile)
                .catch { e ->
                    sendOtpOnMobile.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    sendOtpOnMobile.value = sendOtpOnMobileHandler(data)
                }
        } else {
            sendOtpOnMobile.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun sendOtpOnMobileHandler(response: Response<ResendOtpResponse>): Resource<ResendOtpResponse> {
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

    fun verifyOTPMobileEmailApi(token: String, otp : String) = viewModelScope.launch {
        verifyOTPMobileEmailData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.verifyOTPMobileEmail(token = token , otp = otp )
                .catch { e ->
                    verifyOTPMobileEmailData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    verifyOTPMobileEmailData.value = verifyOTPMobileEmailHandler(data)
                }
        } else {
            verifyOTPMobileEmailData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun verifyOTPMobileEmailHandler(response: Response<ResendOtpResponse>): Resource<ResendOtpResponse> {
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

    fun redeemApi(token: String,amount: String,upiId:String,accountHolderName:String,mobileNumber:String) = viewModelScope.launch {
        redeemCoinData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.redeemCoinApi(token = token,amount=amount, upiId=upiId,accountHolderName=accountHolderName,mobileNumber=mobileNumber)
                .catch { e ->
                    redeemCoinData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    redeemCoinData.value = redeemCoinResponseHandle(data)
                }
        } else {
            redeemCoinData.value = Resource.Error(Constants.NO_INTERNET)
        }
    }


    private fun redeemCoinResponseHandle(response: Response<CheckOutCartResponse>): Resource<CheckOutCartResponse> {
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

    fun addMoneyToWalletApi(token: String, amount: String) = viewModelScope.launch {
        addMoneyToWallet.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.addMoneyToWalletApi(token=token, amount = amount)
                .catch { e ->
                    addMoneyToWallet.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    addMoneyToWallet.value = addMoneyToWalletResponseHandle(data)
                }
        } else {
            addMoneyToWallet.value = Resource.Error(NO_INTERNET)
        }
    }

    private fun addMoneyToWalletResponseHandle(response: Response<CheckOutCartResponse>): Resource<CheckOutCartResponse> {
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



