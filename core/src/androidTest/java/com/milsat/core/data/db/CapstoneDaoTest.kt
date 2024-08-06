package com.milsat.core.data.db


import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.milsat.core.data.db.entities.FieldsEntity
import com.milsat.core.data.db.entities.FormEntity
import com.milsat.core.di.dbTestModule
import com.milsat.core.domain.model.ColumnType
import com.milsat.core.domain.model.UIType
import kotlinx.coroutines.delay
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
class CapstoneDaoTest : KoinTest {

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
    fun insertAndUpdateFields() = runTest {
        // Insert a form entity
        val formEntity = FormEntity(name = "Test Form", pageCount = 2)
        capstoneDao.insertFormEntity(formEntity)

        // Insert a fields entity
        val fieldsEntity = FieldsEntity(
            formId = formEntity.id, // Use the form ID
            columnName = "NAME_BLD",
            fieldTitle = "Name of Building",
            columnType = ColumnType.TEXT,
            required = true,
            minLength = 0,
            maxLength = 500,
            showOnList = true,
            uiType = UIType.TEXT_FIELD,
            values = null,
            id = 1,
            pageName = "BUILDING_MAPPING",
            columnValue = ""
        )
        capstoneDao.insertFieldsEntity(fieldsEntity)

        // Update the fields entity
        val updatedFieldsEntity = fieldsEntity.copy(fieldTitle = "Updated Name of Building")
        capstoneDao.updateFieldEntity(updatedFieldsEntity)


        delay(2000)
        // Retrieve the updated fields entity
        val retrievedFields = capstoneDao.getFieldsByFormId(formEntity.id)

        // Assertions
        assertEquals(1, retrievedFields.size)
        val retrievedField = retrievedFields[0]

        assertEquals(updatedFieldsEntity.formId, retrievedField.formId)
        assertEquals(updatedFieldsEntity.columnName, retrievedField.columnName)
        assertEquals(updatedFieldsEntity.fieldTitle, retrievedField.fieldTitle)
        assertEquals(updatedFieldsEntity.columnType, retrievedField.columnType)
        assertEquals(updatedFieldsEntity.required, retrievedField.required)
        assertEquals(updatedFieldsEntity.minLength, retrievedField.minLength)
        assertEquals(updatedFieldsEntity.maxLength, retrievedField.maxLength)
        assertEquals(updatedFieldsEntity.showOnList, retrievedField.showOnList)
        assertEquals(updatedFieldsEntity.uiType, retrievedField.uiType)
        assertEquals(updatedFieldsEntity.values, retrievedField.values)
    }
}
