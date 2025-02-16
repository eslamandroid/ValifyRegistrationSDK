package com.valify.registration.data.datasources.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "captured_images")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imageData: String,
)
