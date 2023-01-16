package com.mcit.basicjetbackcompose

import android.app.PendingIntent
import android.app.PictureInPictureParams
import android.app.RemoteAction
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toAndroidRect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.mcit.basicjetbackcompose.ui.Boarding
import com.mcit.basicjetbackcompose.ui.MyBroadcastReceiver
import com.mcit.basicjetbackcompose.ui.theme.BasicJetbackComposeTheme

class MainActivity : ComponentActivity() {
    var videoBonds = Rect()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicJetbackComposeTheme {
                MyApp()
            }
        }
    }

    @Composable
    private fun pictureInPictureCourse() {
        AndroidView(
            factory = {
                VideoView(it, null).apply {
                    Uri.parse("Android.resource://$packageName/${R.raw.my_video}")
                    start()
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    videoBonds = it
                        .boundsInWindow()
                        .toAndroidRect()
                }
        )
    }

    fun pictureInPicture(): PictureInPictureParams? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PictureInPictureParams.Builder()
                .setSourceRectHint(videoBonds)
                .setAspectRatio(Rational(16, 8))
                .setActions(
                    listOf(
                        RemoteAction(
                            Icon.createWithResource(
                                applicationContext,
                                R.drawable.ic_launcher_foreground
                            ), "Action ", "This my Action",
                            PendingIntent.getBroadcast(
                                applicationContext,
                                0,
                                Intent(applicationContext, MyBroadcastReceiver::class.java),
                                PendingIntent.FLAG_IMMUTABLE
                            )
                        )
                    )
                )
                .build()
        } else {
            return null
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        pictureInPicture()?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                enterPictureInPictureMode(it)
            }
        }
    }
}

@Composable
fun MyApp() {
    val inBoardingState = remember {
        mutableStateOf(true)
    }
    if (inBoardingState.value) {
        Boarding {
            inBoardingState.value = false
        }
    } else {
        Greetings()
    }

}

@Composable
fun Greetings(
    names: List<Unit> = List(100) {

    }
) {
    Surface(
        color = MaterialTheme.colors.background
    ) {
//        Column(modifier = Modifier.padding(vertical = 4.dp)) {
//            for (name in names) {
//                Greeting(name.toString())
//            }
//        }

        LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
            items(names.size) {
                Greeting(name = it.toString())
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    val extraPadding by animateDpAsState(
        if (expanded) 48.dp else 0.dp, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    Card(
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp).fillMaxSize(),
        backgroundColor = MaterialTheme.colors.background
    ) {
        Row(modifier = Modifier.padding(8.dp)
            .animateContentSize(
            animationSpec = spring(dampingRatio= Spring.DampingRatioMediumBouncy
            , stiffness = Spring.StiffnessLow))
        ) {

                Column(
                    modifier = Modifier
                        .padding(bottom = extraPadding.coerceAtLeast(0.dp)).fillMaxSize()
                        .weight(1f)
                ) {
                    Text(text = "Hello ")
                    Text(
                        text = name, style = MaterialTheme.typography.h4.copy(
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                    if (expanded)
                        Text(
                            text = ("Composem ipsum color sit lazy, " +
                                    "padding theme elit, sed do bouncy.").repeat(4)
                        )
                }

                IconButton(
                    onClick = {
                        expanded = !expanded
                    }) {
                    androidx.compose.material.Icon(
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = if (expanded) stringResource(id = R.string.show_less) else stringResource(
                            id = R.string.show_more
                        )
                    )
                }

        }
    }


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BasicJetbackComposeTheme {
//        Greeting("Android")
        MyApp()
    }
}
