package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class UserNameResponse {

    @SerializedName("result"          ) var result          : ResultUser? = ResultUser()
    @SerializedName("responseMessage" ) var responseMessage : String? = ""
    @SerializedName("responseCode"    ) var responseCode    : Int?    = 0

}
 class ResultUser {

    @SerializedName("isValid" ) var isValid : Boolean? = false

}