package com.example.artportraits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateZoomBy
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artportraits.ui.theme.ArtPortraitsTheme
import com.example.artportraits.ui.theme.Beige
import com.example.artportraits.ui.theme.pinkFlesh
import kotlinx.coroutines.launch
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtPortraitsTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Portrait()
                }
            }
        }
    }
}

@Composable
fun Portrait(modifier: Modifier = Modifier) {

    // set up all transformation states
    val scope = rememberCoroutineScope()
    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
        offset += offsetChange
    }
    var change: Int by remember { mutableStateOf(1) }
    val title = when(change){
        1 -> R.string.ajax_title
        2 -> R.string.australian_title
        3 ->  R.string.bruce_title
        4 ->  R.string.vanuatu_title
        5 ->  R.string.whitman_title
        else ->  R.string.thanks
    }
    val portrait = when(change){
        1 -> R.drawable.ajax
        2 -> R.drawable.australian_cowboy_small
        3 -> R.drawable.bruce
        4 -> R.drawable.vanuatu
        5 -> R.drawable.whitman
        else -> R.drawable.thankstext
    }

    val detail = when(change){
        1 -> R.string.ajax_detail
        2 -> R.string.australian_detail
        3 -> R.string.bruce_detail
        4 -> R.string.vanuatu_detail
        5 -> R.string.whitman_detail
        else -> R.string.thanks_detail
    }
    Column() {
        Row(
            horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top,
            modifier = Modifier.background(color = Color(0xFFfef8ca)).padding(12.dp).fillMaxWidth().weight(0.1F)
        ) {
            Icon(painter = painterResource(R.drawable.brush), contentDescription = "Brush", tint = Color(0xFF60c068))
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 26.sp, fontWeight = FontWeight.Bold
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().weight(0.9F).verticalScroll(rememberScrollState()).background(
                pinkFlesh
            )
        ) {


            //Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Card(shape = RoundedCornerShape(20.dp),elevation = 30.dp, modifier = Modifier.padding(8.dp), contentColor = Color.White) {

                Image(
                    painter = painterResource(portrait),
                    contentDescription = title.toString(),
                    modifier = Modifier// apply other transformations like rotation and zoom
                        // on the image
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            rotationZ = rotation,
                            translationX = offset.x,
                            translationY = offset.y
                        )
                        // add transformable to listen to multitouch transformation events
                        // after offset
                        .transformable(state = state)
                        .background(Color.White)
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onDoubleTap = {
                                    if (scale != 1f) {
                                        scope.launch {
                                            state.animateZoomBy(1 / scale)
                                        }
                                        offset = Offset.Zero
                                        rotation = 0f
                                    } else {
                                        scope.launch {
                                            state.animateZoomBy(2f)
                                        }
                                    }
                                }
                            )
                        }
                )
            }
                Column(modifier = Modifier.background(color = (Color.White)).padding(8.dp)) {
                    Row(horizontalArrangement = Arrangement.Center) {
                        Text(text = stringResource(R.string.artist_header), fontStyle = Italic)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = stringResource(R.string.artist_name))
                    }
                    Text(stringResource(detail), textAlign = TextAlign.Center)
                }

            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.Center) {
                Button(onClick = {
                    change = if (change >= 6){
                        6
                    } else if (change == 5){
                        4
                    } else if (change == 4){
                        3
                    } else if (change == 3){
                        2
                    } else if (change == 2){
                        1
                    } else 5
                }, colors = ButtonDefaults.textButtonColors(Beige)) { Text(text = stringResource(R.string.previous_button), modifier = Modifier.background(Color(0xFFfef8ca))) }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = {
                    change = if (change >= 6){
                        6
                    } else if (change == 1){
                        2
                    } else if (change == 2){
                        3
                    } else if (change == 3){
                        4
                    }
                    else if (change == 4){
                        5
                    } else 1
                }, colors = ButtonDefaults.textButtonColors(Beige)) { Text(text = stringResource(R.string.next_button), modifier = Modifier.background(Color(0xFFfef8ca))) }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PortraitPreview() {
    ArtPortraitsTheme {
        Portrait()
    }
}