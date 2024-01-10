package com.example.rickandmortyapp.domain.use_cases

import com.example.rickandmortyapp.data.result.Result
import com.example.rickandmortyapp.domain.repositories.CharacterRepository
import com.example.rickandmortyapp.domain.model.Character
import javax.inject.Inject


class GetCharacterUseCase @Inject  constructor(
    private val repository: CharacterRepository
){

    suspend operator fun invoke (id:Int): Result<Character>{
        return repository.getCharacter(id)
    }

}