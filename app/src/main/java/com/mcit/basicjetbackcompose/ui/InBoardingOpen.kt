package com.mcit.basicjetbackcompose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Boarding(onClickButton: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Start With BoardScreen")
            Button(onClick = {onClickButton()}, modifier = Modifier.padding(vertical = 24.dp)) {
                Text(text = "Continue")
            }
        }
    }
}