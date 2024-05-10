package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class CheckOutCartResponse(

    @SerializedName("responseCode") var responseCode:Int,
    @SerializedName("responseMessage") var responseMessage:String,
    @SerializedName("result") var resultCheckOutCart:ResultCheckOutCart
)

class ResultCheckOutCart(
    @SerializedName("orderId") var orderId:String
)