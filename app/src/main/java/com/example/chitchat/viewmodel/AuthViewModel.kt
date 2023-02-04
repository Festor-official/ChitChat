package com.example.chitchat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitchat.model.Response
import com.example.chitchat.repository.AuthRepository
import com.example.chitchat.repository.OneTapSignInResponse
import com.example.chitchat.repository.SignInWithGoogleResponse
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo:AuthRepository,
    val oneTapClient:SignInClient
): ViewModel() {

    val isUserAuthenticated get() = repo.isUserAuthenticatedInFirebase

    var oneTapSignInResponse:OneTapSignInResponse?  = null
    var signInWithGoogleResponse:SignInWithGoogleResponse? = null

    fun oneTapSignIn() = viewModelScope.launch{
        signInWithGoogleResponse = Response.Loading
        oneTapSignInResponse = repo.oneTapSignInWithGoogle()
    }

    fun signInWithGoogle(googleCredential: AuthCredential) = viewModelScope.launch{
        oneTapSignInResponse = Response.Loading
        signInWithGoogleResponse = repo.firebaseSignInWithGoogle(googleCredential)

    }


}