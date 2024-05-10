package com.fithun.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.fithun.api.Constants.NO_INTERNET
import com.fithun.api.requests.LogOutRequest
import com.fithun.api.requests.VerifyEmailMobileOtpRequest
import com.fithun.api.requests.VerifyMobileRequest
import com.fithun.api.responses.EditProfileResponse
import com.fithun.api.responses.FAQResponse
import com.fithun.api.responses.LogoutResponse
import com.fithun.api.responses.PojoClass
import com.fithun.api.responses.SendMobileResponse
import com.fithun.api.responses.SignUPEmailOtpResponse
import com.fithun.api.responses.StaticDataResponse
import com.fithun.api.responses.UserProfileResponse
import com.fithun.api.responses.VerifyEmailMobileOtpResponse
import com.fithun.api.responses.VerifyMobileResponse
import com.fithun.repository.DreamWalkRepository
import com.fithun.utils.AndroidExtension
import com.fithun.utils.NetworkHelper
import com.fithun.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    app: Application,
    private val repo: DreamWalkRepository,
    private val networkHelper: NetworkHelper
) : AndroidViewModel(app) {

    private val profileApiData: MutableStateFlow<Resource<UserProfileResponse>> = MutableStateFlow(Resource.Empty())
    val profileApiResponseData: StateFlow<Resource<UserProfileResponse>> = profileApiData


    private val staticApiData: MutableStateFlow<Resource<StaticDataResponse>> = MutableStateFlow(Resource.Empty())
    val staticApiResponseData: StateFlow<Resource<StaticDataResponse>> = staticApiData


    private val logoutApiData: MutableStateFlow<Resource<LogoutResponse>> = MutableStateFlow(Resource.Empty())
    val logoutResponseData: StateFlow<Resource<LogoutResponse>> = logoutApiData


    private val EditApiData: MutableStateFlow<Resource<EditProfileResponse>> = MutableStateFlow(Resource.Empty())
    val EditResponseData: StateFlow<Resource<EditProfileResponse>> = EditApiData


    private val faqListApiData: MutableStateFlow<Resource<FAQResponse>> = MutableStateFlow(Resource.Empty())
    val faqListApiResponseData: StateFlow<Resource<FAQResponse>> = faqListApiData


    private val signEOtpApiData: MutableStateFlow<Resource<SignUPEmailOtpResponse>> =
        MutableStateFlow(Resource.Empty())
    val signEOtpResponseData: StateFlow<Resource<SignUPEmailOtpResponse>> = signEOtpApiData


    private val signMobileOtpApiData: MutableStateFlow<Resource<SendMobileResponse>> = MutableStateFlow(Resource.Empty())
    val signMobileOtpResponseData: StateFlow<Resource<SendMobileResponse>> = signMobileOtpApiData



    private val signMobileOtpVerApiData: MutableStateFlow<Resource<VerifyMobileResponse>> = MutableStateFlow(Resource.Empty())
    val signMobileOtpVerResponseData: StateFlow<Resource<VerifyMobileResponse>> = signMobileOtpVerApiData



    private val signMOtpApiData: MutableStateFlow<Resource<VerifyEmailMobileOtpResponse>> = MutableStateFlow(Resource.Empty())
    val signMOtpResponseData: StateFlow<Resource<VerifyEmailMobileOtpResponse>> = signMOtpApiData


    //    UserProfile
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


    fun LogoutApi(token: String, request: LogOutRequest) = viewModelScope.launch {
        logoutApiData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.logoutApi(
                token = token,
                logoutRequest = request
            )
                .catch { e ->
                    logoutApiData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    logoutApiData.value = logoutResponseHandle(data)
                }
        } else {
            logoutApiData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun logoutResponseHandle(response: Response<LogoutResponse>): Resource<LogoutResponse> {

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

    fun editProfileApi(
        token: String,
        name: RequestBody,
        dob: RequestBody,
        gender: RequestBody,
        mobileNumber: RequestBody,
        mobileCode: RequestBody,
        email: RequestBody,
        homeAddress: RequestBody,
        officeAddress: RequestBody,
        upiId: RequestBody,
        physicallyChallenged: RequestBody,
        shoeSize: RequestBody,
        weight: RequestBody,
        mobileLinkedWithUpi: RequestBody,
        referralCode: RequestBody,
        file: ArrayList<MultipartBody.Part>
    ) = viewModelScope.launch {
        EditApiData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.editProfileApi(
                token = token,
                name = name,
                dob = dob,
                gender = gender,
                mobileNumber = mobileNumber,
                mobileCode = mobileCode,
                email = email,
                homeAddress = homeAddress,
                officeAddress = officeAddress,
                upiId = upiId,
                physicallyChallenged = physicallyChallenged,
                shoeSize = shoeSize,
                weight = weight,
                mobileLinkedWithUpi = mobileLinkedWithUpi,
                referralCode = referralCode,
                file = file
            )
                .catch { e ->
                    EditApiData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    EditApiData.value = editProfileApiHandler(data)
                }
        } else {
            EditApiData.value = Resource.Error(NO_INTERNET)
        }

    }


    private fun editProfileApiHandler(response: Response<EditProfileResponse>): Resource<EditProfileResponse> {

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



    // SignUpEmailMobileVerification
    fun signUpEmailOtpApi(email: String) = viewModelScope.launch {
        signEOtpApiData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.signUpEmailOtpApi(email= email)
                .catch { e ->
                    signEOtpApiData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    signEOtpApiData.value = signUpOtpResponseHandle(data)
                }
        } else {
            signEOtpApiData.value = Resource.Error(NO_INTERNET)
        }

    }


    private fun signUpOtpResponseHandle(response: Response<SignUPEmailOtpResponse>): Resource<SignUPEmailOtpResponse> {
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




    fun signUpMobileOtpApi(mobile: String) = viewModelScope.launch {
        signMobileOtpApiData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.signMobileOtpApiData(mobile= mobile)
                .catch { e ->
                    signMobileOtpApiData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    signMobileOtpApiData.value = signUpMobileOtpResponseHandle(data)
                }
        } else {
            signMobileOtpApiData.value = Resource.Error(NO_INTERNET)
        }

    }


    private fun signUpMobileOtpResponseHandle(response: Response<SendMobileResponse>): Resource<SendMobileResponse> {
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



//    VerificationSignUpEmailMobileVerification

    fun signUpVerificationOtpApi(request : VerifyEmailMobileOtpRequest) = viewModelScope.launch {
        signMOtpApiData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.verifyEmailMobileApi( verifyEmailMobileOtpRequest = request)
                .catch { e ->
                    signMOtpApiData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    signMOtpApiData.value = SignUpVerifationResponseHandle(data)
                }
        } else {
            signMOtpApiData.value = Resource.Error(NO_INTERNET)
        }

    }


    private fun SignUpVerifationResponseHandle(response: Response<VerifyEmailMobileOtpResponse>): Resource<VerifyEmailMobileOtpResponse> {
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




    fun MobileVerifationOtpApi(request : VerifyMobileRequest) = viewModelScope.launch {
        signMobileOtpVerApiData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.verifyMobileOtp( verifyMobileRequest = request)
                .catch { e ->
                    signMobileOtpVerApiData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    signMobileOtpVerApiData.value = MobileVerifationMobResponseHandle(data)
                }
        } else {
            signMobileOtpVerApiData.value = Resource.Error(NO_INTERNET)
        }

    }


    private fun MobileVerifationMobResponseHandle(response: Response<VerifyMobileResponse>): Resource<VerifyMobileResponse> {
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