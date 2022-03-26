package com.juned.githubuserssearch.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.juned.githubuserssearch.api.ApiConfig
import com.juned.githubuserssearch.api.UserDetailsResponse
import com.juned.githubuserssearch.db.FavoriteUserRepository
import com.juned.githubuserssearch.helper.Event
import com.juned.githubuserssearch.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(private val username: String, application: Application) : ViewModel() {

    private val _userDetails = MutableLiveData<UserDetailsResponse>()
    val userDetails: LiveData<UserDetailsResponse> = _userDetails

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Event<String>>()
    val error: LiveData<Event<String>> = _error

    private val userFavoriteRepository = FavoriteUserRepository.getInstance(application)

    val isFavorite = userFavoriteRepository.isFavorite(username)

    fun setFavorite(user: User, isFavorite: Boolean) {
        if (isFavorite) {
            userFavoriteRepository.insertFavorite(user)
        } else {
            userFavoriteRepository.deleteFavorite(user)
        }
    }

    fun getUserDetails(username: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getUserDetail(username)
        client?.enqueue(createResponseCallback() )
    }
    private fun createResponseCallback() =  object : Callback<UserDetailsResponse> {
        override fun onResponse(
            call: Call<UserDetailsResponse>,
            response: Response<UserDetailsResponse>,
        ) {
            _isLoading.value = false
            if (response.isSuccessful) {
                _userDetails.value = response.body() as UserDetailsResponse
            } else {
                _error.value = Event("User details")
                Log.e(TAG, "onFailure: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<UserDetailsResponse>, t: Throwable) {
            _isLoading.value = false
            _error.value = Event("User details")
            Log.e(TAG, "onFailure: ${t.message}")
        }
    }

    companion object {
        private const val TAG = "DetailUserViewModel"
    }
    @Suppress("UNCHECKED_CAST")
    class Factory(private val username: String, private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DetailUserViewModel(username, application) as T
        }
    }
}