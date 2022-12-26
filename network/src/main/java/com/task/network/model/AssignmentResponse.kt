package com.task.network.model

import com.google.gson.annotations.SerializedName

data class AssignmentResponse(
    @SerializedName("logo-url")
    var logoUrl: String,
    @SerializedName("heading-text")
    var headingText: String,
    @SerializedName("uidata")
    var uiData: List<UIDataResponse>
)

data class UIDataResponse(
    @SerializedName("uitype")
    var uiType: String,
    @SerializedName("value")
    var value: String,
    @SerializedName("key")
    var key: String? = null,
    @SerializedName("hint")
    var hint: String? = null,
)