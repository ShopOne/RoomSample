package com.example.roomsample

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HumanEntity constructor(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val age: Int
)
