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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.travel.controller.AuthController
import com.example.travel.viewModel.AuthViewModel
import com.example.travel.viewModel.SearchViewModel

@Composable
fun SearchScreen(navController: NavController) {
    val context = LocalContext.current
    val authController = AuthController(context,navController)

    val authViewModel: AuthViewModel = viewModel()
    val user = authViewModel.user

    val currentBackStackEntry = navController.currentBackStackEntry
    val factory = SearchFactory(currentBackStackEntry?.arguments?.getString("query").toString())
    val searchViewModel: SearchViewModel = viewModel(factory = factory)
    val searchfor = currentBackStackEntry?.arguments?.getString("query").toString()

    var search by remember { mutableStateOf("") }

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
                Text(
                    text = "Search for: $searchfor",
                    style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(5.dp))
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
                        .padding(bottom = 16.dp)
                ){
                    Button(onClick = { authController.signOut() }) {
                        Text(text = "Sign Out")
                    }

                    Button(onClick = { navController.navigate("manager") }) {
                        Text(text = "Manager")
                    }

                    Button(onClick = { navController.navigate("home") }) {
                        Text(text = "Refresh")
                    }
                }
                if (user != null) {
                    if (user?.email != null) {
                        Text(text = "Welcome, ${user?.email ?: "User"}!")
                    }
                }else {
                    Text(text = "No user signed in")
                }

            }
            if(searchViewModel.provinceResult?.data.isNullOrEmpty()){
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "No data found",
                        style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold
                    )
                }
            }else {
                items(searchViewModel.provinceResult?.data.orEmpty()) { data ->
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
class SearchFactory(private val query: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(query) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}