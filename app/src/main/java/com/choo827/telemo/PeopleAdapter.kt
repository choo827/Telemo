package com.choo827.telemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.viewholer_people.view.*

class PeopleAdapter(options: FirestoreRecyclerOptions<PhoneNumber>) :
    FirestoreRecyclerAdapter<PhoneNumber, PeopleAdapter.PeopleViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        return PeopleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.viewholer_people, parent, false))
    }

    override fun onBindViewHolder(peopleViewHolder: PeopleViewHolder, position: Int, phoneNumber: PhoneNumber) {
        peopleViewHolder.setPhoneBook(phoneNumber.name, phoneNumber.number)
    }

    class PeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setPhoneBook(phoneName: String, phoneNumber: String) {
            val nameView = itemView.holderName
            val phoneView = itemView.holderNumber
            nameView.text = phoneName
            phoneView.text = phoneNumber
        }
    }


}