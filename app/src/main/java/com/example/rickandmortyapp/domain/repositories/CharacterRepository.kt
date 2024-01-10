package com.example.rickandmortyapp.domain.repositories

import com.example.rickandmortyapp.data.result.Result
import com.example.rickandmortyapp.domain.model.Character
import com.example.rickandmortyapp.domain.model.Characters
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    fun getCharacters(page:Int): Flow<Result<List<Characters>>>

    suspend fun getCharacter(id:Int): Result<Character>
}