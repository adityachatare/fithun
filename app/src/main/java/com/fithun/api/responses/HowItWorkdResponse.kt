package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class HowItWorkResponse (
    @SerializedName("responseMessage")val  responseMessage:String,
    @SerializedName("responseCode")val  responseCode:Int ,
    @SerializedName("result") val result: HowItWorks = HowItWorks()

)

class HowItWorks{
    @SerializedName("mediaUrl") val mediaUrl:String= ""
    @SerializedName("steps") val steps:ArrayList<Steps> = arrayListOf()
    @SerializedName("friendAction") val friendAction:ArrayList<FriendAction> = arrayListOf()
    @SerializedName("status") val status:String= ""
    @SerializedName("createdAt") val createdAt:String= ""
    @SerializedName("updatedAt") val updatedAt:String= ""
    @SerializedName("userId") val userId:UserIdWinnerList = UserIdWinnerList()
}

class Steps{
    @SerializedName("heading") val heading:String= ""
    @SerializedName("subHeading") val subHeading:String= ""
}

class FriendAction{
    @SerializedName("rewardedAmount") val rewardedAmount:Int= 0
    @SerializedName("referTo") val referTo:ReferTo= ReferTo()
}

class ReferTo{
    @SerializedName("userName") val userName:String= ""

}



