package com.example.roomsample

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HumanEntity::class],version = 1)
abstract class UserDataBase: RoomDatabase(){
    abstract fun userDao(): UserDao
}