package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class GetAllBannerResponse {
    @SerializedName("result"          ) var result          : ArrayList<BannerResult> = arrayListOf()
    @SerializedName("responseMessage" ) var responseMessage : String?           = ""
    @SerializedName("responseCode"    ) var responseCode    : Int?=0
}

 class BannerResult {

    @SerializedName("_id"         ) var Id          : String? = ""
    @SerializedName("bannerImage" ) var bannerImage : String =""
    @SerializedName("status"      ) var status      : String? = ""
    @SerializedName("link"        ) var link        : String? =""
    @SerializedName("createdAt"   ) var createdAt   : String? = ""
    @SerializedName("updatedAt"   ) var updatedAt   : String? = ""
    @SerializedName("__v"         ) var _v          : Int?    = 0

}