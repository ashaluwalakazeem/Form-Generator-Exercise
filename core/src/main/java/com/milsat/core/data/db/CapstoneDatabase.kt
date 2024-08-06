package com.milsat.core.data.db
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.milsat.core.data.db.entities.FieldsEntity
import com.milsat.core.data.db.entities.FormEntity


@Database(entities = [FieldsEntity::class, FormEntity::class], version = 1)
@TypeConverters(StringListConverter::class, UITypeConverter::class, ColumnTypeConverter::class)
internal abstract class CapstoneDatabase : RoomDatabase() {

    abstract fun capstoneDao(): CapstoneDao
}