package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class SocialSignResponse {
    @SerializedName("result"          ) var result          : ResultSocial = ResultSocial()
    @SerializedName("responseMessage" ) var responseMessage : String? = ""
    @SerializedName("responseCode"    ) var responseCode    : Int?    = 0

}
 class ResultSocial {

    @SerializedName("token"              ) var token              : String?  = ""
    @SerializedName("_id"                ) var Id                 : String?  = ""
    @SerializedName("userType"           ) var userType           : String?  =""
    @SerializedName("isProfileCompleted" ) var isProfileCompleted : Boolean = false
     @SerializedName("name"               ) var name               : String?  = ""
     @SerializedName("email"               ) var email             : String?  = ""
    @SerializedName("profilePic"         ) var profilePic         : String?  = ""
    @SerializedName("userName"           ) var userName           : String?  = ""

}