package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class CompletedContestListResponse (
    @SerializedName("responseMessage")val  responseMessage:String,
    @SerializedName("responseCode")val  responseCode:Int ,
    @SerializedName("result")val  result:CompletedContestList = CompletedContestList()
)

class CompletedContestList{
    @SerializedName("docs") val docs: ArrayList<CompletedContest> = arrayListOf()
    @SerializedName("limit")val  limit:Int = 0
    @SerializedName("page")val  page:Int = 0
    @SerializedName("pages")val  pages:Int = 0
    @SerializedName("total")val  total:Int = 0
}

class CompletedContest{
    @SerializedName("_id") val id:String= ""
    @SerializedName("contest") val contest:ContestId = ContestId()
    @SerializedName("contestId") val contestId:String= ""
    @SerializedName("winner") val winner:Winner = Winner()
    @SerializedName("userId") val userId:Number= 0
    @SerializedName("status") val status:String= ""
    @SerializedName("createdAt") val createdAt:String= ""
    @SerializedName("updatedAt") val updatedAt:String= ""

}

class ContestId{
    @SerializedName("_id") val _id :String = ""
    @SerializedName("startDate") val startDate :String = ""
    @SerializedName("numberOfWinner") val numberOfWinner :Number= 0
    @SerializedName("endDate") val endDate :String = ""
    @SerializedName("heading") val heading :String = ""
    @SerializedName("status") val status :String = ""
    @SerializedName("subHeading") val subHeading :String = ""
    @SerializedName("current_pricePool") val currentPricePool: ArrayList<CurrentPricePool> = arrayListOf()
    @SerializedName("spot") val spot:Number = 0
    @SerializedName("maximum_spot") val maximum_spot:Number = 0
    @SerializedName("entryFee") val entryFee:Number= 0

}


class Winner{
    @SerializedName("rank") val rank:Number = 0
}
class CurrentPricePool{
    @SerializedName("price") val price:Number = 0
}
