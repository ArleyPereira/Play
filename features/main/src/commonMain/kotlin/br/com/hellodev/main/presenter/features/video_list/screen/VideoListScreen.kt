package br.com.hellodev.main.presenter.features.video_list.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.hellodev.core.enums.sheet.BottomSheetType.REMOVE_VIDEO
import br.com.hellodev.design.presenter.components.bottom.sheet.content.RemoveVideoSheetContent
import br.com.hellodev.design.presenter.components.bottom.sheet.default.DefaultBottomSheet
import br.com.hellodev.design.presenter.components.card.video.VideoCard
import br.com.hellodev.design.presenter.components.loading.CircularProgressLoading
import br.com.hellodev.design.presenter.components.spacer.VerticalSpacer
import br.com.hellodev.design.presenter.theme.ColorScheme
import br.com.hellodev.domain.model.video.Video
import br.com.hellodev.main.data.rememberVideoDataSource
import br.com.hellodev.main.presenter.features.video_list.action.VideoListAction
import br.com.hellodev.main.presenter.features.video_list.reindex.VideoListReindexSignal
import br.com.hellodev.main.presenter.features.video_list.state.VideoListState
import br.com.hellodev.main.presenter.features.video_list.viewmodel.VideoListViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun VideoListScreen(
    paddingValues: PaddingValues = PaddingValues(),
    navigateToVideoPlayerScreen: (Video) -> Unit,
    onPermissionRequired: () -> Unit
) {
    val dataSource = rememberVideoDataSource()
    val viewModel = koinViewModel<VideoListViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val reindexSignal by VideoListReindexSignal.signal.collectAsStateWithLifecycle()

    LaunchedEffect(dataSource) {
        viewModel.dispatchAction(
            VideoListAction.Refresh(dataSource = dataSource)
        )
    }

    LaunchedEffect(reindexSignal) {
        if (reindexSignal > 0L) {
            viewModel.dispatchAction(
                VideoListAction.Refresh(dataSource = dataSource)
            )
        }
    }

    LaunchedEffect(state.isPermissionRequired) {
        if (state.isPermissionRequired) {
            onPermissionRequired()
        }
    }

    VideoListContent(
        paddingValues = paddingValues,
        state = state,
        action = viewModel::dispatchAction,
        navigateToVideoPlayerScreen = navigateToVideoPlayerScreen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VideoListContent(
    paddingValues: PaddingValues = PaddingValues(),
    state: VideoListState,
    action: (VideoListAction) -> Unit,
    navigateToVideoPlayerScreen: (Video) -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Scaffold(
        containerColor = ColorScheme.colorScheme.screen.backgroundPrimary
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            when {
                state.isLoading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressLoading()

                        VerticalSpacer(size = 16)

                        Text(
                            text = "Carregando videos...",
                            style = TextStyle(
                                color = ColorScheme.colorScheme.text.primaryColor
                            )
                        )
                    }
                }

                state.isPermissionRequired -> {
                    Text(text = "Permissao necessaria para continuar.")
                }

                state.errorMessage != null -> {
                    Text(text = state.errorMessage)
                }

                state.videos.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Verifique se tem videos salvos no dispositivo e que a permissão de acesso aos vídeos e fotos esteja liberada.",
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = TextStyle(
                                color = ColorScheme.colorScheme.text.primaryColor,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentPadding = paddingValues,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(
                            items = state.videos,
                            key = { it.id.orEmpty() }
                        ) { video ->
                            VideoCard(
                                video = video,
                                onClick = {
                                    navigateToVideoPlayerScreen(video)
                                },
                                onDeleteSwipe = {
                                    action(VideoListAction.OnVideoSelected(video))
                                }
                            )
                        }
                    }
                }
            }
        }

        state.sheetModel?.let { sheetModel ->
            DefaultBottomSheet(
                onDismissRequest = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        action(VideoListAction.ClearBottomSheet)
                    }
                },
                sheetState = sheetState,
                content = {
                    when (sheetModel.type) {
                        REMOVE_VIDEO -> {
                            state.videoSelected?.let { video ->
                                RemoveVideoSheetContent(
                                    video = video,
                                    onCancelClick = {
                                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                                            action(VideoListAction.ClearBottomSheet)
                                        }
                                    },
                                    onConfirmClick = {
                                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                                            action(VideoListAction.ClearBottomSheet)
                                        }
                                    }
                                )
                            }
                        }

                        else -> {}
                    }
                }
            )
        }
    }
}