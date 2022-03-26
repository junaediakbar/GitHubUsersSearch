package com.juned.githubuserssearch.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.juned.githubuserssearch.db.FavoriteUserRepository

class FavoriteViewModel(application: Application) : ViewModel() {

    private val userRepository = FavoriteUserRepository.getInstance(application)

    fun getFavorites() = userRepository.getAllFavoriteUsers()

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoriteViewModel(application) as T
        }
    }

}