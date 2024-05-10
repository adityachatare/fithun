package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class ViewAddressResponse {
    @SerializedName("responseMessage")val  responseMessage:String = ""
    @SerializedName("responseCode")val  responseCode:Int = 0
    @SerializedName("result")val  result:AddressResult = AddressResult()
}