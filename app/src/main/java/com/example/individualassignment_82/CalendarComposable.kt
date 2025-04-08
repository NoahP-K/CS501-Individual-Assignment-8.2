package com.example.individualassignment_82

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

/*
Couldn't find a good enough api or existing composable to make a calendar, so in a fit
of equal parts inspiration and sleep deprivation I decided to just make my own.
 */

@Composable
fun ShowCalendar(
    selectedDate: LocalDate,
    changeDate: (LocalDate)->Unit,
    modifier: Modifier,
    colorScheme: ColorScheme
){
    val today = LocalDate.now() //current date; NOT the same as the current displayed month/year
    var displayedYear by rememberSaveable { mutableStateOf(today.year) }    //year displayed
    var displayedMonth by rememberSaveable { mutableStateOf(today.month) }  //month displayed
    val daysInDisplayedMonth = YearMonth.of(displayedYear, displayedMonth).lengthOfMonth() //# days in displayed month
    val firstDayOfDisplayedMonth = YearMonth.of(displayedYear, displayedMonth).atDay(1) //first day of displayed month

    val fontSize = 16.sp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ){
        //display month and year and provide arrow buttons to increment/decrement each
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {displayedYear--}
            ) { Icon(imageVector = Icons.Rounded.KeyboardArrowLeft,
                contentDescription = "previous year") }
            Text(
                text = displayedYear.toString(),
                fontSize = fontSize
            )
            IconButton(
                onClick = {displayedYear++}
            ) { Icon(imageVector = Icons.Rounded.KeyboardArrowRight,
                contentDescription = "next year")
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {displayedMonth = displayedMonth.minus(1)}
            ) { Icon(imageVector = Icons.Rounded.KeyboardArrowLeft,
                contentDescription = "previous month") }
            Text(
                text = displayedMonth.toString(),
                fontSize = fontSize
            )
            IconButton(
                onClick = {displayedMonth = displayedMonth.plus(1)}
            ) { Icon(imageVector = Icons.Rounded.KeyboardArrowRight,
                contentDescription = "next month") }
        }
        //days of week listed for use in a few places
        val days = listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")
        //blanks indicates how many days of the week occur before the first day of the month.
        //these days should be blank on the calendar
        val blanks = when(firstDayOfDisplayedMonth.dayOfWeek){
            DayOfWeek.SUNDAY->0
            DayOfWeek.MONDAY->1
            DayOfWeek.TUESDAY->2
            DayOfWeek.WEDNESDAY->3
            DayOfWeek.THURSDAY->4
            DayOfWeek.FRIDAY->5
            else->6
        }
        //use lazyverticalgrid to create boxes for each day.
        //Set width to strictly 7 so that the format is always preserved
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth(),
            //.height(300.dp), // example height
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalArrangement = Arrangement.SpaceEvenly,
            userScrollEnabled = true
        ) {
            //first make a bar of day names
            items(days.size){i->
                Text(
                    text = days[i],
                    fontSize = fontSize,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            //then make the 'blank' days before the first day of the month
            items(blanks){
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                ){
                    Text(
                        text = " ",
                        fontSize = fontSize,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            //finally make all days in the month
            items(daysInDisplayedMonth) { i ->
                val dayNum = i + 1
                //The current day should be highlighted yellow for easy identification.
                //The current selected day should be light gray for easy identification.
                //All other day boxes should be black/white depending on light mode.
                var boxColor = colorScheme.background
                var textColor = colorScheme.onBackground
                if(displayedMonth == today.month
                    && displayedYear == today.year
                    && dayNum == today.dayOfMonth){
                        boxColor = Color.Yellow
                        textColor = Color.Black
                } else if(dayNum == selectedDate.dayOfMonth
                    && displayedMonth == selectedDate.month
                    && displayedYear == selectedDate.year){
                        boxColor = Color.LightGray
                        textColor = Color.Black
                }

                Box(
                    modifier = Modifier
                        .border(width = 3.dp, color = colorScheme.onBackground)
                        .aspectRatio(1f)
                        .background(color = boxColor)
                        .clickable {
                            changeDate(LocalDate.of(displayedYear, displayedMonth, dayNum))
                        }
                ){
                    Text(
                        text = dayNum.toString(),
                        fontSize = fontSize,
                        modifier = Modifier.align(Alignment.Center),
                        color = textColor
                    )
                }
            }
        }
    }


}