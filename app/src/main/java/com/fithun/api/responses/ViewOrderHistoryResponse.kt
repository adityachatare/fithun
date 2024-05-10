package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class ViewOrderHistoryResponse (
    @SerializedName("responseMessage")val  responseMessage:String,
    @SerializedName("responseCode")val  responseCode:Int ,
    @SerializedName("result") val result: ViewOrderList = ViewOrderList()
)

class ViewOrderList(
    @SerializedName("_id")val  id:String = "",
    @SerializedName("products") val products:ArrayList<ViewProducts> = arrayListOf(),
    @SerializedName("addressId") val addressId:AddressId = AddressId(),
    @SerializedName("userId") val userId:ViewUserId = ViewUserId(),
    @SerializedName("updatedAt")val  updatedAt:String = "",
    @SerializedName("status")val  status:String = "",
    @SerializedName("total")val  total:Int = 0,
    @SerializedName("deliveryFee")val  deliveryFee:Int = 0,
    @SerializedName("totalPayableAmount")val  totalPayableAmount:Number = 0,
)
class ViewUserId(
    @SerializedName("mobileNumber")val  mobileNumber:String = "",
    @SerializedName("mobileCode")val  mobileCode:String = "",
)
class ViewProducts(
    @SerializedName("productId") val productId:ViewProductId = ViewProductId(),
    @SerializedName("quantity") val quantity:Int = 0,
    @SerializedName("awbCode") val awb:String  ="",
    @SerializedName("sizeValueId")val  sizeValueId:String = ""
    )
class ViewProductId(
    @SerializedName("_id")val  _id:String = "",
    @SerializedName("productName")val  productName:String = "",
    @SerializedName("updatedAt")val  updatedAt:String = "",
    @SerializedName("status")val  status:String = "",
    @SerializedName("productImage") val productImage:ArrayList<String> = arrayListOf(),
    @SerializedName("size_value") val size_value: ArrayList<ViewSizeValue> = arrayListOf()
)

class ViewSizeValue(
    @SerializedName("unit")val  unit:String = "",
    @SerializedName("size")val  size:String = "",
    @SerializedName("price")val  price:Int = 0,
    @SerializedName("_id")val  id:String = "",
)

class AddressId(
    @SerializedName("firstName")val  firstName:String = "",
    @SerializedName("lastName")val  lastName:String = "",
    @SerializedName("area")val  area:String = "",
    @SerializedName("city")val  city:String = "",
    @SerializedName("state")val  state:String = "",
    @SerializedName("state_code")val  state_code:String = "",
    @SerializedName("zipCode")val  zipCode:String = "",
    @SerializedName("country")val  country:String = ""
)





