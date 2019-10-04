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
import kotlinx.android.synthetic.main.viewholer_header.view.*
import kotlinx.android.synthetic.main.viewholer_people.view.*


class PeopleAdapter(
    options: FirestoreRecyclerOptions<PhoneNumber>,
    userUid: String,
    context: Context
) :
    FirestoreRecyclerAdapter<PhoneNumber, RecyclerView.ViewHolder>(options) {

    var userUid: String = ""
    var context: Context
    val TYPE_HEADER = 0
    val TYPE_ITEMS = 1

    init {
        this.userUid = userUid
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.viewholer_header, parent, false)
                HeaderViewHolder(view)
            }
            TYPE_ITEMS -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.viewholer_people, parent, false)
                PeopleViewHolder(view)
            }
            else -> throw RuntimeException()
        }

//        return PeopleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.viewholer_people, parent, false))
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        phoneNumber: PhoneNumber
    ) {
//        val current = getItem(position - 1)
        if (holder is PeopleViewHolder) {
            val db = FirebaseFirestore.getInstance()
            holder.setPhoneBook(phoneNumber.name, phoneNumber.number, phoneNumber.etc)
            holder.callBtn.setOnClickListener {
                val call = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phoneNumber.number}"))
                context.startActivity(call)
            }
            holder.shareBtn.setOnClickListener {
                val shareType = Intent(Intent.ACTION_SEND)
                shareType.type = "text/plain"
                shareType.putExtra(Intent.EXTRA_TEXT, phoneNumber.number)
                val share = Intent.createChooser(shareType, "연락처 공유")
                context.startActivity(share)

            }
            holder.deleteBtn.setOnClickListener {
                db.collection(userUid).document(phoneNumber.date).delete()
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionHeader(position)) {
            TYPE_HEADER
        } else {
            TYPE_ITEMS
        }

    }

    fun isPositionHeader(position: Int): Boolean {
        return position == 0
    }

    class PeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val callBtn = itemView.callBtn
        val shareBtn = itemView.shareBtn
        val deleteBtn = itemView.deleteBtn

        fun setPhoneBook(phoneName: String, phoneNumber: String, phoneEtc: String) {
            val nameView = itemView.holderName
            val phoneView = itemView.holderNumber
            val etcView = itemView.holderEtc

            nameView.text = phoneName
            phoneView.text = phoneNumber
            etcView.text = phoneEtc
        }
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTxt = itemView.title

    }


}