package com.milsat.core.domain.usecase

import com.milsat.core.data.db.entities.FormEntity
import com.milsat.core.domain.repository.FormRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
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
class FetchFormEntityUseCaseTest {

    private lateinit var fetchFormEntityUseCase: FetchFormEntityUseCase
    private val formRepository: FormRepository = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fetchFormEntityUseCase = FetchFormEntityUseCase(formRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke should return FormEntity when formRepository returns FormEntity`() = runBlocking {
        // Given
        val formId = 1
        val formEntity = FormEntity(id = formId, name = "Form1", pageCount = 1)
        coEvery { formRepository.fetchFormEntity(formId) } returns formEntity

        // When
        val result = fetchFormEntityUseCase(formId)

        // Then
        assertEquals(formEntity, result)
        coVerify(exactly = 1) { formRepository.fetchFormEntity(formId) }
    }
}
