package com.example.rickandmortyapp.domain.use_cases

import com.example.rickandmortyapp.data.result.ErrorEntity
import com.example.rickandmortyapp.domain.repositories.CharacterRepository
import com.example.rickandmortyapp.util.MIN_NAME_LENGTH
import com.example.rickandmortyapp.data.result.Result
import javax.inject.Inject

class GetMoreCharacters @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(page:Int, input: String) =
        if (input.length < MIN_NAME_LENGTH){
            Result.Error2(ErrorEntity.InputError.NameError)
        } else{
            repository.requestMoreCharacters(page,input)
        }
}