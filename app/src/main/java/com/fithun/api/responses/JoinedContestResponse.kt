package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class JoinedContestResponse (
    @SerializedName("responseMessage")val  responseMessage:String,
    @SerializedName("responseCode")val  responseCode:Int ,
    @SerializedName("result")val  result:JoinedContestListDocs
)
class JoinedContestListDocs(
    @SerializedName("_id") val id:String,
    @SerializedName("heading") val heading:String,
    @SerializedName("subHeading") val subHeading:String,
    @SerializedName("contestImage") val contestImage:String,
    @SerializedName("startDate") val startDate:String,
    @SerializedName("endDate") val endDate:String,
    @SerializedName("poolPrice") val poolPrice:Number,
    @SerializedName("entryFee") val entryFee:Number,
    @SerializedName("maximum_spot") val maximumSpot:Number,
    @SerializedName("firstPrice") val firstPrice:Number,
    @SerializedName("spot") val spot:Number,
    @SerializedName("numberOfWinner") val numberOfWinner:Number,
    @SerializedName("walletDeduction") val walletDeduction:Number,
//    @SerializedName("current_pricePool") val current_pricePool: List<ViewCurrentPriceAndExpectedPool>  = listOf(),
//    @SerializedName("expected_pricePool") val expected_pricePool: List<ViewCurrentPriceAndExpectedPool>  = listOf(),
    @SerializedName("joined") val joined: JoinedContest ,
    @SerializedName("createdAt") val createdAt:String,
    @SerializedName("updatedAt") val updatedAt:String,
)
class ViewCurrentPriceAndExpectedPool(
    @SerializedName("rank") val rank:Int,
    @SerializedName("price") val price:Number,
    @SerializedName("_id") val _id:String,
)

