package com.fithun.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.fithun.api.Constants
import com.fithun.api.responses.CheckOutCartResponse
import com.fithun.api.responses.CompletedContestListResponse
import com.fithun.api.responses.ContextListResponse
import com.fithun.api.responses.JoinedContestResponse
import com.fithun.api.responses.LeaderBoardListResponse
import com.fithun.api.responses.MyContestWinnerListResponse
import com.fithun.api.responses.PojoClass
import com.fithun.api.responses.ReferAndEarnResponse
import com.fithun.api.responses.ViewContestListResponse
import com.fithun.api.responses.WinnerPriceResponse
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
class ContestViewModel @Inject constructor(app: Application, private val repo: DreamWalkRepository, private val networkHelper: NetworkHelper): AndroidViewModel(app) {

    private val contestCatListData: MutableStateFlow<Resource<ContextListResponse>> = MutableStateFlow(Resource.Empty())
    val contestCatListApiResponseData: StateFlow<Resource<ContextListResponse>> = contestCatListData


    private val contestListData: MutableStateFlow<Resource<ViewContestListResponse>> = MutableStateFlow(Resource.Empty())
    val contestListResponseData: StateFlow<Resource<ViewContestListResponse>> = contestListData


    private val checkOutData: MutableStateFlow<Resource<CheckOutCartResponse>> = MutableStateFlow(Resource.Empty())
    val checkOutResponseData: StateFlow<Resource<CheckOutCartResponse>> = checkOutData


    private val joinedContestData: MutableStateFlow<Resource<JoinedContestResponse>> = MutableStateFlow(Resource.Empty())
    val joinedContestResponseData: StateFlow<Resource<JoinedContestResponse>> = joinedContestData


    private val leaderBoardData: MutableStateFlow<Resource<LeaderBoardListResponse>> = MutableStateFlow(Resource.Empty())
    val leaderBoardResponseData: StateFlow<Resource<LeaderBoardListResponse>> = leaderBoardData


    private val myContestListData: MutableStateFlow<Resource<CompletedContestListResponse>> = MutableStateFlow(Resource.Empty())
    val  myContestListResponseData: StateFlow<Resource<CompletedContestListResponse>> = myContestListData

    private val myContestWinnerListData: MutableStateFlow<Resource<MyContestWinnerListResponse>> = MutableStateFlow(Resource.Empty())
    val  myContestWinnerListResponseData: StateFlow<Resource<MyContestWinnerListResponse>> = myContestWinnerListData

    private val priceWinnerListData: MutableStateFlow<Resource<WinnerPriceResponse>> = MutableStateFlow(Resource.Empty())
    val  priceWinnerListResponseData: StateFlow<Resource<WinnerPriceResponse>> = priceWinnerListData

    private val myRankData: MutableStateFlow<Resource<ReferAndEarnResponse>> = MutableStateFlow(Resource.Empty())
    val  myRankDataResponseData: StateFlow<Resource<ReferAndEarnResponse>> = myRankData


//    Category List Response

