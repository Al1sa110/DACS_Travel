package com.example.travel.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun TimeManagerScreen(navController: NavController) {
    val context = LocalContext.current
    val database = FirebaseDatabase.getInstance("https://travel-f4cbd-default-rtdb.asia-southeast1.firebasedatabase.app")
    val refData: DatabaseReference = database.reference.child("Location")

    val currentUser = FirebaseAuth.getInstance().currentUser
    val currentUserEmail = currentUser?.email

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
                            list.add(it)
                        }
                    }
                }
                locationList = list
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors if any
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "List of Locations", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        // Display the list of locations from the database
        locationList.forEach { location ->
            ListItem(location = location)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ListItem(location: LocationData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Name: ${location.name}")
            Text(text = "Address: ${location.full_address}")
            Text(text = "Date: ${location.date}")
            Text(text = "Time: ${location.time}")
            Text(text = "User: ${location.user}")
        }
    }
}
