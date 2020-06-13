package com.fidea.letter.models

import com.google.gson.annotations.SerializedName

class NotificationModel {

    @SerializedName("big_text")
    val bigText: String? = null
    @SerializedName("big_content_title")
    val bigContentTitle: String? = null
    @SerializedName("content_title")
    val contentTitle: String? = null
    @SerializedName("summary_text")
    val summaryText: String? = null
    var importance = 0
    val contentText: String? = null
    val smallIcon: String? = null

}