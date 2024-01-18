package com.example.rickandmortyapp.domain.use_cases


import com.example.rickandmortyapp.domain.model.Characters
import com.example.rickandmortyapp.domain.repositories.CharacterRepository
import com.example.rickandmortyapp.data.result.Result
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test


internal class GetCharactersUseCaseTest{

    @RelaxedMockK
    private lateinit var repository: CharacterRepository

    lateinit var getCharacters: GetCharactersUseCase

    @Before
    fun onBefores(){
        MockKAnnotations.init(this)
        getCharacters = GetCharactersUseCase(repository)
    }

   /* @Test
    fun `when passed an integer for the page, this function should return a list of characters from that page`(): Unit = runBlocking {

        // Given
        val name: String = " "
        val page1: Int = 1
        val page2: Int = 2
        val charactersPage1 = listOf(
            Characters(1, "nameCharacter1", "Human", "image1"),
            Characters(2, "nameCharacter2", "Alien", "image2"),
            Characters(3, "nameCharacters3", "Animal", "image3")
        )
        val charactersPage2 = listOf(
            Characters(4, "nameCharacter4", "Human", "image1"),
            Characters(5, "nameCharacter5", "Alien", "image2"),
            Characters(6, "nameCharacters6", "Animal", "image3")
        )
        val expected1 = Result.Success(charactersPage1)
        val expected2 = Result.Success(charactersPage2)

        coEvery { repository.getCharacters(page1, name) } returns flowOf(expected1)
        coEvery { repository.getCharacters(page2, name) } returns flowOf(expected2)


        // When
        val result1 = getCharacters(page1, name).toList().single()
        val result2 = getCharacters(page2, name).toList().single()

        // Then
        coVerify(exactly = 1) { repository.getCharacters(page1, name) }
        coVerify(exactly = 1) { repository.getCharacters(page2, name) }
        assertThat(result1.data).isEqualTo(expected1.data)
        assertThat(result2.data).isEqualTo(expected2.data)
    }*/

    @Test
    fun `when passed an integer for the page, this function should return a list of characters from that page`() = runBlocking {

        // Given
        val name: String = " "
        val page1: Int = 1
        val page2: Int = 2
        val charactersPage1 = listOf(
            Characters(1, "nameCharacter1", "Human", "image1"),
            Characters(2, "nameCharacter2", "Alien", "image2"),
            Characters(3, "nameCharacters3", "Animal", "image3")
        )
        val charactersPage2 = listOf(
            Characters(4, "nameCharacter4", "Human", "image1"),
            Characters(5, "nameCharacter5", "Alien", "image2"),
            Characters(6, "nameCharacters6", "Animal", "image3"),
            // Add an additional character for the second page
            Characters(7, "nameCharacter7", "Human", "image4")
        )
        val expected1 = Result.Success(charactersPage1)
        val expected2 = Result.Success(charactersPage2)

        coEvery { repository.getCharacters(page1, name) } returns flowOf(expected1)
        coEvery { repository.getCharacters(page2, name) } returns flowOf(expected2)

        // When
        val result1 = getCharacters(page1, name).toList().single()
        val result2 = getCharacters(page2, name).toList().single()

        // Then
        coVerify(exactly = 1) { repository.getCharacters(page1, name) }
        coVerify(exactly = 1) { repository.getCharacters(page2, name) }
        assertThat(result1.data).isEqualTo(expected1.data)
        assertThat(result2.data).isEqualTo(expected2.data)
       // println("Test completed successfully for both pages.")
        // Print the message to the console
        // Additional verification for separate calls
        coVerifyOrder {
            repository.getCharacters(page1, name)
            repository.getCharacters(page2, name)
        }

        // Print the message to the console
        System.out.println("Test completed successfully for both pages.")
    }
}