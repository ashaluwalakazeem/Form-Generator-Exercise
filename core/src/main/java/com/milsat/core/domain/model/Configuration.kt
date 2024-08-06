package com.milsat.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
internal data class Configuration(
    @SerialName("forms") val forms: Map<String, Form>
){
    @Serializable
    internal data class Field(
        @SerialName("ui_type") val uiType: String,
        @SerialName("column_type") val columnType: String,
        @SerialName("column_name") val columnName: String,
        val required: Boolean,
        @SerialName("min_length") val minLength: Int? = null,
        @SerialName("max_length") val maxLength: Int? = null,
        @SerialName("showOnList") val showOnList: Boolean? = null,
        val values: List<String>? = null
    )

    @Serializable
    internal data class Page(
        val fields: Map<String, Field>
    )

    @Serializable
    internal data class Form(
        val pages: Map<String, Page>
    )
}