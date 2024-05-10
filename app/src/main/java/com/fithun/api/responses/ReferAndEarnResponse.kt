package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class ReferAndEarnResponse (
    @SerializedName("responseMessage")val  responseMessage:String,
    @SerializedName("responseCode")val  responseCode:Int,
    @SerializedName("result")val  result:ReferAndEarnResult = ReferAndEarnResult()
)

class ReferAndEarnResult(
    @SerializedName("usersRank") val usersRank:String = "",
    @SerializedName("referCode") val referCode:String = ""
)