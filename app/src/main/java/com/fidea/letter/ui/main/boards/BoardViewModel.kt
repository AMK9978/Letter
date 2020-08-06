package com.fidea.letter.ui.main.boards

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fidea.letter.models.Board
import com.fidea.letter.models.Item
import com.fidea.letter.repositories.BoardRepository
import java.io.File

class BoardViewModel @javax.inject.Inject constructor(
    private val token: String?,
    private val cacheDir: File
) : ViewModel(), LifecycleObserver {

    // TODO: Implement the ViewModel
    var boards: MutableLiveData<ArrayList<Board>> = MutableLiveData()

    init {
        BoardRepository.getRepo()!!.getContents(token, cacheDir)
    }
}
