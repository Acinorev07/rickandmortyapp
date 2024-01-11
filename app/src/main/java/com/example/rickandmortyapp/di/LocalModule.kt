package com.example.rickandmortyapp.di

import android.app.Application
import androidx.room.Room
import com.example.rickandmortyapp.data.source.local.Cache
import com.example.rickandmortyapp.data.source.local.RickAndMortyDatabase
import com.example.rickandmortyapp.data.source.local.RoomCache
import com.example.rickandmortyapp.data.source.local.daos.CharactersDao
import com.example.rickandmortyapp.util.DB_NAME
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalModule {
    @Binds
    abstract fun bindCache(cache: RoomCache):Cache

    companion object{
        @Provides
        @Singleton
        fun provideDatabase(app: Application): RickAndMortyDatabase{
            return Room.databaseBuilder(
                app,
                RickAndMortyDatabase::class.java,
                DB_NAME
            ).build()
        }

        @Provides
        fun provideCharacterDao(db: RickAndMortyDatabase): CharactersDao = db.characterDao()
    }
}