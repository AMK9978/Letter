package com.fidea.letter.models

import java.io.Serializable

class Item(var id: Int, var title: String, var description: String, var imageUrl: String) :
    Serializable {

    override fun toString(): String {
        return "Item(id=$id, title='$title', description='$description', imageUrl='$imageUrl')"
    }
}