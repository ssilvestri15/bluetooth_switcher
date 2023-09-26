package utils

import java.io.FileInputStream
import java.util.*

object KeyManager {
    private val properties = Properties()

    init {
        // Load properties from local.properties
        val localPropertiesFile = "local.properties"
        val inputStream = FileInputStream(localPropertiesFile)
        properties.load(inputStream)
        inputStream.close()
    }

    val JWT_SECRET: String by lazy {
        properties.getProperty("JWT_SECRET")
            ?: throw IllegalStateException("mySecretKey is not defined in local.properties")
    }
}