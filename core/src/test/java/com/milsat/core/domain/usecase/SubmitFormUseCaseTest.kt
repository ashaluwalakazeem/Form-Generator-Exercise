package com.milsat.core.domain.usecase


import com.milsat.core.data.db.entities.FieldsEntity
import com.milsat.core.domain.model.ColumnType
import com.milsat.core.domain.model.FormPage
import com.milsat.core.domain.model.FormFieldState
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
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class SubmitFormUseCaseTest {

    private lateinit var submitFormUseCase: SubmitFormUseCase
    private val formRepository: FormRepository = mockk()
    private val formPageGenerator: FormPageGenerator = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        submitFormUseCase = SubmitFormUseCase(formRepository, formPageGenerator)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke should return Success when form pages are valid`() = runTest {
        // Given
        val formPages = listOf(
            FormPage(name = "Page1", formFields = listOf(
                FormFieldState(fieldsEntity = FieldsEntity(
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
                    values = listOf("Option1"),
                    skipTo = null
                )
                )
            ))
        )
        val fieldEntities = listOf(
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
                values = listOf("Option1"),
                skipTo = null
            )
        )

        every { formPageGenerator.transformFormPagesToFieldEntities(formPages) } returns fieldEntities
        coEvery { formRepository.submitForm(fieldEntities) } just Runs

        // When
        val result = submitFormUseCase(formPages)

        // Then
        assertEquals(Result.Success(true, "Form has been saved successfully."), result)

        coVerify(exactly = 1) {
            formPageGenerator.transformFormPagesToFieldEntities(formPages)
            formRepository.submitForm(fieldEntities)
        }
    }

    @Test
    fun `invoke should return Error when form pages are invalid`() = runBlocking {
        // Given
        val formPages = listOf(
            FormPage(name = "Page1", formFields = listOf(
                FormFieldState(fieldsEntity = FieldsEntity(
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
                    values = listOf("Option1"),
                    skipTo = null
                ))
            ))
        )

        // When
        val result = submitFormUseCase(formPages)

        // Then
        assertEquals(Result.Success(false, "Form is invalid. Please do the needful"), result)

        coVerify(exactly = 0) {
            formPageGenerator.transformFormPagesToFieldEntities(formPages)
            formRepository.submitForm(any())
        }
    }

}
