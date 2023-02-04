package com.example.chitchat.adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.chitchat.R
import com.example.chitchat.data.Message


class MessageAdapter internal constructor(
    context: Context
):RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    val messageArray = arrayListOf<Message>()
    val inflater = LayoutInflater.from(context)

    inner class MessageViewHolder(itemView: View):ViewHolder(itemView){
        val senderName: TextView = itemView.findViewById(R.id.sender_name)
        val messageView: TextView = itemView.findViewById(R.id.message)
        val messageDateView:TextView = itemView.findViewById(R.id.message_time)
        val layout:LinearLayout = itemView.findViewById(R.id.message_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val itemView = inflater.inflate(R.layout.message_item,parent,false)
        return MessageViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val current = messageArray[position]
        if(current.sender_name == "me"){
            holder.layout.gravity = Gravity.RIGHT
        }
        holder.senderName.text = current.sender_name
        holder.messageView.text = current.message
        holder.messageDateView.text = current.message_date.toString()
    }

    override fun getItemCount(): Int {
        return  messageArray.size
    }

}