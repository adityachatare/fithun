package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class VerifyMobileResponse {
    @SerializedName("result"          ) var result          : ResultMobileOtp? = ResultMobileOtp()
    @SerializedName("responseMessage" ) var responseMessage : String? = ""
    @SerializedName("responseCode"    ) var responseCode    : Int?    = 0

}
class ResultMobileOtp {
    @SerializedName("verify")
    var verify: Boolean? = false

}