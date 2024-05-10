package com.fithun.api.requests

import com.google.gson.annotations.SerializedName

class VerifyEmailMobileOtpRequest {
    @SerializedName("email") var email:String=""
    @SerializedName("otp")var otp:String=""

}