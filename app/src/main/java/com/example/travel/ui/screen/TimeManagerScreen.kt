package com.example.travel.ui.screen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.travel.model.LocationData
import com.example.travel.viewModel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun TimeManagerScreen(navController: NavController) {
    val context = LocalContext.current

    val database = FirebaseDatabase.getInstance("https://travel-f4cbd-default-rtdb.asia-southeast1.firebasedatabase.app")
    val refData: DatabaseReference = database.reference.child("Location")

    val authViewModel: AuthViewModel = viewModel()
    val currentUserEmail = authViewModel.user?.email

    var locationList by remember { mutableStateOf(listOf<LocationData>()) }



    // Listen for changes in the database and update the list of locations
    LaunchedEffect(Unit) {
        refData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<LocationData>()
                for (data in snapshot.children) {
                    val location = data.getValue(LocationData::class.java)
                    location?.let {
                        if (location.user == currentUserEmail) {
                            it.id = data.key ?: ""
                            list.add(it)
                        }
                    }
                }

                // Sort list by date and time
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                list.sortWith(compareBy { dateFormat.parse("${it.date} ${it.time}") })
                locationList = list
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun deleteLocation(location: LocationData) {
        val locationRef = database.reference.child("Location").child(location.id)
        locationRef.removeValue().addOnSuccessListener {
            Toast.makeText(context, "Location deleted successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { error ->
            Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateLocation(location: LocationData, newDate: String, newTime: String) {
        val locationRef = database.reference.child("Location").child(location.id)
        locationRef.updateChildren(
            mapOf(
                "date" to newDate,
                "time" to newTime
            )
        ).addOnSuccessListener {
            Toast.makeText(context, "Location updated successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { error ->
            Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    }

    Column(modifier = Modifier.padding(30.dp)) {
        Text(text = "List of Locations", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(10.dp))
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
        LazyColumn {
            items(locationList) { location ->
                ListItem(
                    location = location,
                    onDelete = { deleteLocation(location) },
                    onUpdate = { newDate, newTime -> updateLocation(location, newDate, newTime) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

    }
}

@Composable
fun ListItem(location: LocationData, onDelete: () -> Unit, onUpdate: (String, String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }

    val currentDateTime = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val locationDateTime = dateFormat.parse("${location.date} ${location.time}")
    val isPast = locationDateTime?.before(currentDateTime) == true

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Name: ${location.name}",
                style = if(isPast){
                    TextStyle(textDecoration = TextDecoration.LineThrough)
                } else{
                    TextStyle.Default
                }
            )
            Text(text = "Address: ${location.full_address}")
            Text(text = "Date: ${location.date}")
            Text(text = "Time: ${location.time}")
            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = { onDelete() }) {
                Text(text = "Delete")
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
                if (selectedDate.isNotBlank() && selectedTime.isNotBlank()) {
                    onUpdate(selectedDate, selectedTime)
                } else if (selectedDate.isNotBlank()) {
                    selectedTime = location.time.toString()
                    onUpdate(selectedDate, selectedTime)
                } else if (selectedTime.isNotBlank()) {
                    selectedDate = location.date.toString()
                    onUpdate(selectedDate, selectedTime)
                } else {
                    Toast.makeText(context, "Please select a date and time", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text(text = "Update")
            }
        }
    }
}
