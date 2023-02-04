package com.example.chitchat.impl

import com.example.chitchat.core.Constants.CREATED_AT
import com.example.chitchat.core.Constants.DISPLAY_NAME
import com.example.chitchat.core.Constants.EMAIL
import com.example.chitchat.core.Constants.PHOTO_URL
import com.example.chitchat.core.Constants.SIGN_IN_REQUEST
import com.example.chitchat.core.Constants.SIGN_UP_REQUEST
import com.example.chitchat.core.Constants.USERS
import com.example.chitchat.model.Response
import com.example.chitchat.repository.AuthRepository
import com.example.chitchat.repository.OneTapSignInResponse
import com.example.chitchat.repository.SignInWithGoogleResponse
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue.serverTimestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val  auth: FirebaseAuth,
    private var oneTapClient:SignInClient,
    @Named(SIGN_IN_REQUEST)
    private var signInRequest:BeginSignInRequest,
    @Named(SIGN_UP_REQUEST)
    private var signUpRequest:BeginSignInRequest,
    private val db: FirebaseFirestore,

): AuthRepository {

    override val isUserAuthenticatedInFirebase = auth.currentUser != null
    override suspend fun oneTapSignInWithGoogle(): OneTapSignInResponse {

        return try {
            val signInResult = oneTapClient.beginSignIn(signInRequest).await()
            Response.Success(signInResult)
        } catch (e: Exception) {
            try {
                val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
                Response.Success(signUpResult)
            } catch (e: Exception) {
                Response.Failure(e)
            }

        }

    }

    override suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): SignInWithGoogleResponse {
        return try {
            val authResult = auth.signInWithCredential(googleCredential).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
            if(isNewUser){
                addUserToFirestore()
            }
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    private suspend fun addUserToFirestore(){
        auth.currentUser?.apply {
            val user = toUser()
            db.collection(USERS).document(uid).set(user).await()
        }
    }
}

fun FirebaseUser.toUser() = mapOf(
    DISPLAY_NAME to displayName,
    EMAIL to email,
    PHOTO_URL to photoUrl?.toString(),
    CREATED_AT to serverTimestamp()
)

















