package com.milsat.core.domain.usecase


import com.milsat.core.data.db.entities.FormEntity
import com.milsat.core.domain.repository.FormRepository
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

@ExperimentalCoroutinesApi
class GetAllFormUseCaseTest {

    private lateinit var getAllFormUseCase: GetAllFormUseCase
    private val formRepository: FormRepository = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getAllFormUseCase = GetAllFormUseCase(formRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke should return flow of all form entities`() = runBlocking {
        // Given
        val formEntities = listOf(
            FormEntity(name = "Form1", pageCount = 2),
            FormEntity(name = "Form2", pageCount = 1)
        )
        every { formRepository.getAllFormEntities() } returns flowOf(formEntities)

        // When
        val resultFlow = getAllFormUseCase()

        // Collect and verify the result
        resultFlow.collect { result ->
            assertEquals(formEntities, result)
        }

        verify(exactly = 1) {
            formRepository.getAllFormEntities()
        }
    }
}
