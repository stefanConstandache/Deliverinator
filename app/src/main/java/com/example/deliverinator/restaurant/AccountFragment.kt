package com.example.deliverinator.restaurant

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.deliverinator.*
import com.example.deliverinator.Utils.Companion.hideKeyboard
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.restaurant_fragment_account.view.*

class AccountFragment : Fragment() {
    private lateinit var mAccountImageView: ImageView
    private lateinit var mRestaurantName: TextView
    private lateinit var mDescription: TextView
    private lateinit var mChangePassword: Button
    private lateinit var mChooseImage: Button
    private lateinit var mApplyChanges: Button
    private lateinit var mDeleteAccount: Button
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mStore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.restaurant_fragment_account, container, false)

        mAccountImageView = view.account_fragment_imageView
        mRestaurantName = view.account_fragment_name
        mDescription = view.account_fragment_description
        mChangePassword = view.account_fragment_change_button
        mChooseImage = view.account_fragment_choose_button
        mApplyChanges = view.account_fragment_apply_changes
        mDeleteAccount = view.account_fragment_delete_account_button
        mProgressBar = view.account_fragment_progressBar
        mAuth  = FirebaseAuth.getInstance()
        mStore = FirebaseFirestore.getInstance()

        val user = mAuth.currentUser
        val docRestaurantsRef = mStore.collection(RESTAURANTS).document(user!!.uid)
        val docUsersRef = mStore.collection(USERS).document(user.uid)

        docRestaurantsRef.get().addOnSuccessListener {
            mRestaurantName.text = it.getString(NAME)
            if (it.getString(RESTAURANT_DESCRIPTION) != "") {
                mDescription.text = it.getString(RESTAURANT_DESCRIPTION)
            }
        }

        mChooseImage.setOnClickListener {
            chooseImage()
        }

        mApplyChanges.setOnClickListener {
            applyChanges(docRestaurantsRef, docUsersRef)
        }

        mChangePassword.setOnClickListener {
            sendChangePasswordEmail()
        }

        mDeleteAccount.setOnClickListener {
            deleteAccount(view)
        }

        return view
    }

    private fun deleteAccount(view: View) {
        val alertDialogContext = context
        val layout = LinearLayout(context)
        val deleteDialog = AlertDialog.Builder(view.context)
        val user = mAuth.currentUser
        val mailField = EditText(alertDialogContext)
        val passwordField = EditText(alertDialogContext)

        layout.orientation = LinearLayout.VERTICAL

        mailField.hint = "Email"
        passwordField.hint = "Password"
        passwordField.transformationMethod = PasswordTransformationMethod.getInstance()

        layout.addView(mailField)
        layout.addView(passwordField)

        deleteDialog.setView(layout)

        deleteDialog
            .setTitle("Delete Account")
            .setMessage("Enter credentials to delete account.")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Delete Account") { _, _ ->
                val mail = mailField.text.toString().trim()
                val password = passwordField.text.toString().trim()
                val credential = EmailAuthProvider.getCredential(mail, password)

                user!!.reauthenticate(credential).addOnSuccessListener {
                    mStore.collection(RESTAURANTS).whereEqualTo("Email", mail)
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                mStore.collection(RESTAURANTS).document(document.id).delete()
                            }
                        }

                    mStore.collection(USERS).whereEqualTo("Email", mail)
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                mStore.collection(USERS).document(document.id).delete()
                            }
                        }

                    user.delete().addOnSuccessListener {
                        Toast.makeText(context, R.string.user_deleted, Toast.LENGTH_SHORT).show()

                        mAuth.signOut()

                        val intent = Intent(context, Login::class.java)
                        startActivity(intent)
                    }
                } .addOnFailureListener {
                    Toast.makeText(context, "Wrong credentials", Toast.LENGTH_SHORT).show()
                }
            }
            .create()
            .show()
    }

    private fun applyChanges(docRestaurantRef: DocumentReference, docUsersRef: DocumentReference) {
        val restaurantName = mRestaurantName.text
        val description = mDescription.text

        docRestaurantRef.get().addOnSuccessListener { docSnap ->
            if (restaurantName.toString() == docSnap.getString(NAME) &&
                description.toString() == docSnap.getString(ADDRESS)
            ) {
                mProgressBar.visibility = View.VISIBLE

                Toast.makeText(context, R.string.no_changes, Toast.LENGTH_SHORT).show()

                mProgressBar.visibility = View.INVISIBLE
            } else {
                if (restaurantName.isEmpty()) {
                    mRestaurantName.error = getString(R.string.empty_field)
                    return@addOnSuccessListener
                }

                if (description.isEmpty()) {
                    mDescription.error = getString(R.string.empty_field)
                    return@addOnSuccessListener
                }

                mProgressBar.visibility = View.VISIBLE

                val restaurantInfo = HashMap<String, Any>()

                restaurantInfo[NAME] = restaurantName.toString()
                restaurantInfo[RESTAURANT_DESCRIPTION] = description.toString()

                hideKeyboard()

                docRestaurantRef.update(restaurantInfo).addOnSuccessListener {
                    docUsersRef.update(restaurantInfo).addOnSuccessListener {
                        Toast.makeText(context, R.string.updated_account, Toast.LENGTH_SHORT).show()

                        mProgressBar.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    private fun chooseImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permission = context?.let { context ->
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            if (permission == PackageManager.PERMISSION_DENIED) {
                // Request permission
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

                // Show pop-up
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                chooseImageFromGallery()
            }
        } else {
            chooseImageFromGallery()
        }
    }

    private fun sendChangePasswordEmail() {
        mAuth.sendPasswordResetEmail(mAuth.currentUser!!.email!!)
            .addOnSuccessListener {
                Toast.makeText(context, R.string.reset_link_sent, Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnFailureListener {
                Toast.makeText(context, getString(R.string.link_not_sent) + it.message, Toast.LENGTH_LONG)
                    .show()
            }
    }

    private fun chooseImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseImageFromGallery()
                } else {
                    Toast.makeText(context, R.string.permission_denied, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            mAccountImageView.setImageURI(data?.data)
        }
    }
}