package com.example.deliverinator.client

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.deliverinator.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class AccountFragment : Fragment(R.layout.client_fragment_account) {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mStore: FirebaseFirestore
    private lateinit var mFullName: EditText
    private lateinit var mEmail: TextView
    private lateinit var mAddress: EditText
    private lateinit var mPhone: EditText
    private lateinit var mApplyButton: Button
    private lateinit var mChangePassword: TextView
    private lateinit var mProgressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.client_fragment_account, container, false)

        mAuth = FirebaseAuth.getInstance()
        mStore = FirebaseFirestore.getInstance()
        mFullName = view.findViewById(R.id.account_edit_full_name)
        mEmail = view.findViewById(R.id.account_edit_email)
        mAddress = view.findViewById(R.id.account_edit_address)
        mPhone = view.findViewById(R.id.account_edit_phone)
        mApplyButton = view.findViewById(R.id.account_apply_changes)
        mChangePassword = view.findViewById(R.id.account_change_password)
        mProgressBar = view.findViewById(R.id.account_progressBar)

        mChangePassword.setOnClickListener {
            launchChangePassword(it)
        }

        val user = mAuth.currentUser
        val docRef: DocumentReference =
            mStore.collection("Users").document(user!!.uid)

        docRef.get().addOnSuccessListener { docSnap ->
            mFullName.setText(docSnap.getString("FullName"), TextView.BufferType.EDITABLE)
            mEmail.text = docSnap.getString("UserEmail")

            if (docSnap.getString("Address") != null) {
                mAddress.setText(docSnap.getString("Address"), TextView.BufferType.EDITABLE)
            } else {
                mAddress.setText("", TextView.BufferType.EDITABLE)
            }

            mPhone.setText(docSnap.getString("PhoneNumber"), TextView.BufferType.EDITABLE)
        }

        mApplyButton.setOnClickListener {
            applyChanges(docRef)
        }

        return view
    }

    private fun launchChangePassword(view: View) {
        val user = mAuth.currentUser
        val docRef: DocumentReference =
            mStore.collection("Users").document(user!!.uid)

        docRef.get().addOnSuccessListener { docSnap ->
            mAuth.sendPasswordResetEmail(docSnap.getString("UserEmail")!!)
                .addOnSuccessListener {
                    Toast.makeText(context, R.string.reset_link_sent, Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, getString(R.string.link_not_sent) + it.message, Toast.LENGTH_LONG)
                        .show()
                }
        }
    }

    private fun applyChanges(docRef: DocumentReference) {
        val fullName = mFullName.text
        val address = mAddress.text
        val phone = mPhone.text
        docRef.get().addOnSuccessListener { docSnap ->
            mProgressBar.visibility = View.VISIBLE

            if (fullName.toString() == docSnap.getString("FullName") &&
                address.toString() == docSnap.getString("Address") &&
                phone.toString() == docSnap.getString("PhoneNumber")) {

                Toast.makeText(context, "No changes to be made", Toast.LENGTH_SHORT).show()

                mProgressBar.visibility = View.INVISIBLE
            } else {
                val userInfo = HashMap<String, Any>()
                userInfo["FullName"] = fullName.toString()
                userInfo["PhoneNumber"] = phone.toString()
                userInfo["Address"] = address.toString()

                Log.d("aloo", "partially good")

                docRef.update(userInfo).addOnSuccessListener {
                    Toast.makeText(context, "Information has been updated", Toast.LENGTH_SHORT).show()

                    mProgressBar.visibility = View.INVISIBLE
                }
            }
        }
    }

}