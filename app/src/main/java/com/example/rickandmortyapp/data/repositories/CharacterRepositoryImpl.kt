package com.example.rickandmortyapp.data.repositories

import com.example.rickandmortyapp.data.result.Result
import com.example.rickandmortyapp.data.source.remote.RickAndMortyApi
import com.example.rickandmortyapp.data.source.remote.dto.toCharacter
import com.example.rickandmortyapp.data.source.remote.dto.toListCharacters
import com.example.rickandmortyapp.domain.model.Character
import com.example.rickandmortyapp.domain.model.Characters
import com.example.rickandmortyapp.domain.repositories.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi
): CharacterRepository {
    override fun getCharacters(page: Int): Flow<Result<List<Characters>>> = flow{
       emit(Result.Loading())
        try{
            val response = api.getCharacters(page).toListCharacters()
            emit(Result.Success(response))
        }catch(e: HttpException){
            emit(Result.Error(
                message = "Ooops, something went wrong",
                data = null
            ))
        }catch(e: IOException){

            emit(Result.Error(
                message = "Couldn't reach server, check your internet connection",
                data = null
            ))

        }
    }

    override suspend fun getCharacter(id: Int): Result<Character> {
        val response  = try {
            api.getCharacter(id)
        }catch (e: Exception){
            return Result.Error("An unknown error occurred")
        }
        return Result.Success(response.toCharacter())
    }
}