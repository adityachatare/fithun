package com.fithun.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.fithun.api.Constants
import com.fithun.api.Constants.NO_INTERNET
import com.fithun.api.responses.CommonResponse
import com.fithun.api.responses.FundResponse
import com.fithun.api.responses.GetAllBannerResponse
import com.fithun.api.responses.GraphResponse
import com.fithun.api.responses.NotificationResponse
import com.fithun.api.responses.PojoClass
import com.fithun.api.responses.ProductListResponse
import com.fithun.api.responses.UpComingContestResponse
import com.fithun.api.responses.UserProfileResponse
import com.fithun.api.responses.VersionResponse
import com.fithun.api.responses.ViewStepResponse
import com.fithun.api.responses.WinnerBannerListResponse
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

class HomeFlowViewModel @Inject constructor(app:Application, private val repo:DreamWalkRepository,private val networkHelper: NetworkHelper):AndroidViewModel(app) {

    private val profileApiData: MutableStateFlow<Resource<UserProfileResponse>> = MutableStateFlow(Resource.Empty())
    val profileApiResponseData: StateFlow<Resource<UserProfileResponse>> = profileApiData

    private val addStepsData: MutableStateFlow<Resource<CommonResponse>> = MutableStateFlow(Resource.Empty())
    val addStepsResponseData: StateFlow<Resource<CommonResponse>> = addStepsData

    private val allBannerApiData: MutableStateFlow<Resource<GetAllBannerResponse>> = MutableStateFlow(Resource.Empty())
    val allBannerResponseData: StateFlow<Resource<GetAllBannerResponse>> = allBannerApiData

    private val productListData: MutableStateFlow<Resource<ProductListResponse>> = MutableStateFlow(Resource.Empty())
    val productListResponseData: StateFlow<Resource<ProductListResponse>> = productListData

    private val addToCartData: MutableStateFlow<Resource<CommonResponse>> = MutableStateFlow(Resource.Empty())
    val addToCartResponseData: StateFlow<Resource<CommonResponse>> = addToCartData

    private val viewStepsData: MutableStateFlow<Resource<ViewStepResponse>> = MutableStateFlow(Resource.Empty())
    val viewStepsResponseData: StateFlow<Resource<ViewStepResponse>> = viewStepsData

    private val upcommingContestData: MutableStateFlow<Resource<UpComingContestResponse>> = MutableStateFlow(Resource.Empty())
    val upcommingContestResponseData: StateFlow<Resource<UpComingContestResponse>> = upcommingContestData

  private val winnerBannerList: MutableStateFlow<Resource<WinnerBannerListResponse>> = MutableStateFlow(Resource.Empty())
    val winnerBannerListResponseData: StateFlow<Resource<WinnerBannerListResponse>> = winnerBannerList

  private val upiVerificationData: MutableStateFlow<Resource<CommonResponse>> = MutableStateFlow(Resource.Empty())
    val upiVerificationDataResponseData: StateFlow<Resource<CommonResponse>> = upiVerificationData

    private val getGraphApiData: MutableStateFlow<Resource<GraphResponse>> = MutableStateFlow(Resource.Empty())
    val getGraphApiResponseData: StateFlow<Resource<GraphResponse>> = getGraphApiData

    private val stepsDetailsApiData: MutableStateFlow<Resource<FundResponse>> = MutableStateFlow(Resource.Empty())
    val stepsDetailsApiResponseData: StateFlow<Resource<FundResponse>> = stepsDetailsApiData

    private val listNotificationApiData: MutableStateFlow<Resource<NotificationResponse>> = MutableStateFlow(Resource.Empty())
    val listNotificationResponseData: StateFlow<Resource<NotificationResponse>> = listNotificationApiData


    private val updateCartQtyData: MutableStateFlow<Resource<CommonResponse>> = MutableStateFlow(Resource.Empty())
    val updateCartQtyResponseData: StateFlow<Resource<CommonResponse>> = updateCartQtyData


    private val getAppVersionData: MutableStateFlow<Resource<VersionResponse>> = MutableStateFlow(Resource.Empty())
    val getAppVersionResponseData: StateFlow<Resource<VersionResponse>> = getAppVersionData








