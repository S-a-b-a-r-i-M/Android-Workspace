package com.example.firstapplication.learn_room_db.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.firstapplication.learn_room_db.data.dao.UserDao
import com.example.firstapplication.learn_room_db.data.entity.Address
import com.example.firstapplication.learn_room_db.data.entity.User

@Database(version = 3, entities = [User::class], exportSchema = false)
@TypeConverters(Converters::class)
abstract class DataBase : RoomDatabase() {

    abstract fun getUserDao() : UserDao

    companion object {
         @Volatile
         private var INSTANCE: DataBase? = null

        fun getInstance(appContext: Context): DataBase {
            // If already present then return
            INSTANCE?.let { return it }

            synchronized(this) {
                val tempInstance = Room.databaseBuilder(
                    appContext,
                    DataBase::class.java,
                    "learn_room_db"
                )
                .addMigrations(migration_1_to_2, migrate_2_to_3)
                .build()

                return tempInstance.also { INSTANCE = it }
            }
        }
    }
}

val migration_1_to_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE ${User.TABLE_NAME} ADD COLUMN ${Address.STREET_COL} Text")
        db.execSQL("ALTER TABLE ${User.TABLE_NAME} ADD COLUMN ${Address.CITY_COL} Text")
        db.execSQL("ALTER TABLE ${User.TABLE_NAME} ADD COLUMN ${Address.PINCODE_COL} Int")
    }
}

val migrate_2_to_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE  ${User.TABLE_NAME} ADD COLUMN ${User.PROFILE_IMAGE_COL} BLOB")
    }
}