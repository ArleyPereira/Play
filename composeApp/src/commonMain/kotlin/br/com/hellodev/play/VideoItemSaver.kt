package br.com.hellodev.play

import androidx.compose.runtime.saveable.Saver

fun videoItemSaver(): Saver<VideoItem?, Any> {
    return Saver(
        save = { video ->
            video?.let {
                listOf(
                    it.id,
                    it.name,
                    it.path,
                    it.thumbnailPath,
                    it.sizeInBytes,
                    it.durationMillis ?: -1L,
                )
            }
        },
        restore = { saved ->
            val values = saved as List<*>
            val hasThumbnailPath = values.size >= 6
            val thumbnailPath = if (hasThumbnailPath) values[3] as String? else null
            val sizeInBytes = if (hasThumbnailPath) values[4] as Long else values[3] as Long
            val durationRaw = if (hasThumbnailPath) values[5] as Long else values[4] as Long
            val duration = durationRaw.takeIf { it >= 0L }
            VideoItem(
                id = values[0] as String,
                name = values[1] as String,
                path = values[2] as String,
                thumbnailPath = thumbnailPath,
                sizeInBytes = sizeInBytes,
                durationMillis = duration,
            )
        },
    )
}