    fun stepsDetailsApi(token: String) = viewModelScope.launch {
        stepsDetailsApiData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.stepsDetailsApi(token=token)
                .catch { e ->
                    stepsDetailsApiData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    stepsDetailsApiData.value = stepsDetailsApiHandler(data)
                }
        } else {
            stepsDetailsApiData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun stepsDetailsApiHandler(response: Response<FundResponse>): Resource<FundResponse> {
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

    fun listNotificationApi(token: String) = viewModelScope.launch {
        listNotificationApiData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.listNotification(token=token)
                .catch { e ->
                    listNotificationApiData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    listNotificationApiData.value = listNotificationHandler(data)
                }
        } else {
            listNotificationApiData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun listNotificationHandler(response: Response<NotificationResponse>): Resource<NotificationResponse> {
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

    fun getGraphApi(token: String, type:String) = viewModelScope.launch {
        getGraphApiData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.getGraphStepApi(token=token, type = type)
                .catch { e ->
                    getGraphApiData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    getGraphApiData.value = getGraphApiHandler(data)
                }
        } else {
            getGraphApiData.value = Resource.Error(NO_INTERNET)
        }

    }
    private fun getGraphApiHandler(response: Response<GraphResponse>): Resource<GraphResponse> {
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

    fun userProfile(token: String) = viewModelScope.launch {
        profileApiData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.profileApi(token = token)
                .catch { e ->
                    profileApiData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    profileApiData.value = userProfileResponse(data)
                }
        } else {
            profileApiData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun userProfileResponse(response: Response<UserProfileResponse>): Resource<UserProfileResponse> {
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


    fun getBannerDataApi() = viewModelScope.launch {
        allBannerApiData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.getAllBannerApi()
                .catch { e ->
                    allBannerApiData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    allBannerApiData.value = getBannerResponse(data)
                }
        } else {
            allBannerApiData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun getBannerResponse(response: Response<GetAllBannerResponse>): Resource<GetAllBannerResponse> {
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


    // Product List Api


    fun productListApi(token :String,page :Int,limit :Int, search :String,brandId:ArrayList<String>,priceRange:String) = viewModelScope.launch {
        productListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.listProductApi(token=token, page=page, limit=limit, search=search,brandId=brandId, priceRange = priceRange)
                .catch { e ->
                    productListData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    productListData.value = getProductResponse(data)
                }
        } else {
            productListData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun getProductResponse(response: Response<ProductListResponse>): Resource<ProductListResponse> {
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

    // Add To Cart Api


    fun addToCartApi(token: String,productId: String,sizeValueId: String,quantity: String) = viewModelScope.launch {
        addToCartData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.addToCartApi(token=token,productId=productId, sizeValueId=sizeValueId, quantity=quantity)
                .catch { e ->
                    addToCartData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    addToCartData.value = addCartProductResponse(data)
                }
        } else {
            addToCartData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun addCartProductResponse(response: Response<CommonResponse>): Resource<CommonResponse> {
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

    // Add Steps
    fun addStepsApi(token: String,stepCount:String,distanceWalked:String,unit:String, date: String, contestId :String) = viewModelScope.launch {
        addStepsData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.createStepApi(token=token,stepCount=stepCount,distanceWalked=distanceWalked,unit=unit, date = date, contestId = contestId )
                .catch { e ->
                    addStepsData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    addStepsData.value = addStepsResponse(data)
                }
        } else {
            addStepsData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun addStepsResponse(response: Response<CommonResponse>): Resource<CommonResponse> {
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

    // View Steps


    fun viewStepsApi(token: String, contestId: String, date: String) = viewModelScope.launch {
        viewStepsData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.viewStepApi(token=token, contestId = contestId, date = date)
                .catch { e ->
                    viewStepsData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    viewStepsData.value = viewStepsResponse(data)
                }
        } else {
            viewStepsData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun viewStepsResponse(response: Response<ViewStepResponse>): Resource<ViewStepResponse> {
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


    fun upComingContestApi(token: String) = viewModelScope.launch {
        upcommingContestData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.upcommingContest(token = token)
                .catch { e ->
                    upcommingContestData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    upcommingContestData.value = upComingContestResponseHandle(data)
                }
        } else {
            upcommingContestData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun upComingContestResponseHandle(response: Response<UpComingContestResponse>): Resource<UpComingContestResponse> {
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

    fun winnerBannerList(token: String) = viewModelScope.launch {
        winnerBannerList.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.winnerBannerList(token=token)
                .catch { e ->
                    winnerBannerList.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    winnerBannerList.value = winnerBannerListHandleResponse(data)
                }
        } else {
            winnerBannerList.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    private fun winnerBannerListHandleResponse(response: Response<WinnerBannerListResponse>): Resource<WinnerBannerListResponse> {
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


    fun upiApi() = viewModelScope.launch {
        upiVerificationData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.upiVerificationApi()
                .catch { e ->
                    upiVerificationData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    upiVerificationData.value = upiResponseHandleResponse(data)
                }
        } else {
            upiVerificationData.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    private fun upiResponseHandleResponse(response: Response<CommonResponse>): Resource<CommonResponse> {
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


    // update Cart Api


    fun updateQuantityApi(token: String,productId:String,sizeValueId:String,quantity:Int,quantityType:String) = viewModelScope.launch {
        updateCartQtyData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.updateQuantityApi(token=token,productId=productId,sizeValueId=sizeValueId,quantity=quantity,quantityType=quantityType)
                .catch { e ->
                    updateCartQtyData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    updateCartQtyData.value = updateCartQtyResponseHandle(data)
                }
        } else {
            updateCartQtyData.value = Resource.Error(NO_INTERNET)
        }

    }


    private fun updateCartQtyResponseHandle(response: Response<CommonResponse>): Resource<CommonResponse> {
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


    // delete Cart Api


    fun getLatestVersionApi() = viewModelScope.launch {
        getAppVersionData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.getLatestVersionApi()
                .catch { e ->
                    getAppVersionData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    getAppVersionData.value = getLatestVersionResponseHandle(data)
                }
        } else {
            getAppVersionData.value = Resource.Error(NO_INTERNET)
        }

    }


    private fun getLatestVersionResponseHandle(response: Response<VersionResponse>): Resource<VersionResponse> {
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



