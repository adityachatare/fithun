package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class ViewStepResponse (
    @SerializedName("responseCode") val responseCode:Int ,
    @SerializedName("responseMessage") val responseMessage:String ,
    @SerializedName("result") val result: ViewStepResult
)

class ViewStepResult{
    @SerializedName("stepCount") val stepCount:String =""
    @SerializedName("distanceWalked") val distanceWalked:String =""
    @SerializedName("unit") val unit:String =""
}
