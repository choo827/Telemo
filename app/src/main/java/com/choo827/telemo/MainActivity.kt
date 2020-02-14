package com.choo827.telemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {
    private var adapter: PeopleAdapter? = null
    private var userUid: String = ""
    private var userEmail: String? = ""
    private var userPhoto: String = ""
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private val numberList = ArrayList<PhoneNumber>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            userPhoto = user.photoUrl.toString()
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

        adapter = PeopleAdapter(numberList, userUid, userPhoto,this)
        rvMain.adapter = adapter
        rvMain.layoutManager = LinearLayoutManager(this)
        rvMain.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && addBtn.isShown) {
                    addBtn.hide()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    addBtn.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        db.collection(userUid).addSnapshotListener { querySnapshot, _ ->
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

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }
}