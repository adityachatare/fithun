package com.fithun.api.requests

import com.google.gson.annotations.SerializedName

class ResetPassRequest {
    @SerializedName("token") var token:String=""
    @SerializedName("password")var pass:String=""
    @SerializedName("confirmPassword")var confirmPass:String=""
}