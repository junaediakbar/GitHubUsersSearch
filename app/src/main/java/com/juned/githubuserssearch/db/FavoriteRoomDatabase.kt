package com.juned.githubuserssearch.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.juned.githubuserssearch.model.User

@Database(entities = [User::class], version = 1)
abstract class FavoriteUserRoomDatabase : RoomDatabase() {

    abstract fun favoriteUserDao(): FavoriteUserDao

    companion object {

        @Volatile
        private var INSTANCE: FavoriteUserRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context) = INSTANCE ?: synchronized(FavoriteUserRoomDatabase::class.java) {
            databaseBuilder(context.applicationContext,
                FavoriteUserRoomDatabase::class.java, "favorite_user_database")
                .build()
        }.also { INSTANCE = it }
    }
}