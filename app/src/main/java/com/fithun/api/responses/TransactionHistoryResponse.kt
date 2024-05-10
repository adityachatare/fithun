package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class TransactionHistoryResponse (
    @SerializedName("responseMessage")val  responseMessage:String,
    @SerializedName("responseCode")val  responseCode:Int ,
    @SerializedName("result")val  result:TransactionHistory = TransactionHistory()
)
class TransactionHistory{

    @SerializedName("limit")val  limit:Int = 0
    @SerializedName("page")val  page:Int = 0
    @SerializedName("pages")val  pages:Int = 0
    @SerializedName("total")val  total:Int = 0
    @SerializedName("docs") val docs: ArrayList<TransactionList>  = arrayListOf()
    @SerializedName("wallet") val wallet: WalletDetails = WalletDetails()

}

class TransactionList{
    @SerializedName("_id") val _id:String= ""
    @SerializedName("walletId") val walletId:String= ""
    @SerializedName("amount") val amount:Number= 0
    @SerializedName("receiptId") val receiptId:String= ""
    @SerializedName("transactionStatus") val transactionStatus:String= ""
    @SerializedName("txnType") val txnType:String= ""
    @SerializedName("status") val status:String= ""
    @SerializedName("createdAt") val createdAt :String= ""
    @SerializedName("updatedAt") val updatedAt :String= ""
}

class WalletDetails {
    @SerializedName("totalRewardEarned" ) var totalRewardEarned : Number = 0
    @SerializedName("totalWinings") var totalWinings: Number = 0
    @SerializedName("totalDeposits" ) var totalDeposits: Number = 0


}

