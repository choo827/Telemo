package com.choo827.telemo


import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_bottom_write.*
import kotlinx.android.synthetic.main.fragment_bottom_write.view.*
import java.util.*


class BottomWriteFragment : DialogFragment() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    var bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
        firebaseAnalytics = FirebaseAnalytics.getInstance(context!!)
//        dialog?.window?.attributes?.windowAnimations = R.style.FullScreenDialogStyle

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bottom_write, container, false)
        view.closeBtn.setOnClickListener { dismiss() }
        view.userAdd.isEnabled = false
        view.userAdd.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            val userId = arguments?.getString("userUid")
            val phoneNumber = view.phoneNumber.text.toString()
            val name = view.name.text.toString()
            val etc = view.etc.text.toString()

            val data = PhoneNumber(phoneNumber, name, etc, timeGenerator())
            db.collection(userId.toString()).document(timeGenerator()).set(data)
//                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
//                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

            view.phoneNumber.text = Editable.Factory.getInstance().newEditable("")
            view.name.text = Editable.Factory.getInstance().newEditable("")
            firebaseAnalytics.logEvent("addPhoneNumber", bundle)

            dismiss()
        }

        view.phoneNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        view.phoneNumber.addTextChangedListener(mTextWatcher)
        view.name.addTextChangedListener(mTextWatcher)

        return view
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setWindowAnimations(R.style.AppTheme_Slide)
        }
    }

    private val mTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

        override fun afterTextChanged(editable: Editable) {
            checkFieldsForEmptyValues()
        }
    }

    private fun checkFieldsForEmptyValues() {
        val numberString = phoneNumber.text.toString()
        val nameString = name.text.toString()

        if (numberString == "" || nameString == "") {
            userAdd.isEnabled = false
            userAdd.setBackgroundResource(R.drawable.btn_round)
            userAdd.setImageResource(R.drawable.ic_add)
        } else {
            userAdd.isEnabled = true
            userAdd.setBackgroundResource(R.drawable.btn_active_round)
            userAdd.setImageResource(R.drawable.ic_add)
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