package com.choo827.telemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.viewholer_people.view.*

class PeopleAdapter(val items: ArrayList<String>, val context: Context) :
    RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        return PeopleViewHolder(LayoutInflater.from(context).inflate(R.layout.viewholer_people, parent, false))
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        holder.name.text = items[position]
        holder.number.text = items[position]

    }

    override fun getItemCount(): Int {
        return items.size
    }


    class PeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.name
        val number = itemView.number
    }

}