package com.juned.githubuserssearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.juned.githubuserssearch.api.ApiConfig
import com.juned.githubuserssearch.api.ListUsersResponse
import com.juned.githubuserssearch.helper.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {

    private val _listUsers = MutableLiveData<List<ListUsersResponse>>()
    val listUsers: LiveData<List<ListUsersResponse>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Event<String>>()
    val error: LiveData<Event<String>> = _error

    private fun createResponseCallback(task: String) = object : Callback<List<ListUsersResponse>> {
        override fun onResponse(
            call: Call<List<ListUsersResponse>>,
            response: Response<List<ListUsersResponse>>,
        ) {
            _isLoading.value = false
            if (response.isSuccessful) {
                _listUsers.value = response.body()
            } else {
                _error.value = Event(task)
                Log.e(TAG, "onFailure: ${response.message()}")
            }
        }
        override fun onFailure(call: Call<List<ListUsersResponse>>, t: Throwable) {
            _isLoading.value = false
            _error.value = Event(task)
            Log.e(TAG, "onFailure: ${t.message}")
        }
    }

    fun getFollowersData(username: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getFollowersUser(username)
            client?.enqueue(createResponseCallback("Followers"))
    }

    fun getFollowingData(username: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getFollowingUser(username)
           client?.enqueue(createResponseCallback("Following"))
    }

    companion object {
        private const val TAG = "FollowViewModel"
    }
}