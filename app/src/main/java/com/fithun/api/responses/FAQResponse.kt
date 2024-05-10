package com.fithun.api.responses

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FAQResponse : Serializable {
    @SerializedName("result"          ) var result          : ResultFaq? = ResultFaq()
    @SerializedName("responseMessage" ) var responseMessage : String? = ""
    @SerializedName("responseCode"    ) var responseCode    : Int?    = 0

}
class ResultFaq : Serializable{

    @SerializedName("docs"  ) var docs  : ArrayList<Docs> = arrayListOf()
    @SerializedName("total" ) var total : Int?            = 0
    @SerializedName("limit" ) var limit : Int?            =0
    @SerializedName("page"  ) var page  : Int?            = 0
    @SerializedName("pages" ) var pages : Int?            = 0

}



 class Docs : Serializable{

    @SerializedName("_id"       ) var Id        : String? = ""
    @SerializedName("question"  ) var question  : String? = ""
    @SerializedName("answer"    ) var answer    : String? = ""
    @SerializedName("status"    ) var status    : String? = ""
    @SerializedName("createdAt" ) var createdAt : String? = ""
    @SerializedName("updatedAt" ) var updatedAt : String? = ""
    @SerializedName("categoryData" ) var categoryData : CategoryData  =CategoryData()
    @SerializedName("subCategoryData" ) var subCategoryData : ArrayList<SubCategory>  =ArrayList()
    var expand : Boolean =false
    @SerializedName("__v"       ) var _v        : Int?    = 0

}

class CategoryData: Serializable{

    @SerializedName("_id")
    var Id: String? = ""
    @SerializedName("name")
    var name: String? = ""
    @SerializedName("status")
    var status: String? = ""
    @SerializedName("createdAt")
    var createdAt: String? = ""
    @SerializedName("updatedAt")
    var updatedAt: String? = ""
    @SerializedName("__v")
    var _v: Int? = 0

}

class SubCategory : Serializable{

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
    var expand : Boolean =false
    @SerializedName("categoryData")
    var categoryData: CategoryData? = CategoryData()

}
