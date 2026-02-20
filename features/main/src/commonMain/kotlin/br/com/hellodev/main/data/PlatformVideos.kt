package br.com.hellodev.main.data

import androidx.compose.runtime.Composable

@Composable
expect fun rememberVideoDataSource(): VideoDataSource

@Composable
expect fun rememberVideoAccessState(): VideoAccessState
