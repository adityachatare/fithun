package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class FundResponse (
    @SerializedName("responseMessage")val  responseMessage:String,
    @SerializedName("responseCode")val  responseCode:Int,
    @SerializedName("result") val result: Fund = Fund()
)

class Fund{
    @SerializedName("last30DaysSteps")val  last30DaysSteps:Number = 0
    @SerializedName("reedeemCoin")val  reedeemCoin :Number = 0
    @SerializedName("earning")val  earning:Number = 0
    @SerializedName("isRedeemRequestToday")val  isRequested:Boolean = false


    @SerializedName("totalWining")val  totalWinings:Number = 0
    @SerializedName("totalEarn")val  totalEarn:Number = 0
    @SerializedName("wallet")val  wallet:Wallet = Wallet()
}

class Wallet{
    @SerializedName("amount")val  amount : Number = 0
    @SerializedName("totalWinings")val  totalWinings : Number = 0
}





