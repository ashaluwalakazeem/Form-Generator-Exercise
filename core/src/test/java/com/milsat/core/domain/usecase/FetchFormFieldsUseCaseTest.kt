package com.milsat.core.domain.usecase


import com.milsat.core.data.db.entities.FieldsEntity
import com.milsat.core.domain.model.ColumnType
import com.milsat.core.domain.model.FormFieldState
import com.milsat.core.domain.model.FormPage
import com.milsat.core.domain.model.UIType
import com.milsat.core.domain.repository.FormRepository
import com.milsat.core.utils.ErrorResponse
import com.milsat.core.utils.FormPageGenerator
import com.milsat.core.utils.Result
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class FetchFormFieldsUseCaseTest {

    private lateinit var fetchFormFieldsUseCase: FetchFormFieldsUseCase
    private val formRepository: FormRepository = mockk()
    private val formPageGenerator: FormPageGenerator = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fetchFormFieldsUseCase = FetchFormFieldsUseCase(formRepository, formPageGenerator)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke should return FormPages when formRepository fetches fields and formPageGenerator generates pages`() = runBlocking {
        // Given
        val formId = 1
        val fieldsEntities = listOf(
            FieldsEntity(
                formId = formId,
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
        val formPages = listOf(
            FormPage(name = "Page1", formFields = listOf(
                FormFieldState(
                    fieldsEntity = fieldsEntities[0].copy(columnValue = "Value")
                )
            ))
        )

        coEvery { formRepository.fetchAllFormFields(formId) } returns fieldsEntities
        every { formPageGenerator.generateFormPages(fieldsEntities) } returns Result.Success(formPages)

        // When
        val result = fetchFormFieldsUseCase(formId)

        // Then
        assertEquals(Result.Success(formPages), result)

        coVerify(exactly = 1) { formRepository.fetchAllFormFields(formId) }
        verify(exactly = 1) { formPageGenerator.generateFormPages(fieldsEntities) }
    }

    @Test
    fun `invoke should return Error when formPageGenerator fails`() = runBlocking {
        // Given
        val formId = 1
        val fieldsEntities = listOf(
            FieldsEntity(
                formId = formId,
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
        val errorResult = Result.Error(ErrorResponse.FormFieldError())

        coEvery { formRepository.fetchAllFormFields(formId) } returns fieldsEntities
        every { formPageGenerator.generateFormPages(fieldsEntities) } returns errorResult

        // When
        val result = fetchFormFieldsUseCase(formId)

        // Then
        assertEquals(errorResult, result)

        coVerify(exactly = 1) { formRepository.fetchAllFormFields(formId) }
        verify(exactly = 1) { formPageGenerator.generateFormPages(fieldsEntities) }
    }
}
