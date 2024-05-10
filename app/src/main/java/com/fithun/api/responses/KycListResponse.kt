package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class KycListResponse (
    @SerializedName("responseMessage")val  responseMessage:String,
    @SerializedName("responseCode")val  responseCode:Int,
    @SerializedName("result") val result: KycList = KycList()
)

class KycList{
    @SerializedName("panNumber")val  panNumber:String = ""
    @SerializedName("upiId")val  upiId:String = ""
}





