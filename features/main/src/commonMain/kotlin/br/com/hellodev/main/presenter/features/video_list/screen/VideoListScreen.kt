package br.com.hellodev.main.presenter.features.video_list.screen

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.hellodev.design.presenter.theme.ColorScheme
import br.com.hellodev.main.data.VideoItem
import br.com.hellodev.main.data.rememberVideoAccessState
import br.com.hellodev.main.data.rememberVideoDataSource
import br.com.hellodev.main.presenter.features.video_list.action.VideoListAction
import br.com.hellodev.main.presenter.features.video_list.component.VideoListItem
import br.com.hellodev.main.presenter.features.video_list.viewmodel.VideoListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun VideoListScreen(
    onVideoClick: (VideoItem) -> Unit,
) {
    val videoAccessState = rememberVideoAccessState()
    val dataSource = rememberVideoDataSource()
    val viewModel = koinViewModel<VideoListViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(videoAccessState.hasAccess, dataSource) {
        viewModel.dispatchAction(
            VideoListAction.Refresh(
                hasAccess = videoAccessState.hasAccess,
                dataSource = dataSource,
            ),
        )
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
                                onClick = {
                                    viewModel.dispatchAction(VideoListAction.OnVideoClick(video))
                                    onVideoClick(video)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
