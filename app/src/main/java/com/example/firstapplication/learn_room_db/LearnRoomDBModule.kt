package com.example.firstapplication.learn_room_db

import android.content.Context
import com.example.firstapplication.learn_room_db.data.DataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class) // ← Lives for entire app
object LearnRoomDBModule {

    @Provides
    fun provideLocalDataBase(@ApplicationContext appContext: Context) = DataBase.getInstance(appContext)

    @Provides
    fun provideUserDao(dataBase: DataBase) = dataBase.getUserDao()
}