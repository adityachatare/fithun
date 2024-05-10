package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class VerifyEmailMobileOtpResponse {
    @SerializedName("result"          ) var result          : ResultEmailMobileOtp? = ResultEmailMobileOtp()
    @SerializedName("responseMessage" ) var responseMessage : String? = ""
    @SerializedName("responseCode"    ) var responseCode    : Int?    = 0

}
 class ResultEmailMobileOtp {
    @SerializedName("verify")
    var verify: Boolean? = false

}