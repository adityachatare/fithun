package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class LogoutResponse (


    @SerializedName("result"          ) var result          : ResultLogout,
    @SerializedName("responseCode") var responseCode:Int ,
    @SerializedName("responseMessage") var responseMessage:String
    )

 class ResultLogout {

    @SerializedName("token"               ) var token               : String? = ""
    @SerializedName("emailOrMobileNumber" ) var emailOrMobileNumber : String? = ""
    @SerializedName("_id"                 ) var Id                  : String? = ""
    @SerializedName("userType"            ) var userType            : String? = ""

}