package com.choo827.telemo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.choo827.telemo.databinding.ViewholderHeaderBinding
import com.choo827.telemo.databinding.ViewholderPeopleBinding
import com.google.firebase.firestore.FirebaseFirestore

class PeopleAdapter(
    numberList: ArrayList<PhoneNumber>,
    userUid: String,
    userUrl: String,
    context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: ArrayList<PhoneNumber>? = numberList
    private val TYPE_HEADER = 0
    private val TYPE_ITEMS = 1
    var userUid: String = ""
    var userUrl: String = ""
    var context: Context

    init {
        this.userUid = userUid
        this.userUrl = userUrl
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val binding = ViewholderHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HeaderViewHolder(binding)
            }

            TYPE_ITEMS -> {
                val binding = ViewholderPeopleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                PeopleViewHolder(binding)
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
                notifyItemRemoved(position)
            }
        } else {
            Glide.with(context)
                .load(userUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.itemView.findViewById(R.id.photo))
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

    class PeopleViewHolder(binding: ViewholderPeopleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val callBtn = binding.callBtn
        val shareBtn = binding.shareBtn
        val deleteBtn = binding.deleteBtn
        val nameTxt = binding.holderName
        val numberTxt = binding.holderNumber
        val etcTxt = binding.holderEtc
        val bottomMargin = binding.bottomMargin

    }

    class HeaderViewHolder(binding: ViewholderHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}

