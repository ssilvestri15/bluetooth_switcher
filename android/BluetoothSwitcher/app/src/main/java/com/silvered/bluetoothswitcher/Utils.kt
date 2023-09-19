package com.silvered.bluetoothswitcher

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

fun ByteArray.encodeToBase64(): String = java.util.Base64.getEncoder().encodeToString(this)
fun String.decodeBase64(): ByteArray = java.util.Base64.getDecoder().decode(this)

fun sha256(input: String): String {
    val bytes = input.toByteArray(StandardCharsets.UTF_8)
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(bytes)

    // Convert the byte array to a hexadecimal string
    val hexString = StringBuilder()
    for (byte in hashBytes) {
        val hex = Integer.toHexString(0xFF and byte.toInt())
        if (hex.length == 1) {
            hexString.append('0')
        }
        hexString.append(hex)
    }

    return hexString.toString()
}