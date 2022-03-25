package com.juned.githubuserssearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.juned.githubuserssearch.api.ApiConfig
import com.juned.githubuserssearch.api.ListUsersResponse
import com.juned.githubuserssearch.api.UsersResponse
import com.juned.githubuserssearch.helper.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


    class MainViewModel : ViewModel() {

        private val _listUsers = MutableLiveData<List<ListUsersResponse>>()
        val listUsers: LiveData<List<ListUsersResponse>> = _listUsers

        private val _totalCount = MutableLiveData<Int>()
        val totalCount : LiveData<Int> = _totalCount

        private val _isLoading = MutableLiveData<Boolean>()
        val isLoading: LiveData<Boolean> = _isLoading

        private val _error = MutableLiveData<Event<String>>()
        val error: LiveData<Event<String>> = _error

        private var lastQuery: String = emptyQuery

        init {
            searchUsers()
        }

        private fun searchUsers() {
            searchUsers(lastQuery)
        }

        fun searchUsers(query: String) {
            lastQuery = query
            _isLoading.value = true

            val client = ApiConfig.getApiService().getUsers(query)
            client?.enqueue(createResponseCallback())
        }

        private fun createResponseCallback() = object : Callback<UsersResponse> {
            override fun onResponse(
                call: Call<UsersResponse>,
                response: Response<UsersResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listUsers.value = response.body()!!.items as List<ListUsersResponse>
                        _totalCount.value = response.body()!!.totalCount!!
                    }
                } else {
                    _error.value = Event("Search User")
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UsersResponse>, t: Throwable)
            {
                _isLoading.value = false
                _error.value = Event("Search User")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        }

        companion object {
            private const val TAG = "MainViewModel"
            const val emptyQuery = "\"\""
        }
    }
