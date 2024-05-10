package com.fithun.repository

import com.google.gson.JsonObject
import com.fithun.api.InterfaceImplement
import com.fithun.api.requests.*
import com.fithun.api.responses.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class DreamWalkRepository @Inject constructor(private val interfaceImplement: InterfaceImplement) {

    fun signUpAPi(signUpRequest: SignUpRequest): Flow<Response<CommonResponse>> = flow {
        emit(interfaceImplement.signUpAPi(signUpRequest = signUpRequest))
    }.flowOn(Dispatchers.IO)


    fun loginApi(loginRequest: LoginRequest): Flow<Response<LoginResponse>> = flow {
        emit(interfaceImplement.loginUser(loginRequest = loginRequest))
    }.flowOn(Dispatchers.IO)

    fun forgotPasswordApi(emailOrMobil: String): Flow<Response<ForgotResponse>> = flow {
        emit(interfaceImplement.forgotPassAPi(emailOrMobil))
    }.flowOn(Dispatchers.IO)


    fun getUserName(userName: String): Flow<Response<UserNameResponse>> = flow {
            emit(interfaceImplement.getUserName(userName))
    }.flowOn(Dispatchers.IO)


    fun otpVerifyApi(verifyOtpRequest: VerifyOtpRequest): Flow<Response<VerifyOtfResponse>> = flow {
            emit(interfaceImplement.otpVerifyApi(verifyOtpRequest = verifyOtpRequest))
    }.flowOn(Dispatchers.IO)


    fun resetPasswordApi(resetPasswordRequest: ResetPassRequest): Flow<Response<ResetPassResponse>> = flow {
            emit(interfaceImplement.resetPasswordApi(resetPasswordRequest = resetPasswordRequest))
    }.flowOn(Dispatchers.IO)


    fun changePassApi(token: String, changePassRequest: ChangePassRequest): Flow<Response<ChangePassResponse>> = flow {
        emit(interfaceImplement.changePassApi(token = token, changePassRequest = changePassRequest))
    }.flowOn(Dispatchers.IO)

    fun resendOtpAPi(emailOrMobil: String): Flow<Response<ResendOtpResponse>> = flow {
        emit(interfaceImplement.resendOtpApi(emailOrMobil))
    }.flowOn(Dispatchers.IO)

    fun sendOtpOnMobile(token: String): Flow<Response<ResendOtpResponse>> = flow {
        emit(interfaceImplement.sendOTPonMobile(token = token))
    }.flowOn(Dispatchers.IO)

    fun verifyOTPMobileEmail(token: String, otp: String): Flow<Response<ResendOtpResponse>> = flow {
        emit(interfaceImplement.verifyOTPMobileEmail(token = token, otp = otp))
    }.flowOn(Dispatchers.IO)

    fun howToWorkDetailApi(token: String): Flow<Response<HowItWorkResponse>> = flow {
        emit(interfaceImplement.howToWorkDetail(token = token))
    }.flowOn(Dispatchers.IO)


    fun profileApi(token: String, ): Flow<Response<UserProfileResponse>> = flow {
            emit(interfaceImplement.profileApi( token = token))
    }.flowOn(Dispatchers.IO)

    fun logoutApi(token: String, logoutRequest : LogOutRequest): Flow<Response<LogoutResponse>> = flow {
            emit(interfaceImplement.logoutApi(token = token, logoutRequest = logoutRequest))
    }.flowOn(Dispatchers.IO)


    fun signUpEmailOtpApi(email: String, ): Flow<Response<SignUPEmailOtpResponse>> = flow {
            emit(interfaceImplement.sendEmailMobile(email=email))
    }.flowOn(Dispatchers.IO)

    fun signMobileOtpApiData(mobile: String, ): Flow<Response<SendMobileResponse>> = flow {
        emit(interfaceImplement.sendMobile(mobile=mobile))
    }.flowOn(Dispatchers.IO)



    fun verifyEmailMobileApi(verifyEmailMobileOtpRequest: VerifyEmailMobileOtpRequest): Flow<Response<VerifyEmailMobileOtpResponse>> = flow {
        emit(interfaceImplement.verifyEmailMobileOtp(verifyEmailMobileOtpRequest = verifyEmailMobileOtpRequest))
    }.flowOn(Dispatchers.IO)



    fun verifyMobileOtp(verifyMobileRequest: VerifyMobileRequest): Flow<Response<VerifyMobileResponse>> = flow {
        emit(interfaceImplement.verifyMobileOtp(verifyMobileRequest  = verifyMobileRequest))
    }.flowOn(Dispatchers.IO)


    fun getAllBannerApi(): Flow<Response<GetAllBannerResponse>> = flow {
        emit(interfaceImplement.getAllBannerApi())
        }.flowOn(Dispatchers.IO)




    fun viewStaticData(type: String): Flow<Response<ViewStaticResponse>> = flow {
        emit(interfaceImplement.viewStaticDataApi(type))
    }.flowOn(Dispatchers.IO)

    fun editProfileApi(token: String, name: RequestBody, dob: RequestBody, gender: RequestBody, mobileNumber: RequestBody, mobileCode: RequestBody,
                       email: RequestBody, homeAddress: RequestBody, officeAddress: RequestBody, upiId: RequestBody, physicallyChallenged: RequestBody,
                       shoeSize: RequestBody, weight: RequestBody, mobileLinkedWithUpi: RequestBody,referralCode: RequestBody, file: ArrayList<MultipartBody.Part>): Flow<Response<EditProfileResponse>> = flow {
        emit(interfaceImplement.editProfileApi(token = token, name = name,dob=dob, gender=gender,mobileNumber=mobileNumber,mobileCode=mobileCode,
            email=email, homeAddress=homeAddress, officeAddress=officeAddress, upiId=upiId,physicallyChallenged=physicallyChallenged,
            shoeSize=shoeSize, weight=weight, mobileLinkedWithUpi=mobileLinkedWithUpi, file=file,referralCode=referralCode))

    }.flowOn(Dispatchers.IO)




    fun faqListApi(search:String): Flow<Response<FAQResponse>> = flow {
        emit(interfaceImplement.faqListApi(search))
    }.flowOn(Dispatchers.IO)


    fun addAddressApi(token: String, firstName:String,lastName:String,houseNumber:String, area:String,city:String,state:String,country:String, zipCode:String,stateCode:String): Flow<Response<CommonResponse>> = flow {
        emit(interfaceImplement.addAddressApi(token=token,firstName= firstName, lastName=lastName, houseNumber=houseNumber, area=area, city=city, state=state, country=country, zipCode=zipCode,stateCode=stateCode))
    }.flowOn(Dispatchers.IO)

    fun updateAddressApi(token: String, firstName:String,lastName:String,houseNumber:String, area:String,city:String,state:String,country:String, zipCode:String,id: String,stateCode: String): Flow<Response<CommonResponse>> = flow {
        emit(interfaceImplement.updateAddressApi(token=token,firstName= firstName, lastName=lastName, houseNumber=houseNumber, area=area, city=city, state=state, country=country, zipCode=zipCode,id=id,stateCode=stateCode))
    }.flowOn(Dispatchers.IO)

    fun deleteAddressApi(token: String,id: String): Flow<Response<CommonResponse>> = flow {
        emit(interfaceImplement.deleteAddressApi(token=token,id=id))
    }.flowOn(Dispatchers.IO)

    fun viewAddressApi(token: String,id: String): Flow<Response<ViewAddressResponse>> = flow {
        emit(interfaceImplement.viewAddressApi(token=token,id=id))
    }.flowOn(Dispatchers.IO)

    fun listAddress(token: String): Flow<Response<AddressListResponse>> = flow {
        emit(interfaceImplement.listAddress(token=token))
    }.flowOn(Dispatchers.IO)

    fun stateAndCityApi(code: String): Flow<Response<StateCityResponse>> = flow {
        emit(interfaceImplement.stateAndCityApi(code=code))
    }.flowOn(Dispatchers.IO)

    fun socialLogin(request: JsonObject): Flow<Response<SocialSignResponse>> = flow {
        emit(interfaceImplement.socialLogin(request=request))
    }.flowOn(Dispatchers.IO)

    fun listProductApi(token :String,page :Int,limit :Int, search :String, brandId:ArrayList<String>,priceRange:String): Flow<Response<ProductListResponse>> = flow {
        emit(interfaceImplement.listProductApi(token=token, page=page, limit=limit, search=search,brandId=brandId, priceRange = priceRange))
    }.flowOn(Dispatchers.IO)

    fun howtoPlay(page :Int,limit :Int, search :String): Flow<Response<FAQResponse>> = flow {
            emit(interfaceImplement.howtoPlay( page=page, limit=limit, search=search))
    }.flowOn(Dispatchers.IO)


    fun viewProductApi(token: String,id: String): Flow<Response<ProductViewResponse>> = flow {
        emit(interfaceImplement.viewProductApi(token=token,id=id))
    }.flowOn(Dispatchers.IO)

    fun addToCartApi(token: String,productId: String,sizeValueId: String,quantity: String): Flow<Response<CommonResponse>> = flow {
        emit(interfaceImplement.addToCartApi(token=token,productId=productId, sizeValueId=sizeValueId, quantity=quantity))
    }.flowOn(Dispatchers.IO)

    fun viewCartApi(token: String,sortByFreightCharges:Boolean,sortByEarliestDelivery:Boolean,sortByRating:Boolean): Flow<Response<CartViewResponse>> = flow {
        emit(interfaceImplement.viewCartApi(token=token,
            sortByFreightCharges = sortByFreightCharges,
            sortByEarliestDelivery = sortByEarliestDelivery, sortByRating = sortByRating))
    }.flowOn(Dispatchers.IO)

    fun addAddressForOrderApi(token: String,addressId:String): Flow<Response<CommonResponse>> = flow {
        emit(interfaceImplement.addAddressForOrderApi(token=token,addressId=addressId))
    }.flowOn(Dispatchers.IO)

    fun updateCartApi(token: String,productId:String,sizeValueId:String): Flow<Response<CommonResponse>> = flow {
        emit(interfaceImplement.updateCartApi(token=token,productId=productId,sizeValueId=sizeValueId))
    }.flowOn(Dispatchers.IO)

    fun updateQuantityApi(token: String,productId:String,sizeValueId:String,quantity:Int, quantityType: String): Flow<Response<CommonResponse>> = flow {
        emit(interfaceImplement.updateQuantityApi(token=token,productId=productId,sizeValueId=sizeValueId,quantity=quantity,quantityType=quantityType))
    }.flowOn(Dispatchers.IO)

    fun deleteItemFromCartApi(token: String,productId:String,sizeValueId:String): Flow<Response<CommonResponse>> = flow {
        emit(interfaceImplement.deleteItemFromCartApi(token=token,productId=productId,sizeValueId=sizeValueId))
    }.flowOn(Dispatchers.IO)

    fun createStepApi(token: String,stepCount:String,distanceWalked:String,unit:String, date:String, contestId: String): Flow<Response<CommonResponse>> = flow {
        emit(interfaceImplement.createStepApi(token=token,stepCount=stepCount,distanceWalked=distanceWalked,unit=unit, date = date, contestId = contestId))
    }.flowOn(Dispatchers.IO)

    fun listAllBrandApi(token: String): Flow<Response<BrandListResponse>> = flow {
        emit(interfaceImplement.listAllBrandApi(token=token))
    }.flowOn(Dispatchers.IO)

    fun viewStepApi(token: String, contestId: String, date: String): Flow<Response<ViewStepResponse>> = flow {
        emit(interfaceImplement.viewStepApi(token=token, contestId = contestId, date = date))
    }.flowOn(Dispatchers.IO)

    fun contestListApi(token :String,page :Int,limit :Int, search :String): Flow<Response<ContextListResponse>> = flow {
        emit(interfaceImplement.listContextApi(token=token, page=page, limit=limit, search=search))
    }.flowOn(Dispatchers.IO)

    fun listContestApi(token: String, page: Int, limit: Int, categoryId: String, search: String, contestListType: String,spots: String, entryFees: String): Flow<Response<ViewContestListResponse>> = flow {
        emit(interfaceImplement.listContestApi(token = token, page = page, limit = limit, categoryId = categoryId, search = search, contestListType = contestListType,spots=spots, entryFees=entryFees))
    }.flowOn(Dispatchers.IO)

    fun checkoutCartApi(token: String, paymentUrl: String): Flow<Response<CheckOutCartResponse>> = flow {
        emit(interfaceImplement.checkoutCartApi(token = token,paymentUrl=paymentUrl))
    }.flowOn(Dispatchers.IO)

    fun addMoneyToWalletApi(token: String, amount: String): Flow<Response<CheckOutCartResponse>> = flow {
        emit(interfaceImplement.addMoneyToWalletApi(token = token, amount = amount))
    }.flowOn(Dispatchers.IO)

    fun joinContestApi(token: String, contestId: String, paymentUrl: String): Flow<Response<CheckOutCartResponse>> = flow {
        emit(interfaceImplement.joinContestApi(token = token,contestId=contestId,paymentUrl=paymentUrl))
    }.flowOn(Dispatchers.IO)

    fun redeemCoinApi(token: String,amount: String,upiId:String,accountHolderName:String,mobileNumber:String): Flow<Response<CheckOutCartResponse>> = flow {
        emit(interfaceImplement.redeemCoinApi(token = token,amount=amount, upiId=upiId,accountHolderName=accountHolderName,mobileNumber=mobileNumber))
    }.flowOn(Dispatchers.IO)


    fun viewJoinedContestApi(token: String,id: String): Flow<Response<JoinedContestResponse>> = flow {
        emit(interfaceImplement.viewJoinedContestApi(token = token,id=id))
    }.flowOn(Dispatchers.IO)

    fun leaderboardListApi(token: String,contestId: String,page: Int,limit: Int): Flow<Response<LeaderBoardListResponse>> = flow {
        emit(interfaceImplement.leaderboardListApi(token = token, contestId =contestId,page =  page, limit=limit))
    }.flowOn(Dispatchers.IO)


    fun transactionListApi(token: String,page: Int, limit: Int,transactionType: String, transactionStatus: String,fromDate:String,toDate:String): Flow<Response<TransactionHistoryResponse>> = flow {
        emit(interfaceImplement.transactionListApi(token = token,page = page, limit = limit, transactionType =transactionType, transactionStatus=transactionStatus,fromDate=fromDate, toDate=toDate))
    }.flowOn(Dispatchers.IO)


    fun upcommingContest(token: String): Flow<Response<UpComingContestResponse>> = flow {
        emit(interfaceImplement.upcommingContest(token = token))
    }.flowOn(Dispatchers.IO)

    fun myContestListApi(token: String,page: Int, limit: Int): Flow<Response<CompletedContestListResponse>> = flow {
        emit(interfaceImplement.myContestListApi(token = token,page = page, limit = limit))
    }.flowOn(Dispatchers.IO)

    fun myContestWinnerListApi(token: String,contestId : String): Flow<Response<MyContestWinnerListResponse>> = flow {
        emit(interfaceImplement.myContestWinnerListApi(token = token, contestId = contestId))
    }.flowOn(Dispatchers.IO)

    fun winnerBannerList(token: String): Flow<Response<WinnerBannerListResponse>> = flow {
        emit(interfaceImplement.winnerBannerListApi(token = token))
    }.flowOn(Dispatchers.IO)

    fun orderLisApi(token: String,page: Int, limit: Int): Flow<Response<OrderHistoryResponse>> = flow {
        emit(interfaceImplement.orderList(token = token,page = page, limit = limit))
    }.flowOn(Dispatchers.IO)

    fun viewOrderApi(token: String,orderId:String): Flow<Response<ViewOrderHistoryResponse>> = flow {
        emit(interfaceImplement.viewOrder(token = token,orderId = orderId))
    }.flowOn(Dispatchers.IO)

    fun createKYCApi(token: String,name:String, panNumber:String, birthDate:String, upiId: String): Flow<Response<CommonResponse>> = flow {
        emit(interfaceImplement.createKYCApi(token = token, name = name, panNumber = panNumber, birthDate = birthDate, upiId = upiId))
    }.flowOn(Dispatchers.IO)

    fun getGraphStepApi(token: String, type: String): Flow<Response<GraphResponse>> = flow {
        emit(interfaceImplement.graphApi(token = token, type = type))
    }.flowOn(Dispatchers.IO)

    fun getKycApi(token: String): Flow<Response<KycListResponse>> = flow {
        emit(interfaceImplement.getKYCApi(token = token))
    }.flowOn(Dispatchers.IO)

    fun listNotificationApi(token: String): Flow<Response<KycListResponse>> = flow {
        emit(interfaceImplement.listNotificationApi(token = token))
    }.flowOn(Dispatchers.IO)

    fun stepsDetailsApi(token: String): Flow<Response<FundResponse>> = flow {
        emit(interfaceImplement.stepsDetailsApi(token = token))
    }.flowOn(Dispatchers.IO)

    fun listNotification(token: String): Flow<Response<NotificationResponse>> = flow {
        emit(interfaceImplement.listNotification(token = token))
    }.flowOn(Dispatchers.IO)


    fun upiVerificationApi(): Flow<Response<CommonResponse>> = flow {
        emit(interfaceImplement.upiVerificationApi())
    }.flowOn(Dispatchers.IO)


    fun viewContestPriceList(token:String,id:String,page:Int,limit:Int,priceType:String): Flow<Response<WinnerPriceResponse>> = flow {
        emit(interfaceImplement.viewContestPriceList(token, id, page, limit = limit, priceType))
    }.flowOn(Dispatchers.IO)


    fun trackShipmentApi(shipmentId:String): Flow<Response<OrderTrackingResponse>> = flow {
        emit(interfaceImplement.trackShipmentApi(shipmentId))
    }.flowOn(Dispatchers.IO)

    fun getReferCodeApi(token:String): Flow<Response<ReferAndEarnResponse>> = flow {
        emit(interfaceImplement.getReferCodeApi(token=token))
    }.flowOn(Dispatchers.IO)

    fun getUserRankOfContestApi(token:String,contestId:String): Flow<Response<ReferAndEarnResponse>> = flow {
        emit(interfaceImplement.getUserRankOfContestApi(token=token,contestId=contestId))
    }.flowOn(Dispatchers.IO)

    fun getLatestVersionApi(): Flow<Response<VersionResponse>> = flow {
        emit(interfaceImplement.getLatestVersionApi())
    }.flowOn(Dispatchers.IO)
}
