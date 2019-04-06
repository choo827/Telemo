package com.choo827.telemo


import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_bottom_write.view.*


class BottomWriteFragment : RoundedBottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bottom_write, container, false)

        val db = FirebaseFirestore.getInstance()
        val userId = arguments?.getString("userUid")

        view.saveBtn.setOnClickListener {
            val phoneNumber = view.phoneNumber.text.toString()
            val name = view.name.text.toString()

            val data = PhoneNumber(phoneNumber, name)
            db.collection(userId.toString()).add(data)
//                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
//                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

            view.phoneNumber.text = Editable.Factory.getInstance().newEditable("")
            view.name.text = Editable.Factory.getInstance().newEditable("")
        }

        return view
    }


}
