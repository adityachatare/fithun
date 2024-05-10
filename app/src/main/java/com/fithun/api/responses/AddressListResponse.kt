package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class AddressListResponse (
    @SerializedName("responseMessage")val  responseMessage:String,
    @SerializedName("responseCode")val  responseCode:Int,
    @SerializedName("result")val  result:ArrayList<AddressResult> = arrayListOf()
)

class AddressResult{
    @SerializedName("_id") val id:String = ""
    @SerializedName("userId") val userId:String = ""
    @SerializedName("firstName") val firstName:String = ""
    @SerializedName("lastName") val lastName:String = ""
    @SerializedName("houseNumber") val houseNumber:String = ""
    @SerializedName("area") val area:String = ""
    @SerializedName("city") val city:String = ""
    @SerializedName("state") val state:String = ""
    @SerializedName("country") val country:String = ""
    @SerializedName("state_code") val stateCode:String = ""
    @SerializedName("zipCode") val zipCode:String = ""
    @SerializedName("status") val status:String = ""
    @SerializedName("createdAt") val createdAt:String = ""
    @SerializedName("updatedAt") val updatedAt:String = ""
    var isSelected = false
}
