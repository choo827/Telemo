package com.choo827.telemo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.viewholer_people.view.*


class PeopleAdapter(options: FirestoreRecyclerOptions<PhoneNumber>, userUid: String, context: Context) :
    FirestoreRecyclerAdapter<PhoneNumber, PeopleAdapter.PeopleViewHolder>(options) {
    var userUid: String = ""
    var context: Context

    init {
        this.userUid = userUid
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        return PeopleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.viewholer_people, parent, false))
    }

    override fun onBindViewHolder(peopleViewHolder: PeopleViewHolder, position: Int, phoneNumber: PhoneNumber) {
        val db = FirebaseFirestore.getInstance()
        peopleViewHolder.setPhoneBook(phoneNumber.name, phoneNumber.number, phoneNumber.etc)
        if (phoneNumber.etc == "") {
            peopleViewHolder.bottomMargin.visibility = View.VISIBLE
            peopleViewHolder.etcTxt.visibility = View.GONE
        }
        peopleViewHolder.callBtn.setOnClickListener {
            val call = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phoneNumber.number}"))
            context.startActivity(call)
        }
        peopleViewHolder.shareBtn.setOnClickListener {
            val shareType = Intent(Intent.ACTION_SEND)
            shareType.type = "text/plain"
            shareType.putExtra(Intent.EXTRA_TEXT, phoneNumber.number)
            val share = Intent.createChooser(shareType, "연락처 공유")
            context.startActivity(share)

        }
        peopleViewHolder.deleteBtn.setOnClickListener {
            db.collection(userUid).document(phoneNumber.date).delete()
        }

    }

    class PeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val callBtn = itemView.callBtn
        val shareBtn = itemView.shareBtn
        val deleteBtn = itemView.deleteBtn
        val bottomMargin = itemView.bottomMargin
        val etcTxt = itemView.holderEtc

        fun setPhoneBook(phoneName: String, phoneNumber: String, phoneEtc: String) {
            val nameView = itemView.holderName
            val phoneView = itemView.holderNumber
            val etcView = itemView.holderEtc

            nameView.text = phoneName
            phoneView.text = phoneNumber
            etcView.text = phoneEtc
        }
    }


}