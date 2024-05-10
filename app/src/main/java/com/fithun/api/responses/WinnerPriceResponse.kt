package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class WinnerPriceResponse (
    @SerializedName("responseMessage")val  responseMessage:String,
    @SerializedName("responseCode")val  responseCode:Int ,
    @SerializedName("result") val result: WinnerPrice
)

class WinnerPrice(
    @SerializedName("page")val  page:Int,
    @SerializedName("limit")val  limit:Int,
    @SerializedName("remainingItems")val  remainingItems:Int,
    @SerializedName("count")val  count:Int,
    @SerializedName("totalPages")val  totalPages:Int,
    @SerializedName("docs")val  docs: ArrayList<WinnerPriceData>
)
class WinnerPriceData(
    @SerializedName("rank") var rank: String,
    @SerializedName("price") var price: Number
)

