package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class ViewBannerResponse {
    @SerializedName("result"          ) var result          : ResultViews = ResultViews()
    @SerializedName("responseMessage" ) var responseMessage : String = ""
    @SerializedName("responseCode"    ) var responseCode    : Int    = 0
}



class ResultViews {

    @SerializedName("_id"         ) var Id          : String = ""
    @SerializedName("bannerName"  ) var bannerName  : String = ""
    @SerializedName("bannerImage" ) var bannerImage : String = ""
    @SerializedName("description" ) var description : String = ""
    @SerializedName("status"      ) var status      : String = ""
    @SerializedName("link"        ) var link        : String = ""
    @SerializedName("__v"         ) var _v          : Int   = 0

}