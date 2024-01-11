package com.example.rickandmortyapp.domain.use_cases

import com.example.rickandmortyapp.data.result.Result
import com.example.rickandmortyapp.domain.model.Characters
import com.example.rickandmortyapp.domain.repositories.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
      private val repository: CharacterRepository
) {

    operator fun invoke (page: Int, name: String = ""): Flow<Result<List<Characters>>>{
        return repository.getCharacters(page, name)
    }
}