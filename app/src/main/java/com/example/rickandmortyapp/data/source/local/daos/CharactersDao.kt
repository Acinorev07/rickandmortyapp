package com.example.rickandmortyapp.data.source.local.daos

import androidx.room.*
import com.example.rickandmortyapp.data.source.local.model.characters.CachedCharacters
import kotlinx.coroutines.flow.Flow


@Dao
interface CharactersDao {
//Metodo para obtener una lista de personajes que coincidan con un nombre especifico
    @Transaction
    @Query("SELECT * FROM characters WHERE name LIKE  '%' || :name || '%' LIMIT :limit OFFSET :offset")
    fun getCharacter(name:String, limit: Int, offset: Int): Flow<List<CachedCharacters>>

    //Metodo para insertar personajes en la base de datos local
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: List<CachedCharacters>)
}