package com.silvered.bluetoothswitcher

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConnectionRequestModel (
    val host: String = "",
    val mac: String = "",
    val token: String = "",
    val type: String = ""
): Parcelable