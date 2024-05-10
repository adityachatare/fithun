package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class SendMobileResponse {

    @SerializedName("result"          ) var result          : ResultSendM? = ResultSendM()
    @SerializedName("responseMessage" ) var responseMessage : String? = ""
    @SerializedName("responseCode"    ) var responseCode    : Int?    = 0

}

 class ResultSendM {

    @SerializedName("mobileNumber" ) var mobileNumber : String? = ""
    @SerializedName("otp"          ) var otp          : String? =""
    @SerializedName("otpTime"      ) var otpTime      : String? =""
    @SerializedName("_id"          ) var Id           : String? = ""
    @SerializedName("__v"          ) var _v           : Int?    = 0

}