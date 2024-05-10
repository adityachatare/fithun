package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class CartViewResponse (
    @SerializedName("responseMessage")val  responseMessage:String,
    @SerializedName("responseCode")val  responseCode:Int,
    @SerializedName("result")val  result:CartViewResult = CartViewResult()
)


class CartViewResult{
    @SerializedName("addressId") val addressId:AddressResult = AddressResult()
    @SerializedName("createdAt") val createdAt:String = ""
    @SerializedName("products") val products:ArrayList<CartViewProducts> = arrayListOf()
    @SerializedName("totalAmount") val totalAmount:String = ""
    @SerializedName("walletDeduction") val walletDeduction:Number = 0
    @SerializedName("finalAmount") val finalAmount:Number = 0
    @SerializedName("userId") val userId:String = ""
    @SerializedName("status") val status:String = ""
    @SerializedName("priceDetail") val priceDetail:PriceDetailData = PriceDetailData()

}

class CartViewProducts{
    @SerializedName("etd") val etd:String = ""
    @SerializedName("sizeValueId") val sizeValueId:String = ""
    @SerializedName("quantity")
    var quantity:Int = 0
    @SerializedName("productId") val productId:ProductDetails = ProductDetails()

}



class ProductDetails{
    @SerializedName("_id")val  id:String = ""
    @SerializedName("status")val  status:String = ""
    @SerializedName("productName")val  productName:String = ""
    @SerializedName("productDescription")val  productDescription:String = ""
    @SerializedName("size_value")val  sizeValue:List<SizeValueData> = listOf()
    @SerializedName("productImage")val  productImage:List<String> = listOf()
    @SerializedName("createdAt") val createdAt:String = ""

}

class PriceDetailData{
    @SerializedName("subTotal") val subTotal:Number= 0
    @SerializedName("stepsToRedeem") val stepsToRedeem:Number= 0
    @SerializedName("total") val total:Number= 0
    @SerializedName("shippingCharge") val shippingCharge:Number= 0
    @SerializedName("grandTotal") val grandTotal:Number= 0
}