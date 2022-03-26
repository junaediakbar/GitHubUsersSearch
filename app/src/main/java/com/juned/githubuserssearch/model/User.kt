package com.juned.githubuserssearch.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite_user")
@Parcelize
data class User(

    @ColumnInfo(name = "username")
    @PrimaryKey
    var username: String,

    @ColumnInfo(name = "avatarUrl")
    var avatarUrl: String,

    ) : Parcelable