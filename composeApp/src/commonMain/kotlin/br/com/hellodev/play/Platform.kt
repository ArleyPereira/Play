package br.com.hellodev.play

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform