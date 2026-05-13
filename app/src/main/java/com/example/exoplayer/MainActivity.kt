package com.example.exoplayer

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.exoplayer.ui.theme.ExoplayerTheme
import androidx.annotation.OptIn
import androidx.core.net.toUri

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExoplayerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    VideoPlayer(url = "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4")
                }
            }
        }
    }
}





@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(url: String) {
    val context = LocalContext.current

    // 1. Inicializamos el Player de forma segura
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(url.toUri())
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    // 2. Vista del reproductor
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    // Esto asegura que el video intente llenar el espacio
                    useController = true
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }

    // 3. ¡IMPORTANTE! Liberar el player cuando salgas de la app
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
}