package com.persona5dex.update

import com.google.gson.annotations.SerializedName

data class UpdateConfig(@SerializedName("action_type") val actionType: String,
                        @SerializedName("action_value") val actionValue: String,
                        @SerializedName("action_button_text") val actionButtonText: String)