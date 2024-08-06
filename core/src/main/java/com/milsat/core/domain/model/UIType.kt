package com.milsat.core.domain.model

import com.milsat.core.data.db.UITypeConverter

enum class UIType {
    TEXT_FIELD, DROP_DOWN, UNKNOWN
}

fun String.toUIType(): UIType{
    return try {
        val uiTypeConverter = UITypeConverter()
        uiTypeConverter.toUIType(this)
    }catch (_: Exception){
        UIType.UNKNOWN
    }
}