package com.milsat.core.utils

import com.milsat.core.data.db.entities.FieldsEntity
import com.milsat.core.domain.model.ColumnType
import com.milsat.core.domain.model.FormFieldState
import com.milsat.core.domain.model.FormPage
import com.milsat.core.domain.model.UIType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class FormPageGeneratorTest {

    private lateinit var formPageGenerator: FormPageGenerator

    @Before
    fun setUp() {
        formPageGenerator = FormPageGenerator()
    }

    @Test
    fun `generateFormPages should return form pages successfully`() {
        // Given
        val fieldsEntities = listOf(
            FieldsEntity(
                id = 1,
                formId = 1,
                columnName = "col1",
                pageName = "Page 1",
                fieldTitle = "Title 1",
                columnType = ColumnType.TEXT,
                required = true,
                uiType = UIType.TEXT_FIELD,
                values = listOf("Option 1", "Option 2"),
                skipTo = ""
            ),
            FieldsEntity(
                id = 2,
                formId = 1,
                columnName = "col2",
                pageName = "Page 1",
                fieldTitle = "Title 2",
                columnType = ColumnType.TEXT,
                required = false,
                uiType = UIType.TEXT_FIELD,
                values = listOf("Option 1", "SELECT OPTION"),
                skipTo = ""
            ),
            FieldsEntity(
                id = 3,
                formId = 1,
                columnName = "col3",
                pageName = "Page 2",
                fieldTitle = "Title 3",
                columnType = ColumnType.NUMBER,
                required = true,
                uiType = UIType.TEXT_FIELD,
                values = listOf("Option 1", "Option 2"),
                skipTo = ""
            )
        )

        // When
        val result = formPageGenerator.generateFormPages(fieldsEntities)

        // Then
        assertTrue(result is Result.Success)
        val formPages = (result as Result.Success).data
        assertEquals(2, formPages.size)
        assertEquals("Page 1", formPages[0].name)
        assertEquals(2, formPages[0].formFields.size)
        assertEquals("Page 2", formPages[1].name)
        assertEquals(1, formPages[1].formFields.size)
    }


    @Test
    fun `transformFormPagesToFieldEntities should transform form pages to field entities`() {
        // Given
        val formFields = listOf(
            FormFieldState(
                fieldsEntity = FieldsEntity(
                    id = 1,
                    formId = 1,
                    columnValue = "Value 1",
                    columnName = "col1",
                    pageName = "Page 1",
                    fieldTitle = "Title 1",
                    columnType = ColumnType.TEXT,
                    required = true,
                    uiType = UIType.TEXT_FIELD,
                    values = listOf("Option 1"),
                    skipTo = ""
                )
            ),
            FormFieldState(
                fieldsEntity = FieldsEntity(
                    id = 2,
                    formId = 1,
                    columnValue = "Value 2",
                    columnName = "col2",
                    pageName = "Page 1",
                    fieldTitle = "Title 2",
                    columnType = ColumnType.TEXT,
                    required = false,
                    uiType = UIType.TEXT_FIELD,
                    values = listOf("Option 2"),
                    skipTo = ""
                ),
            )
        )
        val formPages = listOf(FormPage(name = "Page 1", formFields = formFields))

        // When
        val result = formPageGenerator.transformFormPagesToFieldEntities(formPages)

        // Then
        assertEquals(2, result.size)
        assertEquals("Value 1", result[0].columnValue)
        assertEquals("Value 2", result[1].columnValue)
    }
}
