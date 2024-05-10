package com.fithun.api.responses

import com.google.gson.annotations.SerializedName

class GraphResponse (
    @SerializedName("responseMessage")val  responseMessage:String,
    @SerializedName("responseCode")val  responseCode:Int,
    @SerializedName("result")val  result:Graph=Graph()
)


class Graph{
    @SerializedName("totalSteps")val  totalSteps:Number = 0
    @SerializedName("totalDistanceWalked")val  totalDistanceWalked:Number = 0
    @SerializedName("averageSpeed")val  averageSpeed:Float = 0f
    @SerializedName("graphData")val  graphData:ArrayList<GraphData> = arrayListOf()
    @SerializedName("distanceWalked")val  distanceWalked:Float = 2f
    @SerializedName("caloriesCount")val  caloriesCount:Number = 0
    @SerializedName("reduceWeight")val  reduceWeight:Number = 0
    @SerializedName("userDetails")val  userDetails :ResultProfile = ResultProfile()
}
class  GraphData{
    @SerializedName("_id")val  _id:String = ""
    @SerializedName("stepCount")val  stepCount:Float = 2f


}
