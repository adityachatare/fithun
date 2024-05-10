package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class ProductListResponse (
    @SerializedName("responseMessage")val  responseMessage:String,
    @SerializedName("responseCode")val  responseCode:Int ,
    @SerializedName("result")val  result:ProductListResult = ProductListResult()
)


class ProductListResult{
    @SerializedName("limit")val  limit:Int = 0
    @SerializedName("page")val  page:Int = 0
    @SerializedName("pages")val  pages:Int = 0
    @SerializedName("total")val  total:Int = 0
    @SerializedName("docs")val  docs:List<ProductListDocs> =  listOf()
}

class ProductListDocs{
    @SerializedName("_id")val  id:String = ""
    @SerializedName("status")val  status:String = ""
    @SerializedName("productName")val  productName:String = ""
    @SerializedName("productDescription")val  productDescription:String = ""
    @SerializedName("size_value")val  sizeValue:List<SizeValueData> = listOf()
    @SerializedName("productImage")val  productImage:List<String> = listOf()

    var quantity = 1




}

class SizeValueData{
    @SerializedName("price")val  price:Number = 0
    @SerializedName("size")val  size:String = ""
    @SerializedName("weight")val  weight:String = ""
    @SerializedName("unit")val  unit:String = ""
    @SerializedName("_id")val  id:String = ""

}