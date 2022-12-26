package com.task.ezetapapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.task.ezetapapp.ui.theme.EzeTapAppTheme
import com.task.ezetapapp.ui.theme.Grey


@Preview(showSystemUi = true)
@Composable
fun NoNetworkFoundPreview(){
    EzeTapAppTheme {
        Scaffold {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(it)){
                NoNetworkFound()
            }
        }
    }
}

@Composable
fun NoNetworkFound(modifier: Modifier = Modifier, onClickRetry: ()->Unit = {}){
    Column(modifier = modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .aspectRatio(1.0f),
            imageVector = Icons.Rounded.CloudOff,
            contentDescription = "",
            colorFilter = ColorFilter.tint(Grey)
        )
        Text(text = "No network found")
        TextButton(onClick = {
            onClickRetry.invoke()
        }) {
            Text(text = "Tap to Retry")
        }
    }
}