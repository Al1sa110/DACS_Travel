package com.example.travel.ui.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dialog.ResetPasswordDialog
import com.example.travel.controller.AuthController
import com.example.travel.state.HomeUiState
import com.example.travel.viewModel.AuthViewModel
import com.example.travel.viewModel.ProvinceViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


//@Composable
//fun StateHandler(
//    homeUiState: HomeUiState,
//    navController: NavController
//) {
//    when (homeUiState) {
//        is HomeUiState.Loading -> LoadingHomeScreen()
//        is HomeUiState.Success -> HomeScreen(navController)
//        is HomeUiState.Error -> ErrorHomeScreen()
//    }
//
//}
//
//@Composable
//fun LoadingHomeScreen() {
//    Spacer(modifier = Modifier.height(400.dp))
//    Text(text = "Loading...")
//}
//
//@Composable
//fun ErrorHomeScreen() {
//    Text(text = "Error...")
//}


@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val authController = AuthController(context,navController)
    val provinceViewModel: ProvinceViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val user = authViewModel.user
    var search by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold() {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    label = { Text("Search") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                Button(
                    onClick = {
                        if (search.isNotEmpty()) {
                            navController.navigate("search/$search")
                        } else {
                            Toast.makeText(context, "Please enter a search term", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text(text = "Search")
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(bottom = 7.dp)
                ){
                    if (user != null) {
                        if (user.email != null) {
                            Button(onClick = { authController.signOut() }) {
                                Text(text = "Sign Out")
                            }
                        }
                    }else {
                        Button(onClick = { navController.navigate("login") }) {
                            Text(text = "Sign In")
                        }
                    }

                    if (user != null) {
                        if (user.email != null) {
                            Button(onClick = { navController.navigate("manager") }) {
                                Text(text = "Manager")
                            }
                        }
                    }else {
                        // do nothing
                    }

                    Button(onClick = { navController.navigate("home") }) {
                        Text(text = "Refresh")
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(bottom = 7.dp)
                ) {
                    Button(onClick = { showDialog = true }) {
                        Text(text = "Reset password")
                    }
                }

                if (showDialog) {
                    ResetPasswordDialog(
                        onConfirm = {
                            if (user != null && user.email != null) {
                                authViewModel.auth.sendPasswordResetEmail(user.email!!)
                                    .addOnCompleteListener{
                                        if (it.isSuccessful) {
                                            Toast.makeText(context, "Reset mail has been sent to your email", Toast.LENGTH_SHORT).show()
                                        }else {
                                            Toast.makeText(context, "Failed to send reset mail", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(context, "Please sign in first", Toast.LENGTH_SHORT).show()
                            }
                            showDialog = false
                        },
                        onDismiss = { showDialog = false }
                    )
                }

                if (user != null) {
                    if (user?.email != null) {
                        Text(
                            text = "Welcome, ${user?.email ?: "User"}!",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }else {
                    Text(text = "No user signed in")
                }
            }
            if(provinceViewModel.provinceResult?.data.isNullOrEmpty()){
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "No data found",
                        style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold
                    )
                }
            }else {
                items(provinceViewModel.provinceResult?.data.orEmpty()) { data ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .clickable { navController.navigate("details/${data.business_id}") },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = Color.Black
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(text = data.name, style = MaterialTheme.typography.headlineSmall)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Rating: ${data.rating}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "City: ${data.city}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}