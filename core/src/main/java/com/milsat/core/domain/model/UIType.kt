package com.milsat.core.domain.model

import com.milsat.core.data.db.UITypeConverter

enum class UIType {
    TEXT_FIELD, DROP_DOWN, UN_KNOWN
}

fun String.toUIType(): UIType{
    return try {
        val uiTypeConverter = UITypeConverter()
        uiTypeConverter.toUIType(this)
    }catch (_: Exception){
        UIType.UN_KNOWN
    }
}