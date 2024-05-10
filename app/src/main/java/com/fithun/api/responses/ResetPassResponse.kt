package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class ResetPassResponse (
    @SerializedName("result"          ) var result          : Resul,
    @SerializedName("responseMessage" ) var responseMessage : String,
    @SerializedName("responseCode"    ) var responseCode    : Int
   )
 class Resul {

    @SerializedName("additionalDetails"   ) var additionalDetails   : AdditionalDetails? = AdditionalDetails()
    @SerializedName("isProfileCompleted"  ) var isProfileCompleted  : Boolean?           = false
    @SerializedName("isPanVerified"       ) var isPanVerified       : Boolean?           = false
    @SerializedName("isUPIVerified"       ) var isUPIVerified       : Boolean?           = false
    @SerializedName("_id"                 ) var Id                  : String?            = ""
    @SerializedName("name"                ) var name                : String?            = ""
    @SerializedName("userName"            ) var userName            : String?            = ""
    @SerializedName("email"               ) var email               : String?            = ""
    @SerializedName("password"            ) var password            : String?            = ""
    @SerializedName("mobileNumber"        ) var mobileNumber        : String?            =""
    @SerializedName("dob"                 ) var dob                 : String?            = ""
    @SerializedName("gender"              ) var gender              : String?            = ""
    @SerializedName("mobileCode"          ) var mobileCode          : String?            = ""
    @SerializedName("userType"            ) var userType            : String?            = ""
    @SerializedName("approveStatus"       ) var approveStatus       : String?            = ""
    @SerializedName("status"              ) var status              : String?            = ""
    @SerializedName("deviceType"          ) var deviceType          : String?            = ""
    @SerializedName("deviceToken"         ) var deviceToken         : ArrayList<String>  = arrayListOf()
    @SerializedName("homeAddress"         ) var homeAddress         : String?            = ""
    @SerializedName("officeAddress"       ) var officeAddress       : String?            = ""
    @SerializedName("mobileLinkedWithUpi" ) var mobileLinkedWithUpi : String?            = ""
    @SerializedName("createdAt"           ) var createdAt           : String?            = ""
    @SerializedName("updatedAt"           ) var updatedAt           : String?            = ""
    @SerializedName("__v"                 ) var _v                  : Int?               = 0
    @SerializedName("otp"                 ) var otp                 : String?            = ""
    @SerializedName("otpTime"             ) var otpTime             : String?            = ""

}

 class AdditionalDetails {

    @SerializedName("shoeSize"             ) var shoeSize             : String?  = ""
    @SerializedName("weight"               ) var weight               : String?  = ""
    @SerializedName("upiId"                ) var upiId                : String?  = ""
    @SerializedName("physicallyChallenged" ) var physicallyChallenged : String?  = ""
    @SerializedName("ableToWalk"           ) var ableToWalk           : Boolean? = false

}