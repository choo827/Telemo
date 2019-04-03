package com.choo827.telemo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_bottom_write.*
import kotlinx.android.synthetic.main.fragment_bottom_write.view.*


class BottomWriteFragment : RoundedBottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bottom_write, container, false)

        val db = FirebaseFirestore.getInstance()

        view.saveBtn.setOnClickListener {
            view.phoneNumber.text
        }

        return view
    }


}
