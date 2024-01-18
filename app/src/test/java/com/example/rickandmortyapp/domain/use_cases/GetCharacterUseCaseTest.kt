package com.example.rickandmortyapp.domain.use_cases

import com.example.rickandmortyapp.data.source.remote.dto.Location
import com.example.rickandmortyapp.data.source.remote.dto.Origin
import com.example.rickandmortyapp.domain.model.Character
import com.example.rickandmortyapp.domain.repositories.CharacterRepository
import com.example.rickandmortyapp.data.result.Result
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test


internal class GetCharacterUseCaseTest{

    @RelaxedMockK
    private lateinit var repository: CharacterRepository

    lateinit var getCharacterUseCase: GetCharacterUseCase

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        getCharacterUseCase = GetCharacterUseCase(repository)
    }

    @Test
    fun `When an id is passed to the use case, the repository will return the record that belongs to that id and no other`(): Unit =
        runBlocking {
            // Given
            val id1: Int = 1
            val id2: Int = 2
            val origin = Origin("Tierra", "Earth URL")
            val location = Location("Earth", "Earth URL")

            val characterId1 = Character(
                1, "nameCharacter1", "Live", "Human", "Men", origin, location, "image1"
            )
            val characterId2 = Character(
                2, "nameCharacter2", "Live", "Animal", "Women", origin, location, "image2"
            )

            coEvery { repository.getCharacter(id1) } returns Result.Success(characterId1)
            coEvery { repository.getCharacter(id2) } returns Result.Success(characterId2)

            // When
            val result1 = getCharacterUseCase(id1)
            val result2 = getCharacterUseCase(id2)

            // Then
            coVerify(exactly = 1) {
                repository.getCharacter(id1)
            }

            coVerify(exactly = 1) {
                repository.getCharacter(id2)
            }

            assertThat(result1.data).isEqualTo(Result.Success(characterId1).data)
            assertThat(result2.data).isEqualTo(Result.Success(characterId2).data)
        }
}