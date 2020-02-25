package com.example.roomsample

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import androidx.core.view.children
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.human_row.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var db: UserDataBase
    private lateinit var dao: UserDao
    private val mainHandler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //database という名前のデータベースを使用
        db = Room.databaseBuilder(
            applicationContext,
            UserDataBase::class.java,"database"
        ).build()
        dao = db.userDao()
        setAllHumanToTable()

        addButton.setOnClickListener(ButtonListener())
        removeButton.setOnClickListener(ButtonListener())
    }
    //データベース内にあるデータを全て表示します
    private fun setAllHumanToTable(){
        //データベース関連の動作は重いため、メインスレッドで動かしてはいけない決まりがあります
        AsyncTask.execute{
            val humanList = dao.getHumanAll()
            for(human in humanList){
                //全てのhumanをrowに追加
                val row = View.inflate(this@MainActivity,R.layout.human_row,null)
                row.nameTextView.text = human.name
                row.ageTextView.text = human.age.toString()

                //逆に、メインスレッド以外でUIの操作は出来ません
                //なので、このようにしてメインスレッドにViewの追加をお願いしています
                mainHandler.post{
                    humanTable.addView(row)
                }
            }
        }
    }
    private fun resetTable(){
        //humanTableの子を全て参照して
        //先頭以外のrowを全て削除しています
        humanTable.children.forEach {
            if(it is TableRow && it.id != R.id.headRow){
                mainHandler.post{
                    humanTable.removeView(it)
                }
            }
        }
        setAllHumanToTable()
    }
    private fun addHumanFunction(){
        AsyncTask.execute{
            val name = nameInput.text.toString()
            val age = ageInput.text.toString().toIntOrNull() ?: -1
            if(name != "" && age >= 0){
                dao.insertHuman(HumanEntity(id = 0,name = name,age = age))
            }
        }
    }
    private fun removeHumanFunction(){
        AsyncTask.execute{
            val name = nameInput.text.toString()
            val humanList = dao.searchName(name)
            for(human in humanList){
                dao.deleteAllHuman(human)
            }
        }
    }
    inner class ButtonListener:  View.OnClickListener{
        override fun onClick(v: View) {
            AsyncTask.execute {
                when(v.id){
                    R.id.addButton -> addHumanFunction()
                    R.id.removeButton -> removeHumanFunction()
                }
                resetTable()
            }
        }
    }
}