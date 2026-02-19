package br.com.hellodev.play.list.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.hellodev.design.presenter.theme.ColorScheme
import br.com.hellodev.play.VideoItem
import br.com.hellodev.play.VideoListItem
import br.com.hellodev.play.VideoPlayerScreen
import br.com.hellodev.play.list.action.VideoListAction
import br.com.hellodev.play.list.di.videoListModule
import br.com.hellodev.play.rememberVideoAccessState
import br.com.hellodev.play.rememberVideoDataSource
import br.com.hellodev.play.list.viewmodel.VideoListViewModel
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun VideoListScreen() {
    val videoAccessState = rememberVideoAccessState()
    val dataSource = rememberVideoDataSource()

    val koin = getKoin()
    remember(koin) {
        koin.loadModules(listOf(videoListModule))
    }

    val viewModel = koinViewModel<VideoListViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(videoAccessState.hasAccess, dataSource) {
        viewModel.dispatchAction(
            VideoListAction.Refresh(
                hasAccess = videoAccessState.hasAccess,
                dataSource = dataSource,
            )
        )
    }

    val selectedVideo = state.selectedVideo
    if (selectedVideo != null) {
        VideoPlayerScreen(
            video = selectedVideo,
            onBack = { viewModel.dispatchAction(VideoListAction.OnPlayerBack) },
        )
        return
    }

    Scaffold(
        containerColor = ColorScheme.colorScheme.screen.backgroundPrimary,
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            when {
                state.isLoading -> {
                    Text(text = "Carregando videos...")
                }

                state.isPermissionRequired -> {
                    Text(text = "Permita acesso aos videos e imagens para listar os arquivos da pasta \"videos\" e usar miniaturas.")
                    Button(onClick = videoAccessState.requestAccess) {
                        Text("Permitir acesso")
                    }
                }

                state.errorMessage != null -> {
                    state.errorMessage?.let { message ->
                        Text(text = message)
                    }
                }

                state.videos.isEmpty() -> {
                    Text(text = "Nenhum video encontrado na pasta \"videos\".")
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(
                            top = paddingValues.calculateTopPadding() + 16.dp,
                            bottom = paddingValues.calculateBottomPadding() + 16.dp,
                        ),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(state.videos, key = VideoItem::id) { video ->
                            VideoListItem(
                                video = video,
                                onClick = { viewModel.dispatchAction(VideoListAction.OnVideoClick(video)) },
                            )
                        }
                    }
                }
            }
        }
    }
}
