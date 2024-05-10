package com.fithun.api.requests

import com.google.gson.annotations.SerializedName

class ChangePassRequest {
    @SerializedName("oldPassword")var oldpass:String=""
    @SerializedName("password")var pass:String=""
    @SerializedName("confirmPassword")var confirmPass:String=""
}