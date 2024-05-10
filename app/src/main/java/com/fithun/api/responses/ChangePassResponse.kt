package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class ChangePassResponse(
   @SerializedName("responseMessage")val  responseMessage:String,
   @SerializedName("responseCode")val  responseCode:Int

)