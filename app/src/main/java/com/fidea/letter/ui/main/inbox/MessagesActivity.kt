package com.fidea.letter.ui.main.inbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.fidea.letter.R

class MessagesActivity : AppCompatActivity() {

    private lateinit var messagesRecycler : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        initMessagesRecycler()
    }

    private fun initMessagesRecycler() {

    }
}
