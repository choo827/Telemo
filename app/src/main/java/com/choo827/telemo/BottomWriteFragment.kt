package com.choo827.telemo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_bottom_write.*
import kotlinx.android.synthetic.main.fragment_bottom_write.view.*
import java.util.*


class BottomWriteFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bottom_write, container, false)
        view.toolbar.setNavigationIcon(R.drawable.ic_close_drawer)
        view.toolbar.setNavigationOnClickListener { dismiss() }

        val db = FirebaseFirestore.getInstance()
        val userId = arguments?.getString("userUid")

//        view.phoneNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher())
//
//        view.saveBtn.setOnClickListener {
//            val phoneNumber = view.phoneNumber.text.toString()
//            val name = view.name.text.toString()
//            val etc = view.etc.text.toString()
//
//            val data = PhoneNumber(phoneNumber, name, etc, timeGenerator())
//            db.collection(userId.toString()).document(timeGenerator()).set(data)
////                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
////                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
//
//            view.phoneNumber.text = Editable.Factory.getInstance().newEditable("")
//            view.name.text = Editable.Factory.getInstance().newEditable("")
//
//            dismiss()
//        }
//
//        view.expandBtn.setOnClickListener {
//            dismiss()
//        }



        return view
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window.setLayout(width, height)
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

}
