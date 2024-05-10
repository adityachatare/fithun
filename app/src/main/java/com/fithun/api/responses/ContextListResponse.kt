package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class ContextListResponse {

    @SerializedName("result"          ) var result          : ResultContest = ResultContest()
    @SerializedName("responseMessage" ) var responseMessage : String? = ""
    @SerializedName("responseCode"    ) var responseCode    : Int?    = 0

}
 class ContestDetails {

    @SerializedName("_id") var id            : String? =""
    @SerializedName("heading"       ) var heading       : String? = ""
    @SerializedName("subHeading"    ) var subHeading    : String? = ""
    @SerializedName("contestType") var contestType: String? = ""
    @SerializedName("description"   ) var description   : String? =""
    @SerializedName("categoryImage" ) var categoryImage : String? = ""
    @SerializedName("status"        ) var status        : String? =""
    @SerializedName("createdAt"     ) var createdAt     : String? = ""
    @SerializedName("updatedAt"     ) var updatedAt     : String? =""
    @SerializedName("__v"           ) var _v            : Int?    = 0

}

 class ResultContest {

     @SerializedName("docs")
     var docs: ArrayList<ContestDetails> = arrayListOf()
     @SerializedName("total")
     var total: Int? = 0
     @SerializedName("limit")
     var limit: Int? = 0
     @SerializedName("page") var page: Int? = 0
     @SerializedName("pages") var pages: Int? = 0

 }