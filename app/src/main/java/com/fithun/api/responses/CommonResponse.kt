package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class CommonResponse (
    @SerializedName("responseCode") var statusCode:Int ,
    @SerializedName("responseMessage") var responseMessage:String
)
