package com.ericomine.zentimer

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.SoundEffectConstants
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp
import java.util.Timer
import java.util.TimerTask

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppTheme(useDarkTheme = true) {
                Scaffold { _ -> ZenTimer() }
            }
        }
    }

    @Composable
    fun ZenTimer(modifier : Modifier = Modifier) {
        var remaining: Int by remember { mutableIntStateOf(0) }
        var timerAlpha: Float by remember { mutableFloatStateOf(0f) }
        val interactionSource = remember { MutableInteractionSource() }

        val context = LocalContext.current
        var mediaPlayer = MediaPlayer.create(context, R.raw.bell)
        var timer = object : CountDownTimer(20 * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerAlpha = 1f
                remaining = (millisUntilFinished / 60000).toInt()
            }

            override fun onFinish() {
                timerAlpha = 0f
                mediaPlayer.seekTo(0)
                mediaPlayer.start()
            }
        }


        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.zen),
                contentDescription = "zen timer",
                modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    if (remaining == 0) {
                        timer.start()
                        mediaPlayer.start()
                    } else {
                        timer.cancel()
                    }
                }
            )
            Text(
                remaining.toString(),
                modifier = Modifier.alpha(timerAlpha),
                fontSize = 60.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}