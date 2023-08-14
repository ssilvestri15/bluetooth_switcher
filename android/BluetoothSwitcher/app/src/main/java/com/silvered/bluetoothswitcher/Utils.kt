package com.silvered.bluetoothswitcher

fun ByteArray.encodeToBase64(): String = java.util.Base64.getEncoder().encodeToString(this)
fun String.decodeBase64(): ByteArray = java.util.Base64.getDecoder().decode(this)
