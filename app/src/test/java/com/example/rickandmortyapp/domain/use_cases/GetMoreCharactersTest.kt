package com.example.rickandmortyapp.domain.use_cases

import com.example.rickandmortyapp.data.result.ErrorEntity
import com.example.rickandmortyapp.domain.repositories.CharacterRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import com.example.rickandmortyapp.data.result.Result
import com.example.rickandmortyapp.domain.model.info.Info
//import com.google.common.truth.ExpectFailure.assertThat
import org.assertj.core.api.Assertions.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test


internal class GetMoreCharactersTest{
    @RelaxedMockK
    private lateinit var repository: CharacterRepository

    lateinit var getMoreCharacters: GetMoreCharacters

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        getMoreCharacters = GetMoreCharacters(repository)
    }

    @Test
    fun `when the name size is greater than constant MIN_NAME_LENGHT`() = runBlocking {
        //Given
        val input = "CharacterName"
        val page = 1
        val expected = Result.Success(Info(pages = 5))
        coEvery { repository.requestMoreCharacters(page, input) } returns expected

        // when
        val result = getMoreCharacters(page,input)

        //Then
        coVerify(exactly = 1) { repository.requestMoreCharacters(page,input) }

        assertEquals(expected.data, result.data)
    }

    @Test
    fun `when the name size is less than the constant MIN_NAME_LENGTH it returns an error`(): Unit = runBlocking {
        // Given
        val input = "C"
        val expectedError = ErrorEntity.InputError.NameError

        val repository = mockk<CharacterRepository>()
        coEvery { repository.requestMoreCharacters(any(), any()) } returns Result.Error2<Info>(expectedError)

        // When
        val getMoreCharacters = GetMoreCharacters(repository)
        val result = getMoreCharacters(1, input)

        // Then
        coVerify(exactly = 0) { repository.requestMoreCharacters(any(), any()) } // No se llama al repositorio
        assertThat(result).isInstanceOf(Result.Error2::class.java)
        assertThat((result as Result.Error2).error).isEqualTo(expectedError)
    }

}