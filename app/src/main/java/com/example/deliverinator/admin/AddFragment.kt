package com.example.deliverinator.admin

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.deliverinator.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class AddFragment : Fragment() {
    private lateinit var mName: EditText
    private lateinit var mEmail: EditText
    private lateinit var mPassword: EditText
    private lateinit var mConfirmPassword: EditText
    private lateinit var mPhone: EditText
    private lateinit var mAdminAddBtn: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mStore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.admin_fragment_add, container, false)

        mName = view.findViewById(R.id.admin_add_name)
        mEmail = view.findViewById(R.id.admin_add_email)
        mPassword = view.findViewById(R.id.admin_add_password)
        mConfirmPassword = view.findViewById(R.id.admin_add_confirm_password)
        mPhone = view.findViewById(R.id.admin_add_phone)
        mAdminAddBtn = view.findViewById(R.id.admin_add_button)
        mProgressBar = view.findViewById(R.id.admin_progressBar)
        mAuth = FirebaseAuth.getInstance()
        mStore = FirebaseFirestore.getInstance()

        mAdminAddBtn.setOnClickListener {
            addRestaurant()
        }

        return view
    }

    private fun addRestaurant() {
        val name = mName.text.toString().trim()
        val email = mEmail.text.toString().trim()
        val password = mPassword.text.toString().trim()
        val confirmPassword = mConfirmPassword.text.toString().trim()
        val phone = mPhone.text.toString().trim()

        if (TextUtils.isEmpty(name)) {
            mName.error = getString(R.string.empty_field)
            return
        }

        if (!Utils.isValidEmail(email)) {
            mEmail.error = getString(R.string.invalid_email)
            return
        }

        if (!Utils.isValidPhone(phone)) {
            mPhone.error = getString(R.string.invalid_phone)
            return
        }

        if (!Utils.isValidPassword(password)) {
            mPassword.error = getString(R.string.register_invalid_password)
            return
        }

        if (confirmPassword != password) {
            mConfirmPassword.error = getString(R.string.register_invalid_confirm_password)
            return
        }

        mProgressBar.visibility = View.VISIBLE

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = mAuth.currentUser
                user?.sendEmailVerification()
                    ?.addOnSuccessListener {
                        Toast.makeText(activity, R.string.verification_mail_sent, Toast.LENGTH_SHORT)
                            .show()
                    }
                    ?.addOnFailureListener { e ->
                        Toast.makeText(activity, getString(R.string.link_not_sent) + e.message, Toast.LENGTH_SHORT)
                            .show()
                    }
            } else {
                Toast.makeText(activity, R.string.user_registered, Toast.LENGTH_SHORT).show()
            }
        }.addOnSuccessListener {
            val user = mAuth.currentUser

            if (user != null) {
                val docRef = mStore.collection(USERS).document(user.uid)
                val userInfo = HashMap<String, Any>()

                userInfo[NAME] = name
                userInfo[EMAIL] = email
                userInfo[PHONE_NUMBER] = phone

                // 0 means admin, 1 means user, 2 means restaurant
                userInfo[USER_TYPE] = "2"

                docRef.set(userInfo)

                val restaurantRef = mStore.collection(RESTAURANTS).document(user.uid)
                val restaurantInfo = HashMap<String, Any>()

                restaurantInfo[NAME] = name
                restaurantInfo[RESTAURANT_DESCRIPTION] = ""
                restaurantRef.set(restaurantInfo)

                mProgressBar.visibility = View.INVISIBLE
            }
        }
    }
}


