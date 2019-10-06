package com.choo827.telemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {
    private var adapter: PeopleAdapter? = null
    private var userUid: String = ""
    private var userEmail: String? = ""
    private lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(bottomAppBar)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        val auth = FirebaseAuth.getInstance()
        auth.addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser == null) {
                this.finish()
                startActivity(AuthActivity.getLaunchIntent(this))
            }
        }

        val user = auth.currentUser
        if (user != null) {
            userUid = user.uid
            userEmail = user.email
        }

        addBtn.setOnClickListener {
            val bottomWriteFragment = BottomWriteFragment()
            val bundle = Bundle(1)
            bundle.putString("userUid", userUid)
            bottomWriteFragment.arguments = bundle
            bottomWriteFragment.show(supportFragmentManager, bottomWriteFragment.tag)
            firebaseAnalytics.logEvent("writePhoneNumber", bundle)
        }


        val db = FirebaseFirestore.getInstance()
        val query = db.collection(userUid)
        val options = FirestoreRecyclerOptions.Builder<PhoneNumber>()
            .setQuery(query, PhoneNumber::class.java).build()

        adapter = PeopleAdapter(options, userUid, this)
        rvMain.adapter = adapter
        rvMain.layoutManager = LinearLayoutManager(this)

        query.addSnapshotListener { snapshot, _ ->
            if (snapshot != null) {
                if (snapshot.isEmpty) {
                    noData.visibility = View.VISIBLE
                } else {
                    noData.visibility = View.GONE
                }
            }
        }
    }

//    private fun switchToAddFragment() {
//        supportFragmentManager.beginTransaction().replace(R.id.content_layout, AddFragment()).commit()
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
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

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }
}