package com.example.chitchat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chitchat.R
import com.example.chitchat.data.Chat


class ChatsAdapter internal constructor(
    context: Context
): RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder>() {

    var chatArray:Chat? = null
    val inflater = LayoutInflater.from(context)

    inner class ChatsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val chatName: TextView = itemView.findViewById(R.id.chat_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val itemView = inflater.inflate(R.layout.chat_item,parent,false)
        return ChatsViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        val current = chatArray
        if (current != null) {
            holder.chatName.text = current.chatName
        }

    }

    internal fun setChataArray(chats:Chat?){
        this.chatArray = chats
    }

    override fun getItemCount(): Int {
        return  1
    }

}