package br.com.hellodev.core.extensions.integer

fun Int?.orZero(): Int = this ?: 0

fun Int?.isNotZero(): Boolean = this.orZero() != 0

fun Int?.isZero(): Boolean = this == 0