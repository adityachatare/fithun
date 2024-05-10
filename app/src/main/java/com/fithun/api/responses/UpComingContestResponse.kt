package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class UpComingContestResponse (
    @SerializedName("responseMessage")val  responseMessage:String,
    @SerializedName("responseCode")val  responseCode:Int ,
    @SerializedName("result")val  result:UpcommingContestDocs
)

class UpcommingContestDocs(
    @SerializedName("_id") val id:String,
    @SerializedName("heading") val heading:String ? = null,
    @SerializedName("subHeading") val subHeading:String,
    @SerializedName("contestImage") val contestImage:String,
    @SerializedName("startDate") val startDate:String,
    @SerializedName("endDate") val endDate:String,
    @SerializedName("poolPrice") val poolPrice:Number,
    @SerializedName("entryFee") val entryFee:Number,
    @SerializedName("maximum_spot") val maximumSpot:Number,
    @SerializedName("firstPrice") val firstPrice:Number,
    @SerializedName("spot") val spot:Number,
    @SerializedName("categoryId") val categoryId: ViewContestListCategoryId ,
    @SerializedName("isJoined") val joined: Boolean,
    @SerializedName("createdAt") val createdAt:String,
    @SerializedName("status") val status:String,
    @SerializedName("updatedAt") val updatedAt:String,

    @SerializedName("current_pricePool") val current_pricePool: List<ExpectedPricePool>  = listOf(),
    @SerializedName("expected_pricePool") val expectedPricePool:ArrayList<ExpectedPricePool> = arrayListOf()
    )
