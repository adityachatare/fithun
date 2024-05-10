package com.fithun.api.requests

import com.google.gson.annotations.SerializedName

class LoginRequest {
    @SerializedName("emailOrMobile") var email:String=""
    @SerializedName("password")var pass:String=""
    @SerializedName("deviceType")var deviceTypes:String=""
    @SerializedName("deviceToken")var deviceToken:String=""

}