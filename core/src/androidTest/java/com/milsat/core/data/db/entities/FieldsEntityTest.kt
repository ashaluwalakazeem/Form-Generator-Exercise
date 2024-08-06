package com.milsat.core.data.db.entities


import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.milsat.core.data.db.CapstoneDao
import com.milsat.core.data.db.CapstoneDatabase
import com.milsat.core.di.dbTestModule
import com.milsat.core.domain.model.UIType
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
class FieldsEntityTest : KoinTest {

    private val capstoneDao: CapstoneDao by inject()
    private val db: CapstoneDatabase by inject()

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun setUp() {
        startKoin {
            androidContext(context)
            modules(dbTestModule)
        }
    }

    @After
    fun tearDown() {
        db.close()
        stopKoin()
    }

    @Test
    fun insertAndGetFields() = runTest {
        val fieldsEntity = FieldsEntity(
            formId = 1,
            columnName = "NAME_BLD",
            columnTitle = "Name of Building",
            columnType = "TEXT",
            required = true,
            minLength = 0,
            maxLength = 500,
            showOnList = true,
            uiType = UIType.text_field,
            values = null
        )

        capstoneDao.insert(fieldsEntity)

        val retrievedFields = capstoneDao.getFieldsByFormId(1)
        assertEquals(1, retrievedFields.size)
        val retrievedField = retrievedFields[0]

        assertEquals(fieldsEntity.formId, retrievedField.formId)
        assertEquals(fieldsEntity.columnName, retrievedField.columnName)
        assertEquals(fieldsEntity.columnTitle, retrievedField.columnTitle)
        assertEquals(fieldsEntity.columnType, retrievedField.columnType)
        assertEquals(fieldsEntity.required, retrievedField.required)
        assertEquals(fieldsEntity.minLength, retrievedField.minLength)
        assertEquals(fieldsEntity.maxLength, retrievedField.maxLength)
        assertEquals(fieldsEntity.showOnList, retrievedField.showOnList)
        assertEquals(fieldsEntity.uiType, retrievedField.uiType)
        assertEquals(fieldsEntity.values, retrievedField.values)
    }
}
