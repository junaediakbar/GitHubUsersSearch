package com.juned.githubuserssearch.api

import com.google.gson.annotations.SerializedName

data class UsersResponse(

	@field:SerializedName("total_count")
	val totalCount: Int? = null,

	@field:SerializedName("items")
	val items: List<ListUsersResponse?>? = null
)

data class ListUsersResponse(

	@field:SerializedName("login")
	val login: String? = null,

	@field:SerializedName("avatar_url")
	val avatarUrl: String? = null,

)
