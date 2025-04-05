package com.example.individualassignment_82

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

//Borrowed from example code. Retrieves stats about current device window.
@Composable
fun calculateCurrentWindowInfo(): WindowInfo {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val orientation = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Orientation.PORTRAIT
    } else {
        Orientation.LANDSCAPE
    }

    return WindowInfo(
        widthDp = screenWidth,
        heightDp = screenHeight,
        orientation = orientation
    )
}
//Borrowed from example code. Stores window stats.
data class WindowInfo(
    val widthDp: Int,
    val heightDp: Int,
    val orientation: Orientation
)
//Borrowed from example code. Represents screen orientation.
enum class Orientation {
    PORTRAIT,
    LANDSCAPE
}