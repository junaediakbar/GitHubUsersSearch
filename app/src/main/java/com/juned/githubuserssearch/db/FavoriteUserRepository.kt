package com.juned.githubuserssearch.db

import android.app.Application
import com.juned.githubuserssearch.model.User
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository private constructor(application: Application) {

    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoriteUserDao()
    }

    fun getAllFavoriteUsers() = mFavoriteUserDao.getAllFavorite()

    fun insertFavorite(user: User) {
        executorService.execute { mFavoriteUserDao.insert(user) }
    }

    fun deleteFavorite(user: User) {
        executorService.execute { mFavoriteUserDao.delete(user) }
    }

    fun isFavorite(username: String) = mFavoriteUserDao.isFavorite(username)

    companion object {
        @Volatile
        private var INSTANCE: FavoriteUserRepository? = null

        @JvmStatic
        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(FavoriteUserRepository::class.java) {
                FavoriteUserRepository(application)
            }.also { INSTANCE = it }
    }
}