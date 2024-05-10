package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class LeaderBoardListResponse(
    @SerializedName("responseMessage")val  responseMessage:String,
    @SerializedName("responseCode")val  responseCode:Int,
    @SerializedName("result")val  result:LeaderBoardListDocs = LeaderBoardListDocs()
)

class LeaderBoardListDocs{
    @SerializedName("docs") val docs :List<LeaderBoardList> = listOf()
    @SerializedName("limit") val limit :Int = 0
    @SerializedName("page") val page :Int = 0
    @SerializedName("pages") val pages :Int = 0
    @SerializedName("total") val total:Int = 0
}

class LeaderBoardList(
    @SerializedName("_id") val id:String,
    @SerializedName("userId") val userId:UserIdLeaderBoardList,
    @SerializedName("step") val step:UserSteps

)
class UserIdLeaderBoardList{
    @SerializedName("_id") val id:String = ""
    @SerializedName("name") val name:String = ""
    @SerializedName("userName") val userName:String = ""
    @SerializedName("profilePic") val profilePic:String = ""
}

class UserSteps(
    @SerializedName("_id") val id:String,
    @SerializedName("userId") val userId:String,
    @SerializedName("stepCount") val stepCount:String ,
    @SerializedName("unit") val unit:String,
)