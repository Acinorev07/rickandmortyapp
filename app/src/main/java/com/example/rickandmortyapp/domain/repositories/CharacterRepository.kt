package com.example.rickandmortyapp.domain.repositories

import com.example.rickandmortyapp.data.result.Result

import com.example.rickandmortyapp.domain.model.Character
import com.example.rickandmortyapp.domain.model.Characters
import com.example.rickandmortyapp.domain.model.info.Info
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharacters(page:Int, name: String): Flow<Result<List<Characters>>>
    suspend fun getCharacter(id:Int): Result<Character>
    fun findGetCharacters(input: String, limit: Int, offset:Int):Flow<Result<List<Characters>>>
    suspend fun requestMoreCharacters(pageToLoad:Int, name:String): Result<Info>
}