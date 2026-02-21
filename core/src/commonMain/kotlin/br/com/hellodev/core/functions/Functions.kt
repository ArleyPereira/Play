package br.com.hellodev.core.functions

import br.com.hellodev.core.extensions.fixed
import br.com.hellodev.core.extensions.twoDigits

fun formatFileSize(sizeInBytes: Long): String {
    val mb = sizeInBytes / (1024.0 * 1024.0)
    val gb = mb / 1024.0

    return if (gb >= 1.0) {
        "${gb.fixed(2)} GB"
    } else {
        "${mb.fixed(1)} MB"
    }
}

fun formatVideoDuration(durationMillis: Long?): String {
    if (durationMillis == null || durationMillis <= 0L) {
        return "00:00 Minutos"
    }

    val totalSeconds = durationMillis / 1000
    return if (totalSeconds >= 3600) {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        "${hours.twoDigits()}:${minutes.twoDigits()} Horas"
    } else {
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        "${minutes.twoDigits()}:${seconds.twoDigits()} Minutos"
    }
}