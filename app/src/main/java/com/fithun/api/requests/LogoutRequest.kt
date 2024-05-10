package com.fithun.api.requests

import com.google.gson.annotations.SerializedName

class LogOutRequest {
    @SerializedName("deviceType")var deviceType:String=""
    @SerializedName("deviceToken")var deviceToken:String=""
}