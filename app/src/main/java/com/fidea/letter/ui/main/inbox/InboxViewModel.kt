package com.fidea.letter.ui.main.inbox

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fidea.letter.models.Board
import com.fidea.letter.models.Item
import com.fidea.letter.models.Message

class InboxViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    var messages: MutableLiveData<ArrayList<Message>>? = null
}
