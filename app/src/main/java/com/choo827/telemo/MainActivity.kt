package com.choo827.telemo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.content.ClipData.Item
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot




class MainActivity : AppCompatActivity() {
    private var adapter: PeopleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(bottomAppBar)

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



        val db = FirebaseFirestore.getInstance()
        val query = db.collection(userUid.toString())
        val options = FirestoreRecyclerOptions.Builder<PhoneNumber>()
            .setQuery(query, PhoneNumber::class.java).build()

        adapter = PeopleAdapter(options)
        rvMain.adapter = adapter
        rvMain.layoutManager = LinearLayoutManager(this)
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

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()

        if (adapter != null) {
            adapter!!.stopListening()
        }
    }
}
