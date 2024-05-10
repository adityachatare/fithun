package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class VerifyOtfResponse {
    @SerializedName("result"          ) var result          : Result? = Result()
    @SerializedName("responseMessage" ) var responseMessage : String? = ""
    @SerializedName("responseCode"    ) var responseCode    : Int?    = 0

}

 class Resultss {

    @SerializedName("_id"           ) var Id            : String? = ""
    @SerializedName("emailOrMobile" ) var emailOrMobile : String? = ""
    @SerializedName("token"         ) var token         : String? = ""


}