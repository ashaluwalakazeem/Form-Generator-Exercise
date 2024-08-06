package com.milsat.core.data.db.entities

import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forms")
@Stable
data class FormEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val pageCount: Int
)
