package com.example.travel.ui.screen

import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.travel.controller.AuthController
import com.example.travel.viewModel.AuthViewModel
import com.example.travel.viewModel.ProvinceViewModel

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val authController = AuthController(context,navController)
    val provinceViewModel: ProvinceViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val user = authViewModel.user

    Scaffold() {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Button(onClick = { authController.signOut() }) {
                    Text(text = "Sign Out")
                }
                Button(onClick = { navController.navigate("manager") }) {
                    Text(text = "Manager")
                }
                Button(onClick = { navController.navigate("home") }) {
                    Text(text = "Refresh")
                }
                if (user != null) {
                    if (user.email != null) {
                        Text(text = "Welcome, ${user.email ?: "User"}!")
                    }
                }else {
                    Text(text = "No user signed in")
                }
            }
            items(provinceViewModel.provinceResult?.data.orEmpty()) { data ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
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
                        Spacer(modifier = Modifier.height(8.dp))

                        Button(onClick = {navController.navigate("details/${data.business_id}")}) {
                            Text(text = "More details")
                        }
                    }
                }
            }
        }
    }
}