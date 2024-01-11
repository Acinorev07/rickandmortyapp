package com.example.rickandmortyapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rickandmortyapp.data.source.local.daos.CharactersDao
import com.example.rickandmortyapp.data.source.local.model.characters.CachedCharacters

@Database(
    entities =[
        CachedCharacters::class
    ],
    version = 1,
    exportSchema = false
)
abstract class RickAndMortyDatabase: RoomDatabase() {

    abstract fun characterDao(): CharactersDao

}