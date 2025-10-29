package com.example.firstapplication.learn_room_db.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.firstapplication.learn_room_db.data.dao.UserDao
import com.example.firstapplication.learn_room_db.data.entity.User

@Database(version = 1, entities = [User::class], exportSchema = false)
abstract class DataBase : RoomDatabase() {

    abstract fun getUserDao() : UserDao

    companion object {
         @Volatile
         private var INSTANCE: DataBase? = null

        fun getDataBase(appContext: Context): DataBase {
            // If already present then return
            INSTANCE?.let { return it }

            synchronized(this) {
                val tempInstance = Room.databaseBuilder(
                    appContext,
                    DataBase::class.java,
                    "learn_room_db"
                ).build()

                return tempInstance.also { INSTANCE = it }
            }
        }
    }
}