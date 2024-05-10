package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class SignUPEmailOtpResponse {
    @SerializedName("result"          ) var result          : ResultSendEmail? = ResultSendEmail()
    @SerializedName("responseMessage" ) var responseMessage : String? = ""
    @SerializedName("responseCode"    ) var responseCode    : Int?    = 0

}
 class ResultSendEmail {

    @SerializedName("_id"     ) var Id      : String? = ""
    @SerializedName("email"   ) var email   : String? = ""
    @SerializedName("otp"     ) var otp     : String? = ""
    @SerializedName("otpTime" ) var otpTime : String? = ""
    @SerializedName("__v"     ) var _v      : Int?    = 0

}