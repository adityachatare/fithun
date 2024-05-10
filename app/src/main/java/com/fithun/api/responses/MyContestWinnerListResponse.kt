package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class MyContestWinnerListResponse (
    @SerializedName("responseMessage")val  responseMessage:String,
    @SerializedName("responseCode")val  responseCode:Int ,
    @SerializedName("result") val result: ArrayList<MyContestWinnerList> = arrayListOf()

)

class MyContestWinnerList{
    @SerializedName("_id") val _id:String= ""
    @SerializedName("rank") val rank:String = ""
    @SerializedName("status") val status:String= ""
    @SerializedName("createdAt") val createdAt:String= ""
    @SerializedName("updatedAt") val updatedAt:String= ""
//    @SerializedName("userId") val userIds:String = ""
    @SerializedName("userDetails") val userId:UserIdWinnerList = UserIdWinnerList()
    @SerializedName("stepDetails") val stepId:StepId = StepId()
}


class UserIdWinnerList{
    @SerializedName("_id") val _id :String = ""
    @SerializedName("userName") val userName :String = ""
    @SerializedName("profilePic") val profilePic :String = ""
}

class StepId{
    @SerializedName("stepCount") val stepCount :String = ""

}

