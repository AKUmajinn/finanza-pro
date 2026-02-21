package com.example.finanzapro.adapter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzapro.model.UserProfile
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ConfigViewModel : ViewModel() {
    private val database = Firebase.database
    private val profileRef = database.getReference("users")

    private val _userProfile = MutableLiveData<UserProfile?>()
    val userProfile: LiveData<UserProfile?> = _userProfile

    fun fetchUserData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val snapshot = profileRef.child("$userId/profile").get().await()
                val profile = snapshot.getValue(UserProfile::class.java)
                _userProfile.postValue(profile)
            } catch (e: Exception) {
                e.printStackTrace()
                _userProfile.postValue(null)
            }
        }
    }

    fun updateBudget(userId: String, newBudget: Double, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                profileRef.child("$userId/profile/monthly_budget").setValue(newBudget).await()

                val currentProfile = _userProfile.value
                if (currentProfile != null) {
                    _userProfile.postValue(currentProfile.copy(monthly_budget = newBudget))
                } else {
                    fetchUserData(userId)
                }

                launch(Dispatchers.Main) { onResult(true) }
            } catch (e: Exception) {
                e.printStackTrace()
                launch(Dispatchers.Main) { onResult(false) }
            }
        }
    }
}