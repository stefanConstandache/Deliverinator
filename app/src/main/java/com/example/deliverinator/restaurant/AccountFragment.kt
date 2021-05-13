package com.example.deliverinator.restaurant

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.deliverinator.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AccountFragment : Fragment() {
    private lateinit var mAccountImageView: ImageView
    private lateinit var mRestaurantName: TextView
    private lateinit var mDescription: TextView
    private lateinit var mChangePassword: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mStore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.restaurant_fragment_account, container, false)

        mAccountImageView = view.findViewById(R.id.account_fragment_imageView)
        mRestaurantName = view.findViewById(R.id.account_fragment_name)
        mDescription = view.findViewById(R.id.account_fragment_description)
        mChangePassword = view.findViewById(R.id.account_fragment_button)
        mAuth  = FirebaseAuth.getInstance()
        mStore = FirebaseFirestore.getInstance()

        val user = mAuth.currentUser
        val docRef = mStore.collection(USERS).document(user!!.uid)

        docRef.get().addOnSuccessListener { mRestaurantName.text = it.getString(NAME) }

        mDescription.setOnClickListener {
            setDescription(it)
        }

        mChangePassword.setOnClickListener {
            sendChangePasswordEmail()
        }

        mAccountImageView.setOnClickListener {
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

        return view
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

    private fun setDescription(it: View?) {
        val descriptionField = EditText(it?.context)
        val descriptionDialog = it?.context?.let { context -> AlertDialog.Builder(context) }

        if (!mDescription.text.equals(getString(R.string.no_description))) {
            descriptionField.setText(mDescription.text.toString())
        }

        descriptionDialog
            ?.setTitle(R.string.add_description)
            ?.setView(descriptionField)
            ?.setPositiveButton(R.string.set) { _, _ ->
                val description = descriptionField.text.toString().trim()

                if (description.isEmpty()) {
                    mDescription.text = getString(R.string.no_description)
                } else {
                    mDescription.text = description
                }
            }?.create()?.show()
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