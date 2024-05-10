package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class StaticDataResponse {
    @SerializedName("result") var result  : ArrayList<StaticData> = arrayListOf()
    @SerializedName("responseMessage" ) var responseMessage : String = ""
    @SerializedName("responseCode") var responseCode    : Int=0
}

 class StaticData {

    @SerializedName("_id" ) var Id : String= ""
    @SerializedName("type") var type: String= ""
    @SerializedName("title" ) var title       : String= ""
    @SerializedName("description" ) var description : String= ""
    @SerializedName("status"  ) var status      : String= ""
    @SerializedName("createdAt"  ) var createdAt   : String= ""
    @SerializedName("updatedAt") var updatedAt   : String= ""
    @SerializedName("__v" ) var _v  : Int= 0

}