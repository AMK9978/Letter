package com.fidea.letter.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.fidea.letter.models.Item
import com.fidea.letter.repositories.ItemRepository
import java.io.File
import javax.inject.Inject

class ItemsViewModel @Inject constructor(
    private val token: String?,
    private val cacheDir: File
) : ViewModel() {

    private var page = 0
    var items: LiveData<ArrayList<Item>> =
        ItemRepository.getRepo()!!.getContents(token, cacheDir, page++)
    val favorites: LiveData<ArrayList<Item>> = MutableLiveData<ArrayList<Item>>()

    fun getNewItems() {
        items = ItemRepository.getRepo()!!.getContents(token, cacheDir, page++)
    }

    fun getFavorites() {
        favorites.value?.addAll(ItemRepository.getRepo()!!.getFavorites(token, cacheDir).value!!)
    }
}
