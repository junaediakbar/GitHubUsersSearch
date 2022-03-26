package com.juned.githubuserssearch.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy
import com.juned.githubuserssearch.model.User

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM favorite_user ORDER BY username DESC")
    fun getAllFavorite(): LiveData<List<User>>

    @Query("SELECT EXISTS(SELECT username FROM favorite_user WHERE username = :username)")
    fun isFavorite(username: String): LiveData<Boolean>
}