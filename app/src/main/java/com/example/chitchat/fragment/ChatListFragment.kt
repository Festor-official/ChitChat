package com.example.chitchat.fragment

import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chitchat.R
import com.example.chitchat.adapter.ChatsAdapter
import com.example.chitchat.data.Chat
import com.example.chitchat.data.User
import com.example.chitchat.databinding.FragmentChatListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class ChatListFragment : Fragment() {



    private val TAG = "MainActivity"
    private lateinit var auth: FirebaseAuth
    private var _binding:FragmentChatListBinding? = null
    private val binding get() = _binding
    val db = Firebase.firestore


    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat_list, container, false)
        _binding = FragmentChatListBinding.bind(view)
        return  view
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val adapter = ChatsAdapter(requireContext())
        val docRef = db.collection("users").document("users")

        docRef.get()
            .addOnSuccessListener { documentSnapshot->
                if(documentSnapshot!=null){
                    val chats = documentSnapshot.toObject<Chat>()
                    adapter.setChataArray(chats)
                }
            }
        auth = Firebase.auth

    }


    fun updateUI(user: FirebaseUser?){
        if(user==null){
            findNavController().navigate(R.id.action_chatListFragment_to_registrationFragment)
        }
        binding?.userName?.text  = user?.email ?: "Error"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}












