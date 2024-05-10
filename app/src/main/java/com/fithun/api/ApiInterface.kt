package com.fithun.api

import com.google.gson.JsonObject
import com.fithun.api.requests.*
import com.fithun.api.responses.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiInterface {

    @POST("user/signup")
    suspend fun signUpAPi(@Body signUpRequest: SignUpRequest): Response<CommonResponse>

    @FormUrlEncoded
    @POST("static_content/howToPlayList")
    suspend fun howtoPlay(@Field("page") page: Int, @Field("limit") limit: Int, @Field("search") search: String): Response<FAQResponse>

    @POST("user/loginUser")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("user/userName")
    suspend fun getUserName(@Query("userName") userName: String): Response<UserNameResponse>

    @GET("user/forgotPassword")
    suspend fun forgotPassword(@Query("emailOrMobile") emailOrMobile: String): Response<ForgotResponse>

    @POST("user/verifyOtp")
    suspend fun otpVerify(@Body verifyOtpRequest: VerifyOtpRequest): Response<VerifyOtfResponse>

    @POST("user/changePassword")
    suspend fun changePass(@Header("token") token: String, @Body changePassRequest: ChangePassRequest): Response<ChangePassResponse>

    @POST("user/resetPassword")
    suspend fun resetPassword(@Body resetPasswordRequest: ResetPassRequest): Response<ResetPassResponse>

    @GET("user/resendOtp")
    suspend fun resendOtpPassword(@Query("emailOrMobile") emailOrMobile: String): Response<ResendOtpResponse>

    @POST("user/verifyEmailMobileOtp")
    suspend fun verifyEmailMobileOtp(@Body verifyEmailMobileOtpRequest: VerifyEmailMobileOtpRequest): Response<VerifyEmailMobileOtpResponse>

    @POST("user/verifyEmailMobileOtp")
    suspend fun verifyMobileOtp(@Body verifyMobileRequest: VerifyMobileRequest): Response<VerifyMobileResponse>

    @GET("user/sendEmailMobile")
    suspend fun sendEmailMobile(@Query("email") email: String): Response<SignUPEmailOtpResponse>

    @GET("user/profile")
    suspend fun userprofile(@Header("token") token: String): Response<UserProfileResponse>

    @POST("user/logout")
    suspend fun logOut(
        @Header("token") token: String,
        @Body logoutRequest: LogOutRequest
    ): Response<LogoutResponse>

    @GET("static_content/viewStaticContent")
    suspend fun viewStaticData(@Query("type") type: String): Response<ViewStaticResponse>

    @Multipart
    @POST("user/editProfile")
    suspend fun editProfile(
        @Header("token") token: String,
        @Part("name")
        name: RequestBody,
        @Part("dob")
        dob: RequestBody,
        @Part("gender")
        gender: RequestBody,
        @Part("mobileNumber")
        mobileNumber: RequestBody,
        @Part("mobileCode")
        mobileCode: RequestBody,
        @Part("email")
        email: RequestBody,
        @Part("homeAddress")
        homeAddress: RequestBody,
        @Part("officeAddress")
        officeAddress: RequestBody,
        @Part("upiId")
        upiId: RequestBody,
        @Part("physicallyChallenged")
        physicallyChallenged: RequestBody,
        @Part("shoeSize")
        shoeSize: RequestBody,
        @Part("weight")
        weight: RequestBody,
        @Part("mobileLinkedWithUpi")
        mobileLinkedWithUpi: RequestBody,
        @Part
        file: ArrayList<MultipartBody.Part>,
        @Part("referId")
        referralCode: RequestBody
    ): Response<EditProfileResponse>

    @POST("faq/faqList")
    suspend fun faqListApi(@Query("search") search: String): Response<FAQResponse>

    @GET("user/sendEmailMobile")
    suspend fun sendMobile(@Query("mobileNumber") mobile: String): Response<SendMobileResponse>

    @POST("user/socialSignIn")
    suspend fun socialLogin(@Body request: JsonObject): Response<SocialSignResponse>

    @GET("admin/getAllBannners")
    suspend fun getAllBanner(): Response<GetAllBannerResponse>

    @DELETE("address/deleteAddress")
    suspend fun deleteAddressApi(@Header("token") token: String, @Query("_id") id: String): Response<CommonResponse>

    @GET("address/viewAddress")
    suspend fun viewAddressApi(@Header("token") token: String, @Query("_id") id: String): Response<ViewAddressResponse>

    @GET("address/listAddress")
    suspend fun listAddress(@Header("token") token: String): Response<AddressListResponse>

    @FormUrlEncoded
    @POST("address/addAddress")
    suspend fun addAddressApi(
        @Header("token") token: String,
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String,
        @Field("houseNumber") houseNumber: String,
        @Field("area") area: String,
        @Field("city") city: String,
        @Field("state") state: String,
        @Field("country") country: String,
        @Field("zipCode") zipCode: String,
        @Field("state_code") stateCode: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @PUT("address/editAddress")
    suspend fun updateAddressApi(
        @Header("token") token: String,
        @Field("_id") id: String,
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String,
        @Field("houseNumber") houseNumber: String,
        @Field("area") area: String,
        @Field("city") city: String,
        @Field("state") state: String,
        @Field("country") country: String,
        @Field("zipCode") zipCode: String,
        @Field("state_code") stateCode: String
    ): Response<CommonResponse>

    @GET("user/stateAndCity")
    suspend fun stateAndCityApi(@Query("state_code") code: String): Response<StateCityResponse>

    @FormUrlEncoded
    @POST("product/listProduct")
    suspend fun listProductApi(@Header("token") token: String, @Field("page") page: Int, @Field("limit") limit: Int, @Field("search") search: String, @Field("brandIdArray") brandId: ArrayList<String>, @Field("sortByPrice") sortByPrice: String): Response<ProductListResponse>

    @GET("product/viewProduct")
    suspend fun viewProductApi(@Header("token") token: String, @Query("productId") id: String): Response<ProductViewResponse>

    @FormUrlEncoded
    @POST("cart/addToCart")
    suspend fun addToCartApi(@Header("token") token: String, @Field("productId") productId: String, @Field("sizeValueId") sizeValueId: String, @Field("quantity") quantity: String): Response<CommonResponse>

    @GET("cart/viewCart")
    suspend fun viewCartApi(@Header("token") token: String, @Query("sortByFreightCharges")sortByFreightCharges:Boolean, @Query("sortByEarliestDelivery")sortByEarliestDelivery:Boolean, @Query("sortByRating")sortByRating:Boolean): Response<CartViewResponse>

    @GET("cart/addAddress")
    suspend fun addAddressForOrderApi(@Header("token") token: String, @Query("addressId") addressId: String): Response<CommonResponse>

    @FormUrlEncoded
    @PUT("cart/updateSize")
    suspend fun updateCartApi(@Header("token") token: String, @Field("productId") productId: String, @Field("sizeValueId") sizeValueId: String): Response<CommonResponse>

    @FormUrlEncoded
    @PUT("cart/updateQuantity")
    suspend fun updateQuantityApi(@Header("token") token: String, @Field("productId") productId: String, @Field("sizeValueId") sizeValueId: String, @Field("quantity") quantity: Int, @Field("quantityType") quantityType: String): Response<CommonResponse>

    @FormUrlEncoded
    @POST("cart/deleteItemFromCart")
    suspend fun deleteItemFromCartApi(@Header("token") token: String, @Field("productId") productId: String, @Field("sizeValueId") sizeValueId: String): Response<CommonResponse>

    @FormUrlEncoded
    @POST("step/createStep")
    suspend fun createStepApi(@Header("token") token: String, @Field("stepCount") stepCount: String, @Field("distanceWalked") distanceWalked: String, @Field("unit") unit: String, @Field("date") data: String, @Field("contestId") contestId: String): Response<CommonResponse>

    @GET("brand/listAllBrand")
    suspend fun listAllBrandApi(@Header("token") token: String): Response<BrandListResponse>

    @GET("step/viewStep")
    suspend fun viewStepApi(@Header("token") token: String, @Query("contestId") contestId: String, @Query("date") date: String): Response<ViewStepResponse>

    @FormUrlEncoded
    @POST("contest/listContestCategory")
    suspend fun contestList(@Header("token") token: String, @Field("page") page: Int, @Field("limit") limit: Int, @Field("search") search: String ): Response<ContextListResponse>

    @FormUrlEncoded
    @POST("contest/listContest")
    suspend fun listContestApi(@Header("token")token:String,@Field("page")page:Int,@Field("limit")limit:Int, @Field("categoryId")categoryId:String,@Field("search")search:String,@Field("contestListType")contestListType:String, @Field("spots")spots:String,@Field("entryFees")entryFees:String):Response<ViewContestListResponse>

    @FormUrlEncoded
    @POST("payment/checkoutCart")
    suspend fun checkoutCartApi(@Header("token")token:String, @Field("successUrl")paymentUrl:String):Response<CheckOutCartResponse>


    @FormUrlEncoded
    @POST("payment/addMoneyToWallet")
    suspend fun addMoneyToWalletApi(@Header("token")token:String, @Field("amount")amount :String):Response<CheckOutCartResponse>
    @FormUrlEncoded
    @POST("payment/checkoutContest")
    suspend fun joinContestApi(@Header("token")token:String,@Field("contestId")contestId :String, @Field("successUrl")paymentUrl:String):Response<CheckOutCartResponse>

    @FormUrlEncoded
    @POST("payment/redeemCoin")
    suspend fun redeemCoinApi(@Header("token")token:String,@Field("amount")amount :String, @Field("upiId")upiId :String,@Field("accountHolderName")accountHolderName:String, @Field("mobileNumber")mobileNumber:String):Response<CheckOutCartResponse>

    @GET("contest/viewContest")
    suspend fun viewJoinedContestApi(@Header("token")token:String,@Query("_id")id :String,):Response<JoinedContestResponse>

    @FormUrlEncoded
    @POST("contest/leaderboardList")
    suspend fun leaderboardListApi(@Header("token")token:String,@Field("contestId")contestId :String,@Field("page")page :Int,@Field("limit")limit :Int ):Response<LeaderBoardListResponse>

    @GET("contest/upcommingContest")
    suspend fun upcommingContest(@Header("token") token: String): Response<UpComingContestResponse>

    @FormUrlEncoded
    @POST("transaction/transactionList")
    suspend fun transactionListApi(@Header("token")token:String,@Field("page")page :Int,@Field("limit")limit :Int
                                   ,@Field("transactionType")transactionType :String,
                                   @Field("transactionStatus")transactionStatus:String ,
                                   @Field("fromDate")fromDate:String,
                                   @Field("toDate")toDate:String):Response<TransactionHistoryResponse>

    @GET("user/sendOTPonMobile")
    suspend fun sendOTPonMobile(@Header("token")token:String): Response<ResendOtpResponse>

    @GET("user/verifyOTPmobileEmail")
    suspend fun verifyOTPMobileEmail(@Header("token")token:String, @Query("otp") otp: String): Response<ResendOtpResponse>

    @FormUrlEncoded
    @POST("contest/myContestList")
    suspend fun myContestListApi(@Header("token")token:String,@Field("page")page :Int,@Field("limit")limit :Int ):Response<CompletedContestListResponse>

    @FormUrlEncoded
    @POST("contest/myContestWinnerList")
    suspend fun myContestWinnerListApi(@Header("token")token:String,@Field("contestId")contestId :String):Response<MyContestWinnerListResponse>

    @POST("contest/winnerBannerList")
    suspend fun winnerBannerList(@Header("token")token:String):Response<WinnerBannerListResponse>

    @GET("howToWork/detail")
    suspend fun howToWorkDetails(@Header("token") token: String): Response<HowItWorkResponse>

    @POST("bank-verification/upi-verification")
    suspend fun upiVerificationApi(): Response<CommonResponse>
    @FormUrlEncoded
    @POST("order/orderList")
    suspend fun orderList(@Header("token")token:String,@Field("page")page :Int,@Field("limit")limit :Int ): Response<OrderHistoryResponse>

    @FormUrlEncoded
    @POST("order/viewOrder")
    suspend fun viewOrder(@Header("token")token:String,@Field("orderId")orderId :String): Response<ViewOrderHistoryResponse>

    @FormUrlEncoded
    @POST("kyc/createKYC")
    suspend fun createKYCApi(@Header("token")token:String,@Field("name")name :String, @Field("panNumber")panNumber :String, @Field("birthDate")birthDate :String,  @Field("upiId")upiId :String): Response<CommonResponse>

    @FormUrlEncoded
    @POST("step/graphStep")
    suspend fun graphStepApi(@Header("token")token:String, @Field("type")type: String): Response<GraphResponse>

    @GET("kyc/getKYC")
    suspend fun getKYCApi(@Header("token") token: String): Response<KycListResponse>

    @GET("notification/listNotification")
    suspend fun listNotificationApi(@Header("token") token: String): Response<KycListResponse>

    @GET("step/fundData")
    suspend fun stepsDetailsApi(@Header("token") token: String): Response<FundResponse>

    @GET("notification/listNotification")
    suspend fun listNotification(@Header("token") token: String): Response<NotificationResponse>


    @FormUrlEncoded
    @POST("contest/viewContestPriceList")
    suspend fun viewContestPriceList(@Header("token")token:String,@Field("_id")id:String,@Field("page")page:Int,@Field("limit")limit:Int,@Field("priceType")priceType:String):Response<WinnerPriceResponse>


    @FormUrlEncoded
    @POST("payment/trackShipment")
    suspend fun trackShipmentApi(@Field("awbCode")shipmentId:String):Response<OrderTrackingResponse>

    @GET("user/getReferCode")
    suspend fun getReferCodeApi(@Header("token")token:String):Response<ReferAndEarnResponse>

    @GET("contest/getUserRankOfContest")
    suspend fun getUserRankOfContestApi(@Header("token")token:String,@Query("contestId")contestId:String):Response<ReferAndEarnResponse>


    @GET("admin/getLatestVersion")
    suspend fun getLatestVersionApi(@Query("deviceType")deviceType:String):Response<VersionResponse>



}


