package com.example.roomsample

import androidx.room.*

@Dao
interface UserDao{
    //データベース上の全てのHumanEntityを取得
    @Query("SELECT * FROM HumanEntity")
    //データベース上にあるhumanNameさんを全て取得
    fun getHumanAll(): List<HumanEntity>
    @Query("SELECT * FROM HumanEntity WHERE name == :humanName")
    fun searchName(humanName: String): List<HumanEntity>
    //humansを全て取得、primaryKey重複の際には入れ替え
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHuman(vararg humans: HumanEntity)
    //humansを全て消去
    @Delete
    fun deleteAllHuman(vararg humans: HumanEntity)
}