package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class ViewContestListResponse (
    @SerializedName("responseMessage")val  responseMessage:String,
    @SerializedName("responseCode")val  responseCode:Int ,
    @SerializedName("result")val  result:ViewContestListResult = ViewContestListResult()
)

class ViewContestListResult{
    @SerializedName("docs") val docs: ArrayList<ViewContestListDocs> = arrayListOf()
    @SerializedName("total") val total:Int = 0
    @SerializedName("limit") val limit:Int = 0
    @SerializedName("page") val page:Int = 0
    @SerializedName("pages") val pages:Int = 0
}

class ViewContestListDocs(
    @SerializedName("_id") val id:String,
    @SerializedName("heading") val heading:String,
    @SerializedName("subHeading") val subHeading:String,
    @SerializedName("contestImage") val contestImage:String,
    @SerializedName("startDate") val startDate:String,
    @SerializedName("endDate") val endDate:String,
    @SerializedName("poolPrice") val poolPrice:Number,
    @SerializedName("entryFee") val entryFee:Number,
    @SerializedName("status") val status:String,
    @SerializedName("isExpire") val isExpire:Boolean,
    @SerializedName("maximum_spot") val maximumSpot:Number,
    @SerializedName("firstPrice") val firstPrice:Number,
    @SerializedName("spot") val spot:Number,
    @SerializedName("categoryId") val categoryId: ViewContestListCategoryId ,
    @SerializedName("joined") val joined: JoinedContest?=null,
    @SerializedName("createdAt") val createdAt:String,
    @SerializedName("updatedAt") val updatedAt:String,
    @SerializedName("current_pricePool") val current_pricePool: List<ExpectedPricePool>  = listOf(),
    @SerializedName("expected_pricePool") val expectedPricePool:ArrayList<ExpectedPricePool> = arrayListOf()
)


class ExpectedPricePool(
    @SerializedName("price") val price:Number
)

class ViewContestListCategoryId(
    @SerializedName("_id") val id:String,
    @SerializedName("description") val description:String,
    @SerializedName("heading") val heading:String,
    @SerializedName("status") val status:String,
    @SerializedName("subHeading") val subHeading:String,
    @SerializedName("createdAt") val createdAt:String,
    @SerializedName("updatedAt") val updatedAt:String,
)


class JoinedContest(
    @SerializedName("_id") val id:String
)

