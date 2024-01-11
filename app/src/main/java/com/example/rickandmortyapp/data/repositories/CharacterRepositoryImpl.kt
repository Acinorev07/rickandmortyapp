package com.example.rickandmortyapp.data.repositories

import com.example.rickandmortyapp.data.result.ErrorEntity
import com.example.rickandmortyapp.data.result.Result
import com.example.rickandmortyapp.data.source.local.Cache
import com.example.rickandmortyapp.data.source.local.model.characters.CachedCharacters
import com.example.rickandmortyapp.data.source.remote.RickAndMortyApi
import com.example.rickandmortyapp.domain.model.info.Info
import com.example.rickandmortyapp.data.source.remote.dto.toCharacter
import com.example.rickandmortyapp.data.source.remote.dto.toListCharacters
import com.example.rickandmortyapp.data.source.remote.mappers.CharactersMapper
import com.example.rickandmortyapp.data.source.remote.mappers.InfoMapper
import com.example.rickandmortyapp.domain.model.Character
import com.example.rickandmortyapp.domain.model.Characters
import com.example.rickandmortyapp.domain.model.PaginatedCharacters
import com.example.rickandmortyapp.domain.repositories.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi,
    private val cache: Cache,
    private val apiInfoMapper: InfoMapper,
    private val apiCharacterMapper: CharactersMapper

    ): CharacterRepository {
    override fun getCharacters(page: Int, name: String): Flow<Result<List<Characters>>> = flow{
       emit(Result.Loading())
        try{
            val response = api.getCharacters(page, name ).toListCharacters()
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

    // Nuevos miembros
    override fun findGetCharacters(
        input: String,
        limit: Int,
        offset: Int
    ): Flow<Result<List<Characters>>> = flow {
       emit(Result.Loading())
        try{
            cache.getCharacters(
                input,
                limit,
                offset
            ).map{characterList ->
                characterList.map{it.toDomain()}
            }.collect{emit(Result.Success(it))}
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

    override suspend fun requestMoreCharacters(
        pageToLoad: Int,
        name: String
    ): Result<Info> {
       val response = try{
           val (apiInfo, apiResult) = api.getCharacters(pageToLoad, name)
           val paginatedCharacters = PaginatedCharacters(
               info = apiInfoMapper.mapToDomain(apiInfo),
               results = apiResult?.map{
                   apiCharacterMapper.mapToDomain(it)
               }.orEmpty()
           )
           cache.storeCharacters(paginatedCharacters.results.map { CachedCharacters.fronDomain(it) })
           paginatedCharacters.info
       } catch (e: HttpException) {
           return Result.Error2(ErrorEntity.ApiError.UnKnown)
       } catch (e: IOException) {
           return Result.Error2(ErrorEntity.ApiError.NotFound)
       }
        return Result.Success(response)
    }
}