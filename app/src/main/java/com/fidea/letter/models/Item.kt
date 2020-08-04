package com.fidea.letter.models

import java.io.Serializable

data class Item(
    var id: Int, var title: String, var description: String, var imageUrl: String,
    var imagePath: String, var rating: Double, var year: Int, var announce: String,
    var summary: String, var votes: Int
) :
    Serializable {

    override fun toString(): String {
        return "Item(id=$id, title='$title', description='$description', imageUrl='$imageUrl')"
    }
}