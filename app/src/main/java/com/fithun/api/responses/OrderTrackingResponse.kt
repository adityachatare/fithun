package com.fithun.api.responses

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class OrderTrackingResponse(
    @SerializedName("responseMessage")val  responseMessage:String = "",
    @SerializedName("responseCode")val  responseCode:Int = 0,
    @SerializedName("result") val result: ArrayList<ShipmentTrackActivities> = arrayListOf()

)

data class OrderTrackingResult(
    @SerializedName("shipment_track") val shipmentTrack: ArrayList<ShipmentTrackData> = arrayListOf(),
    @SerializedName("shipment_track_activities") val shipmentTrackActivities: ArrayList<ShipmentTrackActivities> = arrayListOf(),
    @SerializedName("track_url") val trackUrl: String = ""
)

class ShipmentTrackData()

class ShipmentTrackActivities(
    @SerializedName("date") val date: String = "",
    @SerializedName("status") val status: String = "",
    @SerializedName("activity") val activity: String = "",
    @SerializedName("location") val location: String = "",
    @SerializedName("sr-status") val srStatus: String = "",
    @SerializedName("sr-status-label") val srStatusLabel: String = "",
    @SerializedName("trackStatus") val trackStatus: Boolean = false,



): Parcelable {

    var progressValue = 50

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(activity)
        parcel.writeString(location)
        parcel.writeString(srStatusLabel)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShipmentTrackActivities> {
        override fun createFromParcel(parcel: Parcel): ShipmentTrackActivities {
            return ShipmentTrackActivities(parcel)
        }

        override fun newArray(size: Int): Array<ShipmentTrackActivities?> {
            return arrayOfNulls(size)
        }
    }
}
