package com.milsat.core.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.milsat.core.domain.model.UIType

@Entity(tableName = "fields")
data class FieldsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val columnName: String,
    val columnTitle: String,
    val columnType: String,
    val required: Boolean,
    val minLength: Int? = null,
    val maxLength: Int? = null,
    val showOnList: Boolean = false,
    val uiType: UIType,
    val values: List<String>?,
)
