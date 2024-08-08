package com.milsat.core.domain.usecase
import com.milsat.core.data.db.entities.FormEntity
import com.milsat.core.domain.repository.FormRepository
import com.milsat.core.utils.ErrorResponse
import com.milsat.core.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class CreateNewFormUseCaseTest {

    private lateinit var createNewFormUseCase: CreateNewFormUseCase
    private val formRepository: FormRepository = mockk()

    @Before
    fun setUp() {
        createNewFormUseCase = CreateNewFormUseCase(formRepository)
    }

    @Test
    fun `invoke should return Success when formRepository returns Success`() = runBlocking {
        // Given
        val formEntity = FormEntity(id = 1, name = "Form1", pageCount = 1)
        coEvery { formRepository.createNewForm(any()) } returns Result.Success(formEntity)

        // When
        val result = createNewFormUseCase("")

        // Then
        assertEquals(Result.Success(formEntity), result)
        coVerify(exactly = 1) { formRepository.createNewForm(any()) }
    }

    @Test
    fun `invoke should return Error when formRepository returns Error`() = runBlocking {
        // Given
        val error = Result.Error(ErrorResponse.Unknown())
        coEvery { formRepository.createNewForm(any()) } returns error

        // When
        val result = createNewFormUseCase("")

        // Then
        assertEquals(error, result)
        coVerify(exactly = 1) { formRepository.createNewForm(any()) }
    }
}
