package com.choo827.telemo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(bottomAppBar)

        supportFragmentManager.beginTransaction().replace(R.id.content_layout, MainFragment()).commit()

//        val userName = intent.extras.getString("name")
        val userUid = intent.extras.getString("uid")
//        val userEmail = intent.extras.getString("email")

        addBtn.setOnClickListener {
            val bottomWriteFragment = BottomWriteFragment()
            val bundle = Bundle(1)
            bundle.putString("userUid", userUid)
            bottomWriteFragment.arguments = bundle
            bottomWriteFragment.show(supportFragmentManager, bottomWriteFragment.tag)
        }
    }

//    private fun switchToAddFragment() {
//        supportFragmentManager.beginTransaction().replace(R.id.content_layout, AddFragment()).commit()
//    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bottomappbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
                bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
            }
        }

        return true
    }
}
