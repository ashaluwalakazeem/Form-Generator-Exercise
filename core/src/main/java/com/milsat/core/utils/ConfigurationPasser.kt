package com.milsat.core.utils

import com.milsat.core.domain.model.Configuration
import kotlinx.serialization.json.Json

internal class ConfigurationPasser {

    private val jsonParser = Json { ignoreUnknownKeys = true }

    fun parseJson(jsonString: String): Configuration? {
        return try {
            jsonParser.decodeFromString(jsonString)
        } catch (_: Exception) {
            null
        }
    }
}