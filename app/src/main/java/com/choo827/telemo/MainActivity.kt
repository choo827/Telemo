package com.choo827.telemo

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_write.closeBtn
import kotlinx.android.synthetic.main.content_write.etc
import kotlinx.android.synthetic.main.content_write.name
import kotlinx.android.synthetic.main.content_write.phoneNumber
import kotlinx.android.synthetic.main.content_write.userAdd
import java.util.*
import kotlin.collections.ArrayList


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
            addBtn.isExpanded = true
            include.visibility = View.GONE
//            firebaseAnalytics.logEvent("writePhoneNumber", bundle)
        }


        val db = FirebaseFirestore.getInstance()
        val query = db.collection(userUid)

        adapter = PeopleAdapter(numberList, userUid, userPhoto, this)
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

        closeBtn.setOnClickListener {
            addBtn.isExpanded = false
            include.visibility = View.VISIBLE

        }
        userAdd.isEnabled = false
        userAdd.setOnClickListener {
            val phoneString = phoneNumber.text.toString()
            val nameString = name.text.toString()
            val etcString = etc.text.toString()

            val data = PhoneNumber(phoneString, nameString, etcString, timeGenerator())
            db.collection(userUid).document(timeGenerator()).set(data)
//                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
//                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

            phoneNumber.text = Editable.Factory.getInstance().newEditable("")
            name.text = Editable.Factory.getInstance().newEditable("")
            etc.text = Editable.Factory.getInstance().newEditable("")
//            firebaseAnalytics.logEvent("addPhoneNumber", bundle)
            addBtn.isExpanded = false
            include.visibility = View.VISIBLE

        }

        phoneNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        phoneNumber.addTextChangedListener(mTextWatcher)
        name.addTextChangedListener(mTextWatcher)
    }

    private val mTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

        override fun afterTextChanged(editable: Editable) {
            checkFieldsForEmptyValues()
        }
    }

    private fun checkFieldsForEmptyValues() {
        val numberString = phoneNumber.text.toString()
        val nameString = name.text.toString()

        if (numberString == "" || nameString == "") {
            userAdd.isEnabled = false
            userAdd.setBackgroundResource(R.drawable.btn_round)
            userAdd.setImageResource(R.drawable.ic_add)
        } else {
            userAdd.isEnabled = true
            userAdd.setBackgroundResource(R.drawable.btn_active_round)
            userAdd.setImageResource(R.drawable.ic_add_active)
        }
    }

    private fun timeGenerator(): String {
        val day = Calendar.getInstance()
        val yearStr = day.get(Calendar.YEAR).toString()
        var monthStr = (day.get(Calendar.MONTH) + 1).toString()
        var dateStr = day.get(Calendar.DATE).toString()
        var hourStr = day.get(Calendar.HOUR).toString()
        var minuteStr = day.get(Calendar.MINUTE).toString()
        var secondStr = day.get(Calendar.SECOND).toString()

        if (monthStr.toInt() < 10) {
            monthStr = "0$monthStr"
        }
        if (dateStr.toInt() < 10) {
            dateStr = "0$dateStr"
        }
        if (hourStr.toInt() < 10) {
            hourStr = "0$hourStr"
        }
        if (minuteStr.toInt() < 10) {
            minuteStr = "0$minuteStr"
        }
        if (secondStr.toInt() < 10) {
            secondStr = "0$secondStr"
        }

        return yearStr + monthStr + dateStr + hourStr + minuteStr + secondStr
    }

//    private fun switchToAddFragment() {
//        supportFragmentManager.beginTransaction().replace(R.id.content_layout, AddFragment()).commit()
//    }

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    override fun onBackPressed() {
        if (addBtn.isExpanded) {
            addBtn.isExpanded = false
            include.visibility = View.VISIBLE

        } else {
            finish()
        }
    }
}