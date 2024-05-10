package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class StateCityResponse {
    @SerializedName("responseMessage")val  responseMessage:String = ""
    @SerializedName("responseCode")val  responseCode:Int = 0
    @SerializedName("result")val  result:List<StateCityResult> = listOf()
}

class  StateCityResult{
    @SerializedName("name")val  name:String = ""
    @SerializedName("state_code")val  stateCode:String = ""
}