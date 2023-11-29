package com.osl.swrnd.entity.local.res

import com.google.gson.annotations.SerializedName

data class AuthTokenVo(
    @SerializedName("expires_in") val expiresIn: Int,
    @SerializedName("status") val status: String,
    @SerializedName("token") val token: String
)