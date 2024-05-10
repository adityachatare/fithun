package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class VersionResponse (
    @SerializedName("result") var result: VersionResponseResult,
    @SerializedName("responseCode") var statusCode:Int ,
    @SerializedName("responseMessage") var responseMessage:String
)

class VersionResponseResult(
    @SerializedName("newVersion") var newVersion:String
)
