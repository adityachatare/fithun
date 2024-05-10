package com.fithun.api.requests

import com.google.gson.annotations.SerializedName

class SignUpRequest {
    @SerializedName("name") var name:String = ""
    @SerializedName("userName") var userName:String = ""
    @SerializedName("email") var email:String = ""
    @SerializedName("password") var password:String = ""
    @SerializedName("mobileNumber") var mobileNumber:String = ""
    @SerializedName("dob") var dob:String = ""
    @SerializedName("gender") var gender:String = ""
    @SerializedName("mobileCode") var mobileCode:String = ""
    @SerializedName("referId") var referId:String = ""
    @SerializedName("isMinor") var isMinor:Boolean = false
    @SerializedName("additionalDetails") var additionalDetails: AdditionalDetails = AdditionalDetails()
}


class AdditionalDetails{
    @SerializedName("shoeSize") var shoeSize:String = ""
    @SerializedName("weight") var weight:String = ""
    @SerializedName("upiId") var upiId:String = ""
    @SerializedName("physicallyChallenged") var physicallyChallenged:String = ""
    @SerializedName("ableToWalk") var ableToWalk:Boolean = false
}
