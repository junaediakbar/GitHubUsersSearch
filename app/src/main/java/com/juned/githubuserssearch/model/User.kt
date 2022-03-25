package com.juned.githubuserssearch.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var login: String?="",
    var avatarUrl: String?="",
    )  : Parcelable
