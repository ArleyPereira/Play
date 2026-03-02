package br.com.hellodev.core.extensions.long

fun Long?.orZero(): Long = this ?: 0

fun Long?.isNotZero(): Boolean = this.orZero() != 0L

fun Long?.isZero(): Boolean = this == 0L