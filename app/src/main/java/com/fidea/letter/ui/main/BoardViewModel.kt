package com.fidea.letter.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fidea.letter.models.Board
import com.fidea.letter.models.Item

class BoardViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    var boards: MutableLiveData<ArrayList<Board>>? = null
}
