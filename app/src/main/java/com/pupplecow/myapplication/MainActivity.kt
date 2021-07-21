package com.pupplecow.myapplication


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //로그인
        button.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        //작업장(홈)
        button2.setOnClickListener {
            val intent = Intent(this, HomeActivity1::class.java)
            startActivity(intent)
        }

        //민원접수
        button3.setOnClickListener {
            val intent = Intent(this, ComplaintActivity::class.java)
            startActivity(intent)
        }
        //공지사항
        button4.setOnClickListener {
            val intent = Intent(this, AnnounceMentListActivity::class.java)
            startActivity(intent)
        }
        //설정
        button5.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

    }





}