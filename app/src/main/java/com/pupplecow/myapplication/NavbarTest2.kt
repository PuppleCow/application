package com.pupplecow.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pupplecow.myapplication.ui.announcement.AnnouncementFragment
import com.pupplecow.myapplication.ui.complaint.ComplaintFragment
import com.pupplecow.myapplication.ui.home.HomeFragment
import com.pupplecow.myapplication.ui.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_navbar_test.*


@Suppress("DEPRECATION")
class NavbarTest2 : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var navbarTestSetting: NavbarTestSetting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navbar_test2)

        bottom_nav_bar.setOnNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.navbar_settings->{
//                val intent=Intent(this,SettingActivity::class.java)
//                startActivity(intent)
                navbarTestSetting= NavbarTestSetting.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.main_frame2,navbarTestSetting).commit()

            }
        }

       return true
    }
}
        /*

        @Suppress("DEPRECATION")
        bottom_nav_bar.setOnNavigationItemSelectedListener (this)

        bottom_nav_bar.selectedItemId=R.id.navbar_home

         */
//
//    override fun onNavigationItemSelected(item: MenuItem):Boolean {
//        when(item.itemId){
//            R.id.navbar_home->{
//                supportFragmentManager.beginTransaction().replace(R.id.main_frame,HomeFragment()).commit()
//            }
//            R.id.navbar_complaint->{
//                supportFragmentManager.beginTransaction().replace(R.id.main_frame,ComplaintFragment()).commit()
//            }
//            R.id.navbar_announcement->{
//                supportFragmentManager.beginTransaction().replace(R.id.main_frame,AnnouncementFragment()).commit()
//            }
//            R.id.navbar_settings->{
//                supportFragmentManager.beginTransaction().replace(R.id.main_frame,SettingsFragment()).commit()
//            }
//        }
//    }

