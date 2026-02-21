package br.com.hellodev.core.extensions

import kotlin.math.pow
import kotlin.math.round

fun Double.fixed(decimals: Int): String {
    val factor = 10.0.pow(decimals)
    val rounded = round(this * factor) / factor
    val raw = rounded.toString()

    val dotIndex = raw.indexOf('.')
    if (decimals == 0) return raw.substringBefore('.')
    if (dotIndex == -1) return "$raw.${"0".repeat(decimals)}"

    val currentDecimals = raw.length - dotIndex - 1
    if (currentDecimals >= decimals) return raw
    return raw + "0".repeat(decimals - currentDecimals)
}

fun Long.twoDigits(): String = if (this < 10) "0$this" else this.toString()

fun String.fileNameWithoutExtension(): String {
    val trimmed = this.trim()
    if (trimmed.isEmpty()) return this
    return trimmed.substringBeforeLast('.', missingDelimiterValue = trimmed)
}