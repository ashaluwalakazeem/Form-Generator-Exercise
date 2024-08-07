package com.milsat.core.domain.model

import com.milsat.core.data.db.entities.FieldsEntity
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
        val values: List<String>? = null,
        @SerialName("skipTo")
        val skipTo: Map<String, String>? = null
    ){
        fun toDomain(formId: Int, fieldTitle: String, pageName: String): FieldsEntity{
            return FieldsEntity(
                formId = formId,
                columnName = columnName,
                pageName = pageName,
                fieldTitle = fieldTitle,
                columnType = columnType.toColumnType(),
                required = required,
                minLength = minLength,
                maxLength = maxLength,
                showOnList = showOnList,
                uiType = uiType.toUIType(),
                values = values,
                skipTo = skipTo?.getOrDefault("NO", null)
            )
        }
    }

    @Serializable
    internal data class Page(
        val fields: Map<String, Field>
    )

    @Serializable
    internal data class Form(
        val pages: Map<String, Page>
    )
}