package com.example.travel.ui.screen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.travel.model.LocationData
import com.example.travel.viewModel.AuthViewModel
import com.example.travel.viewModel.ProvinceDetailViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

@Composable
fun DetailsScreen(navController: NavController) {
    val context = LocalContext.current
    val currentBackStackEntry = navController.currentBackStackEntry
    val factory = DetailsFactory(currentBackStackEntry?.arguments?.getString("id").toString())
    val provinceDetailViewModel: ProvinceDetailViewModel = viewModel(factory = factory)
    val calendar = Calendar.getInstance()
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    val authViewModel: AuthViewModel = viewModel()
    val user = authViewModel.user

    Scaffold {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                ) {
                    Button(onClick = { navController.navigate("home") }) {
                        Text(text = "Back")
                    }
                }
            }
            items(provinceDetailViewModel.provinceDetailResult?.data.orEmpty()) { data ->
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
                        Text(text = data.name ?: "", style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Rating: ${data.rating ?: "No value"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Type:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        data.types?.forEach { type ->
                            Text(
                                text = type ?: "No value",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "City: ${data.full_address ?: "No value"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Phone: ${data.phone_number ?: "No value"}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Time zone: ${data.timezone ?: "No value"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Working hours:",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = formatWorkingHours(data.working_hours.toString()),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "State: ${data.state?: "No value"}",
                                style = MaterialTheme.typography.bodyMedium
                            )


                        Spacer(modifier = Modifier.height(8.dp))
                        Column {
                            data.photos?.forEach { photo ->
                                AsyncImage(
                                    model = photo.url,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .height(200.dp),
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (data.description.isNotEmpty()) {
                            Text("Description", style = MaterialTheme.typography.headlineSmall)
                            Spacer(modifier = Modifier.height(8.dp))
                            data.description.forEach { description ->
                                Text(
                                    text = description ?: "",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Button(onClick = {
                                // Date Picker Dialog
                                DatePickerDialog(context, { _, year, month, dayOfMonth ->
                                    selectedDate = "$dayOfMonth/${month + 1}/$year"
                                    Toast.makeText(context, "Date: $selectedDate", Toast.LENGTH_SHORT).show()
                                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
                            }) {
                                Text(text = "Pick Date")
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = selectedDate)
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Button(onClick = {
                                // Time Picker Dialog
                                TimePickerDialog(context, { _, hourOfDay, minute ->
                                    selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                                    Toast.makeText(context, "Time: $selectedTime", Toast.LENGTH_SHORT).show()
                                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
                            }) {
                                Text(text = "Pick Time")
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = selectedTime)
                            }
                        }

                        Button(onClick = {
                            if (user != null){
                                if(user.email != null){
                                    if (selectedDate.isNotBlank() && selectedTime.isNotBlank()) {
                                        val database = FirebaseDatabase.getInstance("https://travel-f4cbd-default-rtdb.asia-southeast1.firebasedatabase.app")
                                        val refData: DatabaseReference = database.reference.child("Location")

                                        refData.orderByChild("user").equalTo(user?.email).addListenerForSingleValueEvent(object :
                                            ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                var isConflict = false
                                                for (data in snapshot.children) {
                                                    val location = data.getValue(LocationData::class.java)
                                                    if (location != null && location.date == selectedDate && location.time == selectedTime) {
                                                        isConflict = true
                                                        break
                                                    }
                                                }
                                                if (isConflict) {
                                                    Toast.makeText(context, "Date and time are already in use", Toast.LENGTH_SHORT).show()
                                                } else {
                                                    // No conflict, add the location
                                                    val locationData = LocationData(
                                                        name = data.name ?: "",
                                                        full_address = data.full_address ?: "",
                                                        date = selectedDate,
                                                        time = selectedTime,
                                                        user = user?.email ?: "User"
                                                    )
                                                    refData.push().setValue(locationData)
                                                    Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show()
                                                }
                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                                            }
                                        })
                                    } else {
                                        Toast.makeText(context, "Please select a date and time", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }else {
                                Toast.makeText(context, "Please login first", Toast.LENGTH_SHORT).show()
                                navController.navigate("login")
                            }
                        }) {
                            Text(text = "Add")
                        }

                    }
                }
            }
        }
    }
}


class DetailsFactory(private val id: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProvinceDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProvinceDetailViewModel(id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


fun formatWorkingHours(hours: String?): String {
    if (hours.isNullOrEmpty()) return ""
    var formattedHours = hours
        .replace("{", "")
        .replace("}", "")
        .replace("[]", "No value")
        .replace("PM,", "PM |")
        .replace("=[", ":")
        .replace("]", "")
        .replace(",", "\n")
        .replace(" ", "")

    return formattedHours
}
