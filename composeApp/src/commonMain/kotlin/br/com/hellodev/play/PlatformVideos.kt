package br.com.hellodev.play

import androidx.compose.runtime.Composable

@Composable
expect fun rememberVideoDataSource(): VideoDataSource

@Composable
expect fun rememberVideoAccessState(): VideoAccessState
