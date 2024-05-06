package com.example.umc01

data class Album(
    var  title: String? = "",
    var singer : String? = "",
    var coverImg: Int? = null,
    var songs: ArrayList<Song>? = null
)
