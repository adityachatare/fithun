package com.fithun.api

import com.google.gson.JsonObject
import com.fithun.api.requests.*
import com.fithun.api.responses.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class InterfaceImplement @Inject constructor(private val apiService: ApiInterface) {

    suspend fun signUpAPi(signUpRequest: SignUpRequest): Response<CommonResponse> = apiService.signUpAPi(signUpRequest = signUpRequest)

    suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse> = apiService.loginUser(loginRequest = loginRequest)

    suspend fun forgotPassAPi(emailOrMobile: String): Response<ForgotResponse> = apiService.forgotPassword(emailOrMobile)

    suspend fun getUserName(userName: String): Response<UserNameResponse> = apiService.getUserName(userName)

    suspend fun otpVerifyApi(verifyOtpRequest: VerifyOtpRequest): Response<VerifyOtfResponse> = apiService.otpVerify(verifyOtpRequest = verifyOtpRequest)

    suspend fun changePassApi(token: String, changePassRequest: ChangePassRequest): Response<ChangePassResponse> = apiService.changePass(token = token, changePassRequest = changePassRequest)

    suspend fun sendEmailMobile(email: String): Response<SignUPEmailOtpResponse> = apiService.sendEmailMobile(email = email)

    suspend fun sendMobile(mobile: String): Response<SendMobileResponse> = apiService.sendMobile(mobile = mobile)

    suspend fun verifyEmailMobileOtp(verifyEmailMobileOtpRequest: VerifyEmailMobileOtpRequest): Response<VerifyEmailMobileOtpResponse> = apiService.verifyEmailMobileOtp(verifyEmailMobileOtpRequest = verifyEmailMobileOtpRequest)

    suspend fun verifyMobileOtp(verifyMobileRequest: VerifyMobileRequest): Response<VerifyMobileResponse> = apiService.verifyMobileOtp(verifyMobileRequest = verifyMobileRequest)

    suspend fun resetPasswordApi(resetPasswordRequest: ResetPassRequest): Response<ResetPassResponse> = apiService.resetPassword(resetPasswordRequest = resetPasswordRequest)

    suspend fun resendOtpApi(emailOrMobile: String): Response<ResendOtpResponse> = apiService.resendOtpPassword(emailOrMobile)
    suspend fun sendOTPonMobile(token: String): Response<ResendOtpResponse> = apiService.sendOTPonMobile(token = token)
    suspend fun verifyOTPMobileEmail(token: String, otp : String): Response<ResendOtpResponse> = apiService.verifyOTPMobileEmail(token = token , otp = otp)

    suspend fun profileApi(token: String): Response<UserProfileResponse> = apiService.userprofile(token = token)

    suspend fun logoutApi(token: String, logoutRequest: LogOutRequest): Response<LogoutResponse> = apiService.logOut(token = token, logoutRequest = logoutRequest)

    suspend fun getAllBannerApi(): Response<GetAllBannerResponse> = apiService.getAllBanner()

    suspend fun viewStaticDataApi(type: String): Response<ViewStaticResponse> = apiService.viewStaticData(type)


    suspend fun editProfileApi(token: String, name: RequestBody, dob: RequestBody, gender: RequestBody, mobileNumber: RequestBody, mobileCode: RequestBody, email: RequestBody, homeAddress: RequestBody, officeAddress: RequestBody, upiId: RequestBody, physicallyChallenged: RequestBody, shoeSize: RequestBody, weight: RequestBody, mobileLinkedWithUpi: RequestBody, file: ArrayList<MultipartBody.Part>,referralCode:RequestBody): Response<EditProfileResponse>
    = apiService.editProfile(token = token, name = name, dob = dob, gender = gender, mobileNumber = mobileNumber, mobileCode = mobileCode, email = email, homeAddress = homeAddress, officeAddress = officeAddress, upiId = upiId, physicallyChallenged = physicallyChallenged, shoeSize = shoeSize, weight = weight, mobileLinkedWithUpi = mobileLinkedWithUpi, file = file,referralCode=referralCode)


    suspend fun faqListApi(search: String): Response<FAQResponse> = apiService.faqListApi(search)
    suspend fun socialLogin(request: JsonObject): Response<SocialSignResponse> = apiService.socialLogin(request)

    suspend fun addAddressApi(token: String, firstName: String, lastName: String, houseNumber: String, area: String, city: String, state: String, country: String, zipCode: String, stateCode: String): Response<CommonResponse>
    = apiService.addAddressApi(token = token, firstName = firstName, lastName = lastName, houseNumber = houseNumber, area = area, city = city, state = state, country = country, zipCode = zipCode, stateCode = stateCode)


    suspend fun updateAddressApi(token: String, firstName: String, lastName: String, houseNumber: String, area: String, city: String, state: String, country: String, zipCode: String, id: String, stateCode: String): Response<CommonResponse>
    = apiService.updateAddressApi(token = token, id = id,firstName = firstName, lastName = lastName, houseNumber = houseNumber, area = area, city = city, state = state, country = country, zipCode = zipCode, stateCode = stateCode)


    suspend fun deleteAddressApi(token: String, id: String): Response<CommonResponse> = apiService.deleteAddressApi(token = token, id = id)


    suspend fun viewAddressApi(token: String, id: String): Response<ViewAddressResponse> = apiService.viewAddressApi(token = token, id = id)


    suspend fun listAddress(token: String): Response<AddressListResponse> = apiService.listAddress(token = token)

    suspend fun stateAndCityApi(code: String): Response<StateCityResponse> = apiService.stateAndCityApi(code = code)

    suspend fun listProductApi(token: String, page: Int, limit: Int, search: String, brandId: ArrayList<String>, priceRange: String): Response<ProductListResponse>
    = apiService.listProductApi(token = token, page = page, limit = limit, search = search, brandId = brandId, sortByPrice = priceRange)

    suspend fun howtoPlay(page: Int, limit: Int, search: String): Response<FAQResponse> = apiService.howtoPlay(page = page, limit = limit, search = search)


    suspend fun viewProductApi(token: String, id: String): Response<ProductViewResponse> = apiService.viewProductApi(token = token, id = id)


    suspend fun addToCartApi(token: String, productId: String, sizeValueId: String, quantity: String): Response<CommonResponse> = apiService.addToCartApi(token = token, productId = productId, sizeValueId = sizeValueId, quantity = quantity)


    suspend fun viewCartApi(token: String,sortByFreightCharges:Boolean,sortByEarliestDelivery:Boolean,sortByRating:Boolean): Response<CartViewResponse> = apiService.viewCartApi(token = token,
        sortByFreightCharges = sortByFreightCharges,
        sortByEarliestDelivery = sortByEarliestDelivery, sortByRating = sortByRating
    )

    suspend fun addAddressForOrderApi(token: String, addressId: String): Response<CommonResponse> = apiService.addAddressForOrderApi(token = token, addressId = addressId)

    suspend fun updateCartApi(token: String, productId: String, sizeValueId: String): Response<CommonResponse> = apiService.updateCartApi(token = token, productId = productId, sizeValueId = sizeValueId)

    suspend fun updateQuantityApi(token: String, productId: String, sizeValueId: String, quantity: Int, quantityType: String): Response<CommonResponse> = apiService.updateQuantityApi(token = token, productId = productId, sizeValueId = sizeValueId, quantity = quantity, quantityType = quantityType)

    suspend fun deleteItemFromCartApi(token: String, productId: String, sizeValueId: String): Response<CommonResponse> = apiService.deleteItemFromCartApi(token = token, productId = productId, sizeValueId = sizeValueId)

    suspend fun createStepApi(token: String, stepCount: String, distanceWalked: String, unit: String, date:String, contestId:String): Response<CommonResponse> = apiService.createStepApi(token = token, stepCount = stepCount, distanceWalked = distanceWalked, unit = unit, data = date, contestId = contestId)

    suspend fun listAllBrandApi(token: String): Response<BrandListResponse> = apiService.listAllBrandApi(token = token)

    suspend fun viewStepApi(token: String, contestId: String, date: String): Response<ViewStepResponse> = apiService.viewStepApi(token = token, contestId = contestId, date = date)

    suspend fun listContextApi(token: String, page: Int, limit: Int, search: String): Response<ContextListResponse> = apiService.contestList(token = token, page = page, limit = limit, search = search)

    suspend fun listContestApi(token: String, page: Int, limit: Int, categoryId: String, search: String, contestListType: String, spots: String, entryFees: String): Response<ViewContestListResponse> =
        apiService.listContestApi(token = token, page = page, limit = limit, categoryId = categoryId, search = search, contestListType = contestListType,spots=spots,entryFees=entryFees)

    suspend fun checkoutCartApi(token: String, paymentUrl: String): Response<CheckOutCartResponse> = apiService.checkoutCartApi(token = token,paymentUrl=paymentUrl)
    suspend fun addMoneyToWalletApi(token: String, amount: String): Response<CheckOutCartResponse> = apiService.addMoneyToWalletApi(token = token, amount = amount)

    suspend fun joinContestApi(token: String, contestId: String, paymentUrl: String): Response<CheckOutCartResponse> = apiService.joinContestApi(token = token,contestId=contestId,paymentUrl=paymentUrl)
    suspend fun redeemCoinApi(token: String,amount: String,upiId:String,accountHolderName:String,mobileNumber:String): Response<CheckOutCartResponse> = apiService.redeemCoinApi(token = token, amount=amount, upiId=upiId,accountHolderName=accountHolderName,mobileNumber=mobileNumber)

    suspend fun viewJoinedContestApi(token: String,id: String): Response<JoinedContestResponse> = apiService.viewJoinedContestApi(token = token,id=id)

    suspend fun leaderboardListApi(token: String,contestId: String,page: Int, limit: Int): Response<LeaderBoardListResponse> = apiService.leaderboardListApi(token = token, contestId =contestId,page =  page, limit=limit)
    suspend fun transactionListApi(token: String,page: Int, limit: Int, transactionType:String, transactionStatus:String,fromDate:String,toDate:String): Response<TransactionHistoryResponse> = apiService.transactionListApi(token = token,page = page, limit = limit, transactionType =transactionType, transactionStatus=transactionStatus,fromDate=fromDate, toDate=toDate)

    suspend fun upcommingContest(token: String): Response<UpComingContestResponse> = apiService.upcommingContest(token = token)

    suspend fun myContestListApi(token: String,page: Int, limit: Int): Response<CompletedContestListResponse> = apiService.myContestListApi(token = token,page = page, limit = limit)


    suspend fun myContestWinnerListApi(token: String,contestId: String): Response<MyContestWinnerListResponse> = apiService.myContestWinnerListApi(token = token, contestId = contestId)


    suspend fun winnerBannerListApi(token: String): Response<WinnerBannerListResponse> = apiService.winnerBannerList(token = token)

    suspend fun orderList(token: String,page: Int, limit: Int): Response<OrderHistoryResponse> = apiService.orderList(token = token,page = page, limit = limit)

    suspend fun viewOrder(token: String,orderId:String): Response<ViewOrderHistoryResponse> = apiService.viewOrder(token = token,orderId = orderId)

    suspend fun createKYCApi(token: String,name:String, panNumber:String, birthDate:String, upiId: String): Response<CommonResponse> = apiService.createKYCApi(token = token, name = name, panNumber = panNumber, birthDate = birthDate, upiId = upiId)

    suspend fun graphApi(token: String, type: String): Response<GraphResponse> = apiService.graphStepApi(token = token, type = type)

    suspend fun getKYCApi(token: String): Response<KycListResponse> = apiService.getKYCApi(token = token)

    suspend fun listNotificationApi(token: String): Response<KycListResponse> = apiService.listNotificationApi(token = token)

    suspend fun stepsDetailsApi(token: String): Response<FundResponse> = apiService.stepsDetailsApi(token = token)

    suspend fun listNotification(token: String): Response<NotificationResponse> = apiService.listNotification(token = token)

    suspend fun howToWorkDetail(token: String): Response<HowItWorkResponse> = apiService.howToWorkDetails(token = token)

    suspend fun upiVerificationApi(): Response<CommonResponse> = apiService.upiVerificationApi()

    suspend fun viewContestPriceList(token:String,id:String,page:Int,limit:Int,priceType:String): Response<WinnerPriceResponse> = apiService.viewContestPriceList(token, id, page, limit, priceType)


    suspend fun trackShipmentApi(shipmentId:String): Response<OrderTrackingResponse> = apiService.trackShipmentApi(shipmentId)

    suspend fun getReferCodeApi(token:String): Response<ReferAndEarnResponse> = apiService.getReferCodeApi(token)

    suspend fun getUserRankOfContestApi(token:String,contestId:String): Response<ReferAndEarnResponse> = apiService.getUserRankOfContestApi(token,contestId)
    suspend fun getLatestVersionApi(): Response<VersionResponse> = apiService.getLatestVersionApi(deviceType = "Android")
}