    fun contestCategoryListApi(token:String, page :Int, limit :Int, search :String) = viewModelScope.launch {
        contestCatListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.contestListApi( token= token ,page=page, limit=limit, search=search)
                .catch { e ->
                    contestCatListData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    contestCatListData.value = contestCategoryListResponse(data)
                }
        } else {
            contestCatListData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun contestCategoryListResponse(response: Response<ContextListResponse>): Resource<ContextListResponse> {
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


//     Contest List Response

    fun contestListApi(token: String, page: Int, limit: Int, categoryId: String, search: String, contestListType: String,spots: String, entryFees: String) = viewModelScope.launch {
        contestListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.listContestApi(token = token, page = page, limit = limit, categoryId = categoryId, search = search, contestListType = contestListType,spots=spots, entryFees=entryFees)
                .catch { e ->
                    contestListData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    print("Else")

                    contestListData.value = contestListResponseHandle(data)
//                    print(contestListResponseHandle(data))
                }
        } else {
            contestListData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun contestListResponseHandle(response: Response<ViewContestListResponse>): Resource<ViewContestListResponse> {
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

    fun joinContestApi(token: String,contestId: String,paymentUrl:String) = viewModelScope.launch {
        checkOutData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.joinContestApi(token=token,contestId=contestId,paymentUrl=paymentUrl)
                .catch { e ->
                    checkOutData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    checkOutData.value = joinContestResponseHandle(data)
                }
        } else {
            checkOutData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }


    private fun joinContestResponseHandle(response: Response<CheckOutCartResponse>): Resource<CheckOutCartResponse> {
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




//   View contest for join

    fun joinContestViewApi(token: String,id: String) = viewModelScope.launch {
        joinedContestData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.viewJoinedContestApi(token=token,id=id)
                .catch { e ->
                    joinedContestData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    joinedContestData.value = joinContestViewResponseHandle(data)
                }
        } else {
            joinedContestData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }


    private fun joinContestViewResponseHandle(response: Response<JoinedContestResponse>): Resource<JoinedContestResponse> {
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


//    Leader board list api
    fun leaderboardLUserListApi(token: String,contestId: String,page: Int,limit: Int) = viewModelScope.launch {
        leaderBoardData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.leaderboardListApi(token = token, contestId =contestId,page =  page, limit=limit)
                .catch { e ->
                    leaderBoardData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    leaderBoardData.value = leaderboardLUserListResponseHandle(data)
                }
        } else {
            leaderBoardData.value = Resource.Error(Constants.NO_INTERNET)
        }

    }

    private fun leaderboardLUserListResponseHandle(response: Response<LeaderBoardListResponse>): Resource<LeaderBoardListResponse> {
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
    fun myContestListApi(token: String, page:Int
                        ,limit:Int) = viewModelScope.launch {
        myContestListData.value = Resource.Loading()
        println("In mycontest")
        if (networkHelper.hasInternetConnection()) {

            repo.myContestListApi(token=token,page= page,
                limit=limit)
                .catch { e ->
                    myContestListData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    println("Received data: $data")
                    myContestListData.value = myContestListHandleResponse(data)
                }
        } else {
            myContestListData.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    private fun myContestListHandleResponse(response: Response<CompletedContestListResponse>): Resource<CompletedContestListResponse> {
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

    fun myContestWinnerListApi(token: String,contestId:String) = viewModelScope.launch {
        myContestWinnerListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.myContestWinnerListApi(token=token, contestId = contestId)
                .catch { e ->
                    myContestWinnerListData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    myContestWinnerListData.value = myContestWinneListHandleResponse(data)
                }
        } else {
            myContestWinnerListData.value = Resource.Error(Constants.NO_INTERNET)
        }
    }

    private fun myContestWinneListHandleResponse(response: Response<MyContestWinnerListResponse>): Resource<MyContestWinnerListResponse> {
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

//     Price Distribution Api

    fun priceWinnerListApi(token:String,id:String,page:Int,limit:Int,priceType:String) = viewModelScope.launch {
        priceWinnerListData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.viewContestPriceList(token, id, page, limit, priceType)
                .catch { e ->
                    priceWinnerListData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    priceWinnerListData.value = priceWinneListHandleResponse(data)
                }
        } else {
            priceWinnerListData.value = Resource.Error(Constants.NO_INTERNET)
        }
    }
    private fun priceWinneListHandleResponse(response: Response<WinnerPriceResponse>): Resource<WinnerPriceResponse> {
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



//     My Rank Api

    fun myRankApi(token:String,id:String) = viewModelScope.launch {
        myRankData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.getUserRankOfContestApi(token, id)
                .catch { e ->
                    myRankData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    myRankData.value = getUserRankOfContestResponse(data)
                }
        } else {
            myRankData.value = Resource.Error(Constants.NO_INTERNET)
        }
    }
    private fun getUserRankOfContestResponse(response: Response<ReferAndEarnResponse>): Resource<ReferAndEarnResponse> {
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