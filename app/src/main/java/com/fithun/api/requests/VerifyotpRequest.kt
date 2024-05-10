package com.fithun.api.requests

import com.google.gson.annotations.SerializedName

class VerifyOtpRequest {
    @SerializedName("emailOrMobile" )var emailandPass:String = ""
    @SerializedName("otp")var otp:String=""
}