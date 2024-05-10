package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class HowToPlayResponse (
    @SerializedName("result"          ) var result          : ResultHow,
    @SerializedName("responseCode") var statusCode:Int ,
    @SerializedName("responseMessage") var responseMessage:String
    )


    class ResultHow {

     @SerializedName("Data")
     var Data: ArrayList<Docs> = arrayListOf()
     @SerializedName("total")
     var total: Int? = 0
     @SerializedName("limit")
     var limit: Int? = 0
     @SerializedName("page")
     var page: Int? = 0
     @SerializedName("pages")
     var pages: Int? = 0

 }
 class SubCategoryData {

     @SerializedName("_id")
     var Id: String? = ""
     @SerializedName("title")
     var title: String? = ""
     @SerializedName("media")
     var media: String? = ""
     @SerializedName("description")
     var description: String? = ""
     @SerializedName("categoryId")
     var categoryId: String? = ""
     @SerializedName("status")
     var status: String? = ""
     @SerializedName("createdAt")
     var createdAt: String? = ""
     @SerializedName("updatedAt")
     var updatedAt: String? = ""
     @SerializedName("__v")
     var _v: Int? = 0
     @SerializedName("categoryData")
     var categoryData: CategoryData? = CategoryData()

 }


