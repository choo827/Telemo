package com.choo827.telemo

import android.app.PendingIntent.getActivity
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {
    private var adapter: PeopleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(bottomAppBar)

        val userUid = intent.extras.getString("uid")


        addBtn.setOnClickListener {
            val bottomWriteFragment = BottomWriteFragment()
            val bundle = Bundle(1)
            bundle.putString("userUid", userUid)
            bottomWriteFragment.arguments = bundle
            bottomWriteFragment.show(supportFragmentManager, bottomWriteFragment.tag)
//            showKeyboard(view)
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
                val userEmail = intent.extras.getString("email")
                val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
                val bundle = Bundle(1)
                bundle.putString("userEmail", userEmail)
                bottomNavDrawerFragment.arguments = bundle
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

    fun showKeyboard(view: View?) {
        if (view == null) return
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }
}
