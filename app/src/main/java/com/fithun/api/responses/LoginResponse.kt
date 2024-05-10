package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class LoginResponse (

    @SerializedName("result") var result: Result,
    @SerializedName("responseCode") var statusCode:Int ,
    @SerializedName("responseMessage") var responseMessage:String
)


class Result {

    @SerializedName("token"               ) var token               : String? =""
    @SerializedName("emailOrMobileNumber" ) var emailOrMobileNumber : String? = ""
    @SerializedName("_id") var id: String? = ""
    @SerializedName("userType") var userType            : String? = ""
    @SerializedName("kycDetails") val kycDetails: KycDetails = KycDetails()


}