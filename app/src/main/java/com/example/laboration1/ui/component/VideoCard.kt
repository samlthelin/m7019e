package com.example.laboration1.ui.component

//import android.media.browse.MediaBrowser.MediaItem
import android.content.Intent
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.laboration1.network.model.ApiVideo
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.media3.common.MediaItem
import coil3.compose.AsyncImage

// just for lab presentation! if the site field is Sample we know it's the hardcoded we have for preview!
@Composable
fun VideoCard(video: ApiVideo) {
    if (video.site == "Sample") {
        ExoPlayerCard(video)
    } else {
        ExternalYouTubeCard(video)
    }
}

// solution from canvas, more or less. it works! :)
@Composable
fun ExoPlayerCard(video: ApiVideo) {

    val context = LocalContext.current // Get the current context
    val lifecycleOwner = LocalLifecycleOwner.current // not in medium but should be important, using principles from viewmodel.
    val player = remember { ExoPlayer.Builder(context).build() } // remember to not recreate it every recomposition!

    val videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

    AndroidView(
        factory = {
            PlayerView(context).apply {
                this.player = player
                useController = true
            }
        },
        modifier = Modifier
            .padding(8.dp)
            .width(200.dp)
            .height(120.dp)
    )

    // Manage lifecycle events
    // essentially, if the screen stops we pause the video, and if the screen is destroyed we release it from mem.
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> player.pause()
                Lifecycle.Event.ON_DESTROY -> player.release()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        // Set MediaSource to ExoPlayer
        player.setMediaItem(MediaItem.fromUri(videoUrl))
        player.prepare()
        player.playWhenReady = false

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            player.release()
        }
    }
}

// for youtube trailer. uses same principles as from version with imdb!
@Composable
fun ExternalYouTubeCard(video: ApiVideo) {
    val context = LocalContext.current
    val thumbnailUrl = "https://img.youtube.com/vi/${video.key}/hqdefault.jpg"

    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(200.dp)
            .height(120.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=${video.key}"))
                context.startActivity(intent)
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box {
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = video.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = Color.White,
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

