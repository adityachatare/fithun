package com.fithun.api.requests

import com.google.gson.annotations.SerializedName

class VerifyMobileRequest {
    @SerializedName("mobileNumber")var mobileNumber:String=""
    @SerializedName("otp")var otp:String=""
}