package com.example.camerax.ui

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "time")
    var time: String,

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var data: ByteArray)