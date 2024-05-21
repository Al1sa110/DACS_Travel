package com.example.travel.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dialog.ForgotPasswordDialog
import com.example.travel.R
import com.example.travel.controller.AuthController
import com.example.travel.viewModel.AuthViewModel

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    val authViewModel: AuthViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp,Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = "Login Image",
            modifier = Modifier.size(200.dp)
        )

        Text(text = "Welcome Back", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Text(
            modifier = Modifier
                .clickable { navController.navigate("hotel") },
            text = "Login to your account")

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = {
                Text(text = "Email address")
            }
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(text = "Password")
            },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = {
                      AuthController(context,navController).login(
                          email,
                          password,
                      )
            },
        ) {
            Text(text = "Login")
        }
        Row {
            Text(
                modifier = Modifier
                    .clickable { showDialog = true },
                text = "Forget your password? "
            )
        }
        Row {
            Text(text = "Don't have account? ")
            Text(
                modifier = Modifier
                    .clickable { navController.navigate("signup") },
                fontWeight = FontWeight.Bold,
                text = "Signup"
            )
        }

        if (showDialog) {
            ForgotPasswordDialog(
                onConfirm = { inputEmail ->
                    authViewModel.auth.sendPasswordResetEmail(inputEmail)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(context, "Reset email sent", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Failed to send reset email", Toast.LENGTH_SHORT).show()
                            }
                        }
                    showDialog = false
                },
                onDismiss = { showDialog = false }
            )
        }

    }
}


