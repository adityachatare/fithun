package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class WinnerBannerListResponse (
    @SerializedName("responseMessage")val  responseMessage:String,
    @SerializedName("responseCode")val  responseCode:Int ,
    @SerializedName("result") val result: ArrayList<WinnerBannerList> = arrayListOf()

)

class WinnerBannerList{
    @SerializedName("winnerList") val winnerList:ArrayList<WinnerList> = arrayListOf()
}


class WinnerList{
    @SerializedName("_id") val _id :_id = _id()
    @SerializedName("totalSteps") val totalSteps :Int = 0
}

class _id{
    @SerializedName("userName") val userName:String= ""
    @SerializedName("profilePic") val profilePic :String= ""
}

