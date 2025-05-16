package com.maxidev.tastymeal

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.Q
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.allowHardware
import coil3.request.allowRgb565
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.maxidev.tastymeal.navigation.NavigationGraph
import com.maxidev.tastymeal.presentation.theme.TastyMealTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import okio.FileSystem

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (SDK_INT >= Q) {
            window.isNavigationBarContrastEnforced
        }
        enableEdgeToEdge()
        setContent {
            TastyMealTheme {

                setSingletonImageLoaderFactory { context ->
                    getAsyncImageLoader(context)
                }

                Surface {
                    NavigationGraph()
                }
            }
        }
    }
}

private fun getAsyncImageLoader(context: PlatformContext): ImageLoader {

    val cachePolicy = CachePolicy.ENABLED

    return ImageLoader.Builder(context)
        .crossfade(true)
        .allowRgb565(true)
        .allowHardware(true)
        .memoryCachePolicy(policy = cachePolicy)
        .memoryCache { newMemoryCache(context) }
        .diskCachePolicy(policy = cachePolicy)
        .diskCache { newDiskCache() }
        .networkCachePolicy(policy = cachePolicy)
        .coroutineContext(Dispatchers.IO)
        .logger(DebugLogger())
        .build()
}

private fun newMemoryCache(context: PlatformContext): MemoryCache {

    return MemoryCache.Builder()
        .maxSizePercent(context = context, 0.03)
        .strongReferencesEnabled(true)
        .build()
}

private fun newDiskCache(): DiskCache {

    return DiskCache.Builder()
        .directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "image_cache")
        .maxSizeBytes(1024L * 1024 * 1024) // 512 MB
        .build()
}