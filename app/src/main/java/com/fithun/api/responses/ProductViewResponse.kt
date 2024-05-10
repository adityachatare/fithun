package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class ProductViewResponse(
    @SerializedName("responseMessage")val  responseMessage:String,
    @SerializedName("responseCode")val  responseCode:Int,
    @SerializedName("result")val  result:ProductListDocs = ProductListDocs()
)