package com.example.chitchat.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chitchat.R
import com.example.chitchat.databinding.FragmentRegistrationBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private val TAG = "MainActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient:GoogleSignInClient

    private var _binding:FragmentRegistrationBinding? = null
    private val binding get() = _binding


    private val googleSingInContract = object:ActivityResultContract<String,Int>(){

        override fun createIntent(context: Context, input: String): Intent {
            return googleSignInClient.signInIntent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Int {
            if(resultCode == Activity.RESULT_OK){
                val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
                try{
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e:ApiException){
                    Log.v(TAG,"Google sign failed")
                }
            }
            return resultCode
        }
    }

    private val googleSignIn = registerForActivityResult(googleSingInContract){}



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_registration, container, false)
        _binding = FragmentRegistrationBinding.bind(view)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.my_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(),gso)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        binding?.signInWithGoogle?.findViewById<Button>(R.id.sign_in_with_google)?.setOnClickListener {
            googleSignIn.launch("Hello")
        }
    }

    fun firebaseAuthWithGoogle(idToken:String){
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()){ task->
                if(task.isSuccessful){
                    val user = auth.currentUser
                    updateUI(user)
                }else{
                    updateUI(null)
                }

            }
    }

    fun updateUI(user: FirebaseUser?){
        if(user!=null){
            findNavController().navigate(R.id.action_registrationFragment_to_chatListFragment)
        }

    }

    companion object {
        private var RC_SIGN_IN = 9001
    }

}