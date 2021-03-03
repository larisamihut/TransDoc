package com.application.transdoc.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.application.transdoc.R
import com.application.transdoc.databinding.FragmentLoginBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.application.transdoc.MainActivity
import com.application.transdoc.firestore.Admin
import com.application.transdoc.firestore.Database
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import kotlin.collections.HashMap

class LoginFragment : AppCompatActivity() {

    companion object {
        const val TAG = "LoginFragment"
        const val SIGN_IN_RESULT_CODE = 1001
    }


    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var dataSource: CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<FragmentLoginBinding>(this, R.layout.fragment_login)
        dataSource = Database().getCollectionRefference("admins")

        viewModel.authenticationState.observe(this, object : Observer<LoginViewModel.AuthenticationState>{
            override fun onChanged(t: LoginViewModel.AuthenticationState?) {
                if (t != null) {
                    if (t.ordinal.equals(0)){
                        addAdminToFirestore()
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                    }
                    else if (t.ordinal.equals(1)){
                        launchSignInFlow()
                    }
                }
            }
        })
    }

    private fun launchSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            SIGN_IN_RESULT_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESULT_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                Log.i(
                    TAG,
                    "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!"
                )
            //   val intent = Intent(this, MainActivity::class.java)
            //    startActivity(intent)
            } else {
                Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }

    private fun addAdminToFirestore() {
        val data = hashMapOf(
            "email" to FirebaseAuth.getInstance().currentUser?.email
        )
        dataSource.get()
            .addOnSuccessListener { result ->
                checkIfAdminExists(result, data)
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error getting documents: ", exception)
            }
    }

    private fun checkIfAdminExists(
        result: QuerySnapshot,
        data: HashMap<String, String?>
    ) {
        var flag = false
        val admins = result.toObjects(Admin::class.java)
        for (admin in admins) {
            if ((admin.email).equals(FirebaseAuth.getInstance().currentUser?.email)) {
                flag = true
                break
            }
        }
        if (flag == false) {
            addNewAdmin(data)
        }
    }

    private fun addNewAdmin(data: HashMap<String, String?>) {
        FirebaseAuth.getInstance().uid?.let {
            dataSource.document(it).set(data)
                .addOnSuccessListener {
                    Toast.makeText(
                        applicationContext,
                        "The new admin was created",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
                .addOnFailureListener { e ->
                    Log.w(
                        "LoginFragment",
                        "Error adding document",
                        e
                    )
                }
        }
    }
}