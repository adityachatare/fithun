package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class OrderHistoryResponse (
    @SerializedName("responseMessage")val  responseMessage:String,
    @SerializedName("responseCode")val  responseCode:Int ,
    @SerializedName("result") val result: OrderList = OrderList()
)

class OrderList{
    @SerializedName("docs") val docs:ArrayList<Lists> = arrayListOf()
    @SerializedName("total") val total:Int = 0
    @SerializedName("limit") val limit:Int = 0
    @SerializedName("page") val page:Int = 0
    @SerializedName("pages") val pages:Int = 0
}

class Lists(
    @SerializedName("products") val products:ArrayList<Products> = arrayListOf(),
    @SerializedName("_id")val  id:String = "",
    @SerializedName("updatedAt")val  updatedAt:String = "",
    @SerializedName("status")val  status:String= ""
    )

class Products{
    @SerializedName("quantity")val  quantity:Int = 0
    @SerializedName("productId") val productId:ProductId = ProductId()
    @SerializedName("sizeValueId")val  sizeValueId:String = ""

}
class ProductId{
    @SerializedName("_id")val  _id:String = ""
    @SerializedName("productName")val  productName:String = ""
    @SerializedName("productImage") val productImage:ArrayList<String> = arrayListOf()
    @SerializedName("size_value") val size_value: ArrayList<SizeValue> = arrayListOf()
}

class SizeValue{
    @SerializedName("unit")val  unit:String = ""
    @SerializedName("size")val  size:String = ""
    @SerializedName("price")val  price:Int = 0
    @SerializedName("_id")val  id:String= ""
}





