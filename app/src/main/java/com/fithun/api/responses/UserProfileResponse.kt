package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class UserProfileResponse (
    @SerializedName("result") var result: ResultProfile = ResultProfile(),
    @SerializedName("responseMessage" ) var responseMessage : String? = "",
    @SerializedName("responseCode"    ) var responseCode    : Int?    = 0

)

 class ResultProfile (

    @SerializedName("additionalDetails"   ) var additionalDetails   : AdditionalData = AdditionalData(),
    @SerializedName("isProfileCompleted") var isProfileCompleted  : Boolean= false,
    @SerializedName("isPanVerified") var isPanVerified: Boolean= false,
    @SerializedName("isUPIVerified") var isUPIVerified: Boolean= false,
    @SerializedName("_id") var Id : String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("userName") var userName  : String = "",
    @SerializedName("email") var email     : String = "",
    @SerializedName("mobileNumber" ) var mobileNumber : String = "",
    @SerializedName("dob") var dob: String = "",
    @SerializedName("gender"    ) var gender    : String = "",
    @SerializedName("mobileCode") var mobileCode: String = "",
    @SerializedName("userType") var userType  : String = "",
    @SerializedName("approveStatus") var approveStatus: String = "",
    @SerializedName("status"    ) var status    : String = "",
    @SerializedName("homeAddress") var homeAddress  : String = "",
    @SerializedName("profilePic") var profilePic  : String = "",
    @SerializedName("officeAddress") var officeAddress: String = "",
    @SerializedName("mobileLinkedWithUpi" ) var mobileLinkedWithUpi : String = "",
    @SerializedName("createdAt" ) var createdAt : String = "",
    @SerializedName("updatedAt" ) var updatedAt : String = "",
    @SerializedName("__v") var v : Int = 0,
    @SerializedName("wallet") var wallet : Float = 0F,
    @SerializedName("kycDetails") val kycDetails: KycDetails = KycDetails()


)
class AdditionalData {

   @SerializedName("shoeSize"   ) var shoeSize   : String?  = ""
   @SerializedName("weight") var weight     : String?  = ""
   @SerializedName("upiId") var upiId: String?  = ""
   @SerializedName("physicallyChallenged" ) var physicallyChallenged : String?  = ""
   @SerializedName("ableToWalk" ) var ableToWalk : Boolean? = false
}


class KycDetails(
   @SerializedName("upiId" ) var upiId : String?  = "",
   @SerializedName("panNumber" ) var panNumber : String?  = ""
)
