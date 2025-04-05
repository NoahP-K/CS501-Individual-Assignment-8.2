package com.example.individualassignment_82

import android.content.Context
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.FileNotFoundException
import java.time.LocalDate

@Composable
fun JournalScreen(date: LocalDate, modifier: Modifier) {
    val context = LocalContext.current
    val fileName = "${date.month}_${date.dayOfMonth}_${date.year}"

    var currentDay by rememberSaveable { mutableStateOf(date)}
    var textToSave by rememberSaveable { mutableStateOf(readFromFile(context, fileName)) }
    var fileContent by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }

    if(currentDay != date){
        textToSave = readFromFile(context, fileName)
        currentDay = date
    }

    val scrollState = rememberLazyListState()
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .scrollable(
                scrollState,
                orientation = Orientation.Vertical,
                enabled = true,
            )
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${date.month} ${date.dayOfMonth}, ${date.year}",
            fontSize = 30.sp,

        )

        OutlinedTextField(
            value = textToSave,
            onValueChange = { textToSave = it },
            label = { Text("Enter text to save") },
            modifier = Modifier.fillMaxWidth()
        )

        // Save button
        Button(onClick = {
            saveToFile(context, fileName, textToSave)
            message = "Text saved successfully"
        }) {
            Text("Save to Internal Storage")
        }

        // Read button
        Button(onClick = {
            fileContent = readFromFile(context, fileName)
            textToSave = fileContent
        }) {
            Text("Load Saved Version")
        }

        // Delete button
        Button(onClick = {
            val deleted = deleteFile(context, fileName)
            textToSave = readFromFile(context, fileName)
            message = if (deleted) "File deleted" else "File not found"
        }) {
            Text("Delete File")
        }
    }
}

// Function to save text to internal storage
fun saveToFile(context: Context, filename: String, content: String) {
    // MODE_PRIVATE means the file is only accessible to this app
    context.openFileOutput(filename, Context.MODE_PRIVATE).use { outputStream ->
        outputStream.write(content.toByteArray())
    }
}

// Function to read text from internal storage
fun readFromFile(context: Context, filename: String): String {
    return try {
        context.openFileInput(filename).bufferedReader().useLines { lines ->
            lines.joinToString("\n")
        }
    } catch (e: FileNotFoundException) {
        "File not found"
    }
}

// Function to delete file from internal storage
fun deleteFile(context: Context, filename: String): Boolean {
    return context.deleteFile(filename)
}