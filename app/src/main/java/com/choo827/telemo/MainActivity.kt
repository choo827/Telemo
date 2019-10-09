package com.choo827.telemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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
    private val numberList = ArrayList<PhoneNumber>()


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

        adapter = PeopleAdapter(numberList, userUid, this)
        rvMain.adapter = adapter
        rvMain.layoutManager = LinearLayoutManager(this)

        db.collection(userUid).addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            numberList.clear()
            for (item in querySnapshot!!.documents) {
                val phoneNumber = item.toObject(PhoneNumber::class.java)
                numberList.add(phoneNumber!!)
            }
            rvMain.adapter!!.notifyDataSetChanged()
        }


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

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }
}