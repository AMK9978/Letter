package com.fidea.letter.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fidea.letter.models.Item

class MainViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    var contents: MutableLiveData<ArrayList<Item>>? = MutableLiveData()

    var page = 1
//    var repo: ItemRepository? = null


}
