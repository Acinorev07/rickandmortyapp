package com.example.rickandmortyapp.data.source.local

import com.example.rickandmortyapp.data.source.local.daos.CharactersDao
import com.example.rickandmortyapp.data.source.local.model.characters.CachedCharacters
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomCache @Inject constructor(
    private val charactersDao: CharactersDao
): Cache {
    override fun getCharacters(
        input: String,
        limit: Int,
        offset: Int
    ): Flow<List<CachedCharacters>> {
        return charactersDao.getCharacter(input, limit, offset)
    }

    override suspend fun storeCharacters(characters: List<CachedCharacters>) {
        charactersDao.insertCharacter(characters)
    }
}