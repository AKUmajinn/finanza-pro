package com.example.finanzapro.adapter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finanzapro.model.UserProfile
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ConfigViewModel : ViewModel() {
    private val database = Firebase.database
    private val profileRef = database.getReference("users/user_123/profile")

    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile: LiveData<UserProfile> = _userProfile

    fun fetchUserData() {
        profileRef.get().addOnSuccessListener { snapshot ->
            val profile = snapshot.getValue(UserProfile::class.java)
            profile?.let { _userProfile.postValue(it) }
        }
    }
}