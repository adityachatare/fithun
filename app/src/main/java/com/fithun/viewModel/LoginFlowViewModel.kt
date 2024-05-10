package com.fithun.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.fithun.api.Constants.NO_INTERNET
import com.fithun.api.requests.*
import com.fithun.api.responses.*
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
class LoginFlowViewModel @Inject constructor(
    app: Application,
    private val repo: DreamWalkRepository,
    private val networkHelper: NetworkHelper
) : AndroidViewModel(app) {

    private val signUpApiData: MutableStateFlow<Resource<CommonResponse>> = MutableStateFlow(Resource.Empty())
    val signUpApiResponseData: StateFlow<Resource<CommonResponse>> = signUpApiData

    private val loginApiData: MutableStateFlow<Resource<LoginResponse>> =
        MutableStateFlow(Resource.Empty())
    val loginApiResponseData: StateFlow<Resource<LoginResponse>> = loginApiData

    private val forgotPassApiData: MutableStateFlow<Resource<ForgotResponse>> =
        MutableStateFlow(Resource.Empty())
    val forgotApiResponseData: StateFlow<Resource<ForgotResponse>> = forgotPassApiData

    private val otpVerifyApiData: MutableStateFlow<Resource<VerifyOtfResponse>> = MutableStateFlow(Resource.Empty())
    val otpVerifyApiResponseData: StateFlow<Resource<VerifyOtfResponse>> = otpVerifyApiData

    private val resetApiData: MutableStateFlow<Resource<ResetPassResponse>> =
        MutableStateFlow(Resource.Empty())
    val resetApiResponseData: StateFlow<Resource<ResetPassResponse>> = resetApiData


    private val changePassApiData: MutableStateFlow<Resource<ChangePassResponse>> = MutableStateFlow(Resource.Empty())
    val changePassApiResponseData: StateFlow<Resource<ChangePassResponse>> = changePassApiData

    private val resendOtpApiData: MutableStateFlow<Resource<ResendOtpResponse>> =
        MutableStateFlow(Resource.Empty())
    val resendOtpApiResponseData: StateFlow<Resource<ResendOtpResponse>> = resendOtpApiData

    private val userNameApiData: MutableStateFlow<Resource<UserNameResponse>> =
        MutableStateFlow(Resource.Empty())
    val userApiResponseData: StateFlow<Resource<UserNameResponse>> = userNameApiData



    private val signEOtpApiData: MutableStateFlow<Resource<SignUPEmailOtpResponse>> = MutableStateFlow(Resource.Empty())
    val signEOtpResponseData: StateFlow<Resource<SignUPEmailOtpResponse>> = signEOtpApiData


    private val signMobileOtpApiData: MutableStateFlow<Resource<SendMobileResponse>> = MutableStateFlow(Resource.Empty())
    val signMobileOtpResponseData: StateFlow<Resource<SendMobileResponse>> = signMobileOtpApiData

    private val signMOtpApiData: MutableStateFlow<Resource<VerifyEmailMobileOtpResponse>> = MutableStateFlow(Resource.Empty())
    val signMOtpResponseData: StateFlow<Resource<VerifyEmailMobileOtpResponse>> = signMOtpApiData


    private val signMobileOtpVerApiData: MutableStateFlow<Resource<VerifyMobileResponse>> = MutableStateFlow(Resource.Empty())
    val signMobileOtpVerResponseData: StateFlow<Resource<VerifyMobileResponse>> = signMobileOtpVerApiData

    private val socialLoginData: MutableStateFlow<Resource<SocialSignResponse>> = MutableStateFlow(Resource.Empty())
    val socialLoginDataResponse: StateFlow<Resource<SocialSignResponse>> = socialLoginData


    // Sign Up APi
    fun signUpApi(signUpRequest: SignUpRequest) = viewModelScope.launch {
        signUpApiData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.signUpAPi(signUpRequest = signUpRequest)
                .catch { e ->
                    signUpApiData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    signUpApiData.value = signUpResponseHandle(data)
                }
        } else {
            signUpApiData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun signUpResponseHandle(response: Response<CommonResponse>): Resource<CommonResponse> {
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


//LoginUser

    fun loginApi(request: LoginRequest) = viewModelScope.launch {
        loginApiData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.loginApi(loginRequest = request)
                .catch { e ->
                    loginApiData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    loginApiData.value = loginResponseHandle(data)
                }
        } else {
            loginApiData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun loginResponseHandle(response: Response<LoginResponse>): Resource<LoginResponse> {
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

//SocialLogin
    fun socialLoginApi(request: JsonObject) = viewModelScope.launch {
        socialLoginData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()){

            repo.socialLogin(request)
                .catch { e ->
                    socialLoginData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    socialLoginData.value = socialLoginResponseHandle(data)
                }
        }else{
            socialLoginData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun socialLoginResponseHandle(response: Response<SocialSignResponse>): Resource<SocialSignResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
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






    //forgotPass
    fun forgotApi(emailOrMobile: String) = viewModelScope.launch {
        forgotPassApiData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.forgotPasswordApi(emailOrMobile)
                .catch { e ->
                    forgotPassApiData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    forgotPassApiData.value = forgotRersponseHandler(data)
                }
        } else {
            forgotPassApiData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun forgotRersponseHandler(response: Response<ForgotResponse>): Resource<ForgotResponse> {
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
   //OtpVerification

    fun otpVerifyApi(request: VerifyOtpRequest) = viewModelScope.launch {
        otpVerifyApiData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.otpVerifyApi(verifyOtpRequest = request)
                .catch { e ->
                    otpVerifyApiData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    otpVerifyApiData.value = otpverifyResponseHandle(data)
                }
        } else {
            otpVerifyApiData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun otpverifyResponseHandle(response: Response<VerifyOtfResponse>): Resource<VerifyOtfResponse> {

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
    // resetPassword


    fun resetApiData(request: ResetPassRequest) = viewModelScope.launch {
        resetApiData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.resetPasswordApi(resetPasswordRequest = request)
                .catch { e ->
                    resetApiData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    resetApiData.value = resetPasswordResponseHandle(data)
                }
        } else {
            resetApiData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun resetPasswordResponseHandle(response: Response<ResetPassResponse>): Resource<ResetPassResponse> {

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

    //changePass
    fun changePassApi(token: String, request: ChangePassRequest) = viewModelScope.launch {
        changePassApiData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.changePassApi(token = token,changePassRequest = request)
                .catch { e ->
                    changePassApiData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    changePassApiData.value = changePassResponseHandle(data)
                }
        } else {
            changePassApiData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun changePassResponseHandle(response: Response<ChangePassResponse>): Resource<ChangePassResponse> {

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
    //ResendOtp
    fun resendApi(emailOrMobile: String) = viewModelScope.launch {
        resendOtpApiData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.resendOtpAPi(emailOrMobile)
                .catch { e ->
                    resendOtpApiData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    resendOtpApiData.value = resendOtpHandler(data)
                }
        } else {
            resendOtpApiData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun resendOtpHandler(response: Response<ResendOtpResponse>): Resource<ResendOtpResponse> {
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
   //User name
    fun userNameApi(userName: String) = viewModelScope.launch {
        userNameApiData.value = Resource.Loading()

        if (networkHelper.hasInternetConnection()) {

            repo.getUserName(userName)
                .catch { e ->
                    userNameApiData.value = Resource.Error(AndroidExtension.manageMessages(e))
                }.collect { data ->
                    userNameApiData.value = ResponseHandler(data)
                }
        } else {
            userNameApiData.value = Resource.Error(NO_INTERNET)
        }

    }

    private fun ResponseHandler(response: Response<UserNameResponse>): Resource<UserNameResponse> {
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
