package br.com.hellodev.design.presenter.components.bottom.sheet.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import br.com.hellodev.core.enums.theme.ThemeType
import br.com.hellodev.design.presenter.components.bottom.sheet.header.HeaderBottomSheet
import br.com.hellodev.design.presenter.components.button.AlertButton
import br.com.hellodev.design.presenter.components.button.SecondaryButton
import br.com.hellodev.design.presenter.components.item.video.VideoItem
import br.com.hellodev.design.presenter.components.spacer.VerticalSpacer
import br.com.hellodev.design.presenter.theme.ColorScheme
import br.com.hellodev.design.presenter.theme.HelloTheme
import br.com.hellodev.design.presenter.theme.ShapeBottomSheet
import br.com.hellodev.design.provider.preview.LightDarkModePreviewProvider
import br.com.hellodev.domain.model.video.Video
import org.jetbrains.compose.resources.stringResource
import play.design.generated.resources.Res
import play.design.generated.resources.text_first_button_remove_sheet_content
import play.design.generated.resources.text_second_button_remove_sheet_content
import play.design.generated.resources.text_title_remove_video_sheet_content

@Composable
fun RemoveVideoSheetContent(
    modifier: Modifier = Modifier,
    video: Video,
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ColorScheme.colorScheme.screen.backgroundSecondary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderBottomSheet(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            title = stringResource(Res.string.text_title_remove_video_sheet_content)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            VideoItem(video = video)

            VerticalSpacer(size = 24)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecondaryButton(
                    modifier = Modifier
                        .weight(1f),
                    text = stringResource(Res.string.text_second_button_remove_sheet_content),
                    onClick = onCancelClick
                )

                AlertButton(
                    modifier = Modifier
                        .weight(1f),
                    text = stringResource(Res.string.text_first_button_remove_sheet_content),
                    onClick = onConfirmClick
                )
            }
        }
    }
}

@Preview
@Composable
private fun RemoveVideoSheetContentPreview(
    @PreviewParameter(LightDarkModePreviewProvider::class) type: ThemeType
) {
    HelloTheme(themeType = type) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorScheme.colorScheme.screen.backgroundPrimary),
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = ColorScheme.colorScheme.screen.backgroundSecondary,
                        shape = ShapeBottomSheet
                    )
            ) {
                RemoveVideoSheetContent(
                    video = Video(
                        name = "A Era do Gelo",
                        sizeInBytes = 1526999998L,
                        durationMillis = 152699L
                    ),
                    onCancelClick = {},
                    onConfirmClick = {}
                )
            }
        }
    }
}