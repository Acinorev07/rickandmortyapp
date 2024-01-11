package com.example.rickandmortyapp.domain.use_cases

import com.example.rickandmortyapp.domain.model.Characters
import com.example.rickandmortyapp.data.result.Result
import com.example.rickandmortyapp.domain.repositories.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FindGetCharacters @Inject constructor(
    private val repository: CharacterRepository
) {
    operator fun invoke(name: String, limit: Int, offset: Int): Flow<Result<List<Characters>>> {
        return repository.findGetCharacters(name, limit, offset)
            .filter { it is Result.Success }
            .map { it as Result.Success<List<Characters>> }
    }
}