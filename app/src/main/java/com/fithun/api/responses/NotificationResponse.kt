package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class NotificationResponse (
    @SerializedName("responseMessage")val  responseMessage:String,
    @SerializedName("responseCode")val  responseCode:Int,
    @SerializedName("result") val result: NotificationResult = NotificationResult()
)

class NotificationResult{
    @SerializedName("docs") val  docs: ArrayList<NotificationDocs> = arrayListOf()
}

class NotificationDocs{
    @SerializedName("subject")val  subject:String = ""
    @SerializedName("body")val  body:String = ""
    @SerializedName("createdAt")val  createdAt:String = ""
}





