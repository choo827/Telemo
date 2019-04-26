package com.choo827.telemo


import android.content.Context
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_bottom_write.*
import kotlinx.android.synthetic.main.fragment_bottom_write.view.*
import java.util.*


class BottomWriteFragment : RoundedBottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bottom_write, container, false)

        val db = FirebaseFirestore.getInstance()
        val userId = arguments?.getString("userUid")

        view.phoneNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        view.saveBtn.setOnClickListener {
            val phoneNumber = view.phoneNumber.text.toString()
            val name = view.name.text.toString()
            val etc = view.etc.text.toString()

            val data = PhoneNumber(phoneNumber, name, etc, timeGenerator())
            db.collection(userId.toString()).document(timeGenerator()).set(data)
//                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
//                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

            view.phoneNumber.text = Editable.Factory.getInstance().newEditable("")
            view.name.text = Editable.Factory.getInstance().newEditable("")

            dismiss()
        }

        view.expandBtn.setOnClickListener {
            dismiss()
        }



        return view
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


}
