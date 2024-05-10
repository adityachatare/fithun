package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class BrandListResponse (
    @SerializedName("responseMessage")val  responseMessage:String,
    @SerializedName("responseCode")val  responseCode:Int,
    @SerializedName("result")val  result:List<BrandListResult>
)


class BrandListResult{
    @SerializedName("_id")val  id:String = ""
    @SerializedName("brandName")val  brandName:String = ""
    @SerializedName("status")val  status:String = ""


    var isSelected = false

}