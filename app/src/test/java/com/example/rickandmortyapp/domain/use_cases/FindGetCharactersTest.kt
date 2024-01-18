package com.example.rickandmortyapp.domain.use_cases

import com.example.rickandmortyapp.domain.model.Characters
import com.example.rickandmortyapp.domain.repositories.CharacterRepository
import io.mockk.MockKAnnotations
import com.example.rickandmortyapp.data.result.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.collections.List
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test



internal class FindGetCharactersTest{

    @RelaxedMockK
    private lateinit var repository: CharacterRepository

    lateinit var findGetCharacters: FindGetCharacters

    @Before
    fun onBefore(){

        MockKAnnotations.init(this)

      findGetCharacters = FindGetCharacters(repository)
    }


    @Test
    fun `when passed the name, limit and offset parameters, it returns a list of characters`(): Unit = runBlocking{
        // Given
        val name = "searchCharacter"
        val limit = 1
        val offset = 0
        val listCharacters = listOf(Characters(1, "searchCharacter", "Human", "image"))
        val expected = Result.Success(listCharacters)
        coEvery { repository.findGetCharacters(name, limit, offset) } returns flowOf(expected)

        // When
        val result =  findGetCharacters(name, limit, offset)

        // Then
        coVerify(exactly = 1) {
            repository.findGetCharacters(name, limit, offset)
        }
        val firstValue = runBlocking { result.first() }
        assertThat(firstValue).isEqualTo(expected)
    }

    @Test
    fun `When the repository returns both error and success, it filters only success`(): Unit = runBlocking {
        // Given
        val name = "errorCharacter"
        val limit = 1
        val offset = 1

        // Mock both Result.Error and Result.Success
        val expectedError = Result.Error<List<Characters>>("Something went wrong")
        val expectedResult = Result.Success<List<Characters>>(listOf(/* Some Characters */))

        coEvery { repository.findGetCharacters(name, limit, offset) } returns flowOf(expectedError, expectedResult)

        // When
        val result = findGetCharacters(name, limit, offset)

        // Then
        coVerify(exactly = 1) {
            repository.findGetCharacters(name, limit, offset)
        }

        // Ensure that only Result.Success is emitted
        assertThat(result.first()).isEqualTo(expectedResult)
    }

    @Test
    fun `It filters successful results`(): Unit = runBlocking {
        // Given
        val name = "searchCharacter"
        val limit = 1
        val offset = 1
        val expectedCharacter = Characters(1, "searchCharacter", "Human", "image")
        val unexpectedCharacter = Characters(2, "otherCharacter", "Alien", "otherImage")
        val repositoryResult = Result.Success(listOf(expectedCharacter, unexpectedCharacter))
        coEvery { repository.findGetCharacters(name, limit, offset) } returns flowOf(repositoryResult)

        // When
        val result = findGetCharacters(name, limit, offset)
        val firstValue = runBlocking { result.first() }

        // Then
        assertThat(firstValue.data).containsExactly(expectedCharacter)
    }
}