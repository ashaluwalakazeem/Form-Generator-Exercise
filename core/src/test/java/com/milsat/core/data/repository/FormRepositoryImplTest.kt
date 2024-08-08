package com.milsat.core.data.repository

import com.milsat.core.data.db.CapstoneDao
import com.milsat.core.data.db.entities.FieldsEntity
import com.milsat.core.data.db.entities.FormEntity
import com.milsat.core.domain.model.ColumnType
import com.milsat.core.domain.model.Configuration
import com.milsat.core.domain.model.UIType
import com.milsat.core.utils.ConfigurationPasser
import com.milsat.core.utils.Result
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class FormRepositoryImplTest {

    private lateinit var formRepository: FormRepositoryImpl
    private val capstoneDao: CapstoneDao = mockk()
    private val configurationPasser: ConfigurationPasser = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        formRepository = FormRepositoryImpl(capstoneDao, configurationPasser)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `createNewForm should return Success when configuration is valid`() = runBlocking {
        // Given
        val config = """{"forms": {"Form1": {"pages": {"Page1": {"fields": {"Field1": {"type": "String"}}}}}}}"""
        val mockField: Configuration.Field = mockk()
        val configuration = Configuration(
            forms = mapOf("Form1" to Configuration.Form(
                pages = mapOf("Page1" to Configuration.Page(
                    fields = mapOf("Field1" to Configuration.Field(
                        uiType = "drop_down", columnType = "TEXT", columnName = "BLD_STAT", required = true
                    ))
                ))
            ))
        )

        val formEntity = FormEntity(name = "Form1", pageCount = 1)
        val formEntityWithId = formEntity.copy(id = 1)
        val fieldsEntity = FieldsEntity(
            formId = 1,
            columnName = "Field1",
            pageName = "Page1",
            fieldTitle = "Field1",
            columnType = ColumnType.TEXT,
            columnValue = "",
            required = true,
            minLength = null,
            maxLength = null,
            showOnList = true,
            uiType = UIType.TEXT_FIELD,
            values = listOf("Option1", "Option2"),
            skipTo = null
        )

        coEvery { configurationPasser.parseJson(config) } returns configuration
        coEvery { capstoneDao.insertFormEntity(any()) } returns 1L
        coEvery { capstoneDao.insertFieldsEntities(any()) } just Runs

        // When
        val result = formRepository.createNewForm(config)

        // Then
        assertEquals(Result.Success(formEntityWithId), result)

        coVerify(exactly = 1) {
            configurationPasser.parseJson(config)
            capstoneDao.insertFormEntity(any())
            capstoneDao.insertFieldsEntities(any())
        }
    }

    @Test
    fun `createNewForm should return Error when configuration is invalid`() = runBlocking {
        // Given
        val config = """{"invalid": "config"}"""

        coEvery { configurationPasser.parseJson(config) } returns null

        // When
        val result = formRepository.createNewForm(config)

        // Then
        assertTrue(result is Result.Error)

        coVerify(exactly = 1) {
            configurationPasser.parseJson(config)
        }
        coVerify(exactly = 0) {
            capstoneDao.insertFormEntity(any())
            capstoneDao.insertFieldsEntities(any())
        }
    }

    @Test
    fun `getAllFormEntities should return flow of form entities`() = runBlocking {
        // Given
        val formEntities = listOf(FormEntity(name = "Form1", pageCount = 1))
        every { capstoneDao.getAllFormEntities() } returns flowOf(formEntities)

        // When
        val result = formRepository.getAllFormEntities()

        // Then
        result.collect {
            assertEquals(formEntities, it)
        }

        verify(exactly = 1) {
            capstoneDao.getAllFormEntities()
        }
    }

    @Test
    fun `fetchAllFormFields should return list of form fields`() = runBlocking {
        // Given
        val formId = 1
        val fieldsEntities = listOf(
            FieldsEntity(
                formId = formId,
                columnName = "Field1",
                pageName = "Page1",
                fieldTitle = "Field1",
                columnType = ColumnType.TEXT,
                columnValue = "",
                required = true,
                minLength = null,
                maxLength = null,
                showOnList = true,
                uiType = UIType.TEXT_FIELD,
                values = listOf("Option1", "Option2"),
                skipTo = null
            )
        )
        coEvery { capstoneDao.getFieldsByFormId(formId) } returns fieldsEntities

        // When
        val result = formRepository.fetchAllFormFields(formId)

        // Then
        assertEquals(fieldsEntities, result)

        coVerify(exactly = 1) {
            capstoneDao.getFieldsByFormId(formId)
        }
    }

    @Test
    fun `submitForm should update form fields`() = runBlocking {
        // Given
        val fieldsEntities = listOf(
            FieldsEntity(
                formId = 1,
                columnName = "Field1",
                pageName = "Page1",
                fieldTitle = "Field1",
                columnType = ColumnType.TEXT,
                columnValue = "Value",
                required = true,
                minLength = null,
                maxLength = null,
                showOnList = true,
                uiType = UIType.TEXT_FIELD,
                values = listOf("Option1", "Option2"),
                skipTo = null
            )
        )

        coEvery { capstoneDao.updateFieldEntities(fieldsEntities) } just Runs

        // When
        formRepository.submitForm(fieldsEntities)

        // Then
        coVerify(exactly = 1) {
            capstoneDao.updateFieldEntities(fieldsEntities)
        }
    }

    @Test
    fun `fetchFormEntity should return form entity`() = runBlocking {
        // Given
        val formId = 1
        val formEntity = FormEntity(id = formId, name = "Form1", pageCount = 1)

        coEvery { capstoneDao.getFormEntity(formId) } returns formEntity

        // When
        val result = formRepository.fetchFormEntity(formId)

        // Then
        assertEquals(formEntity, result)

        coVerify(exactly = 1) {
            capstoneDao.getFormEntity(formId)
        }
    }
}
