package com.choo827.telemo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.viewholder_header.view.*
import kotlinx.android.synthetic.main.viewholder_people.view.*

class PeopleAdapter(numberList: ArrayList<PhoneNumber>, userUid: String, context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: ArrayList<PhoneNumber>? = numberList
    private val TYPE_HEADER = 0
    private val TYPE_ITEMS = 1
    var userUid: String = ""
    var context: Context

    init {
        this.userUid = userUid
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.viewholder_header, parent, false)
                HeaderViewHolder(view)
            }
            TYPE_ITEMS -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.viewholder_people, parent, false)
                PeopleViewHolder(view)
            }
            else -> throw RuntimeException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PeopleViewHolder) {
            val db = FirebaseFirestore.getInstance()
            val current = list!![position - 1]

            holder.numberTxt.text = current.number
            holder.nameTxt.text = current.name
            holder.etcTxt.text = current.etc

            if (current.etc == "") {
                holder.bottomMargin.visibility = View.VISIBLE
                holder.etcTxt.visibility = View.GONE
            }

            holder.callBtn.setOnClickListener {
                val call = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${current.number}"))
                context.startActivity(call)
            }
            holder.shareBtn.setOnClickListener {
                val shareType = Intent(Intent.ACTION_SEND)
                shareType.type = "text/plain"
                shareType.putExtra(Intent.EXTRA_TEXT, current.number)
                val share = Intent.createChooser(shareType, "연락처 공유")
                context.startActivity(share)

            }
            holder.deleteBtn.setOnClickListener {
                db.collection(userUid).document(current.date).delete()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0)
            TYPE_HEADER
        else TYPE_ITEMS
    }

    override fun getItemCount(): Int {
        return list!!.size + 1
    }

    class PeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val callBtn = itemView.callBtn
        val shareBtn = itemView.shareBtn
        val deleteBtn = itemView.deleteBtn
        val nameTxt = itemView.holderName
        val numberTxt = itemView.holderNumber
        val etcTxt = itemView.holderEtc
        val bottomMargin = itemView.bottomMargin

    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTxt = itemView.title

    }
}

