package com.example.travel.controller

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.example.travel.R
import com.example.travel.viewModel.AuthViewModel
import com.google.firebase.auth.FirebaseUser

class AuthController(context: Context,navController: NavController) {
    private val auth = AuthViewModel().auth
    private val navController = navController

    private val toastEmpty = Toast.makeText(
        context,
        R.string.empty_information,
        Toast.LENGTH_SHORT
    )

    private val toastIncorrect = Toast.makeText(
        context,
        R.string.incorrect,
        Toast.LENGTH_SHORT
    )

    private val toastSuccessful = Toast.makeText(
        context,
        R.string.successful,
        Toast.LENGTH_SHORT
    )

    private val toastNotMatch = Toast.makeText(
        context,
        R.string.password_do_not_match,
        Toast.LENGTH_SHORT
    )

    fun login(email:String,password:String){

        if(email.isEmpty()||password.isEmpty()){
            toastEmpty.show()
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        toastSuccessful.show()
                        val user = auth.currentUser
                        updateUI(user)
                        navController.navigate("home")

                    } else {
                        toastIncorrect.show()
                        updateUI(null)
                    }
                }
        }
    }

    fun signup(email:String,password: String,confirmPassword:String){
        if( email.isEmpty()||password.isEmpty()||confirmPassword.isEmpty() ){
            toastEmpty.show()
        }
        else {
            if(password == confirmPassword) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val user = auth.currentUser
                            updateUI(user)
                            navController.navigate("home")
                        } else {
                            updateUI(null)
                        }
                    }
            } else {
                toastNotMatch.show()
            }
        }
    }

    fun signOut(){
        auth.signOut()
        toastSuccessful.show()
        navController.navigate("login")
    }

    private fun updateUI(user: FirebaseUser?) {
    }

}