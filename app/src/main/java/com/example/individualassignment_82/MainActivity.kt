package com.example.individualassignment_82

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

// Create the DataStore instance as an extension property on Context
val Context.dataStore by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Read the preferences as Compose state
//            val context = this
//            val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
//            val FONT_SIZE_KEY = intPreferencesKey("font_size")
//
//            val darkModeFlow: Flow<Boolean> = context.dataStore.data
//                .map { preferences -> preferences[DARK_MODE_KEY] ?: false }
//            val fontSizeFlow: Flow<Int> = context.dataStore.data
//                .map { preferences -> preferences[FONT_SIZE_KEY] ?: 12}
//
//            val isDarkMode by darkModeFlow.collectAsState(initial = false)
//            val fontSize by fontSizeFlow.collectAsState(initial = 12)
//
//            // Apply the theme based on the preference
//            DataStoreDemoApp(isDarkMode = isDarkMode)
//            Scaffold() {padding->
//                Column(Modifier.padding(padding)){
//                ShowCalendar()
//                    }
//            }
            Scaffold() { padding ->
                MainScreen(padding)
            }
        }
    }
}

@Composable
fun MainScreen(padding: PaddingValues){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    val FONT_SIZE_KEY = intPreferencesKey("font_size")

    val darkModeFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[DARK_MODE_KEY] ?: false }
    val fontSizeFlow: Flow<Int> = context.dataStore.data
        .map { preferences -> preferences[FONT_SIZE_KEY] ?: 12}

    val isDarkMode by darkModeFlow.collectAsState(initial = false)
    val fontSize by fontSizeFlow.collectAsState(initial = 12)

    val colorScheme = if (isDarkMode) darkColorScheme() else lightColorScheme()

    val windowInfo = calculateCurrentWindowInfo()
    var today by rememberSaveable {mutableStateOf(LocalDate.now())}

    MaterialTheme(colorScheme = colorScheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (windowInfo.orientation == Orientation.PORTRAIT) {
                Column(
                    modifier = Modifier.padding(padding)
                ) {
                    ShowCalendar(
                        today,
                        { newDate ->
                            today = newDate
                        },
                        modifier = Modifier.weight(1f),
                        colorScheme
                    )
                    JournalScreen(today, fontSize, modifier = Modifier.weight(1f))
                    Row() {
                        Column() {
                            Text(
                                text = if (isDarkMode) "Dark Mode" else "Light Mode",
                                color = colorScheme.onBackground
                            )
                            Switch(
                                modifier = Modifier.padding(4.dp),
                                checked = isDarkMode,
                                onCheckedChange = { newValue ->
                                    scope.launch {
                                        context.dataStore.edit { preferences ->
                                            preferences[DARK_MODE_KEY] = newValue
                                        }
                                    }
                                }
                            )
                        }
                        Spacer(modifier = Modifier.size(12.dp))
                        Column() {
                            Text(
                                text = "Font Size: $fontSize",
                                color = colorScheme.onBackground
                            )
                            Slider(
                                value = fontSize.toFloat(),
                                valueRange = 12f..24f,
                                steps = 12,
                                onValueChange = { newValue: Float ->
                                    scope.launch {
                                        context.dataStore.edit { preferences ->
                                            preferences[FONT_SIZE_KEY] = newValue.toInt()
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.padding(padding)
                ) {
                    ShowCalendar(
                        today,
                        { newDate ->
                            today = newDate
                        },
                        modifier = Modifier.weight(.5f),
                        colorScheme
                    )
                    Column(
                        modifier = Modifier.weight(.6f)
                    ) {
                        JournalScreen(today, fontSize, modifier = Modifier.weight(1f))
                        Row() {
                            Column() {
                                Text(
                                    text = if (isDarkMode) "Dark Mode" else "Light Mode",
                                    color = colorScheme.onBackground
                                )
                                Switch(
                                    modifier = Modifier.padding(4.dp),
                                    checked = isDarkMode,
                                    onCheckedChange = { newValue ->
                                        scope.launch {
                                            context.dataStore.edit { preferences ->
                                                preferences[DARK_MODE_KEY] = newValue
                                            }
                                        }
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.size(12.dp))
                            Column() {
                                Text(
                                    text = "Font Size: $fontSize",
                                    color = colorScheme.onBackground
                                )
                                Slider(
                                    value = fontSize.toFloat(),
                                    valueRange = 12f..24f,
                                    steps = 12,
                                    onValueChange = { newValue: Float ->
                                        scope.launch {
                                            context.dataStore.edit { preferences ->
                                                preferences[FONT_SIZE_KEY] = newValue.toInt()
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DataStoreDemoApp(isDarkMode: Boolean) {
    // Dynamically apply dark/light theme using Material3
    val colorScheme = if (isDarkMode) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = colorScheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SettingsScreen()
        }
    }
}

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")

    val darkModeFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[DARK_MODE_KEY] ?: false }

    val isDarkMode by darkModeFlow.collectAsState(initial = false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Dark Mode Preference", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Switch(
            checked = isDarkMode,
            onCheckedChange = { newValue ->
                scope.launch {
                    context.dataStore.edit { preferences ->
                        preferences[DARK_MODE_KEY] = newValue
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = if (isDarkMode) "Dark Mode ON" else "Dark Mode OFF")
    }
}