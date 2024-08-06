package com.milsat.core.domain.model

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.input.KeyboardType
import com.milsat.core.data.db.ColumnTypeConverter

@Stable
enum class ColumnType {
    TEXT, NUMBER, EMAIL, UNKNOWN
}

fun String.toColumnType(): ColumnType{
    return try {
        val uiTypeConverter = ColumnTypeConverter()
        uiTypeConverter.toColumnType(this)
    }catch (_: Exception){
        ColumnType.UNKNOWN
    }
}

fun ColumnType.toKeyboardType(): KeyboardType {
    return when(this){
        ColumnType.TEXT, ColumnType.UNKNOWN -> KeyboardType.Text
        ColumnType.NUMBER -> KeyboardType.Number
        ColumnType.EMAIL -> KeyboardType.Email
    }
}