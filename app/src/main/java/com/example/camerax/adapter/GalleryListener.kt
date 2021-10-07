package com.example.camerax.adapter

interface GalleryListener {
    fun onClickListener(id: Int, image: ByteArray, current: String, position: Int)
}