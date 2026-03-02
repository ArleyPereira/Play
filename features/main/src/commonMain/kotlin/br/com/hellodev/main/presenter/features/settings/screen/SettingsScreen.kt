package br.com.hellodev.main.presenter.features.settings.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.hellodev.core.enums.illustration.IllustrationType.IC_FOLDER_FILL
import br.com.hellodev.design.presenter.components.button.PrimaryButton
import br.com.hellodev.design.presenter.components.card.default.DefaultCardUI
import br.com.hellodev.design.presenter.components.icon.illustration.getDrawableIllustration
import br.com.hellodev.design.presenter.components.snackbar.FeedbackUI
import br.com.hellodev.design.presenter.theme.ColorScheme
import br.com.hellodev.design.presenter.theme.HelloTheme
import br.com.hellodev.core.enums.theme.ThemeType
import br.com.hellodev.design.presenter.theme.borderDefault
import br.com.hellodev.design.presenter.theme.helloFontFamily
import br.com.hellodev.design.provider.preview.LightDarkModePreviewProvider
import br.com.hellodev.main.presenter.features.settings.action.SettingsAction
import br.com.hellodev.main.presenter.features.settings.state.SettingsState
import br.com.hellodev.main.presenter.features.settings.viewmodel.SettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    paddingValues: PaddingValues,
) {
    val viewModel = koinViewModel<SettingsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsContent(
        paddingValues = paddingValues,
        state = state,
        action = viewModel::dispatchAction
    )
}

@Composable
fun SettingsContent(
    paddingValues: PaddingValues,
    state: SettingsState,
    action: (SettingsAction) -> Unit
) {
    Scaffold(
        containerColor = ColorScheme.colorScheme.screen.backgroundPrimary,
        bottomBar = {
            state.feedback?.let { feedback ->
                FeedbackUI(
                    modifier = Modifier
                        .padding(
                            bottom = paddingValues.calculateBottomPadding()
                        ),
                    feedback = feedback,
                    onDismiss = {
                        action(SettingsAction.DismissFeedback)
                    }
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "Pasta atual dos vídeos",
                    style = TextStyle(
                        color = ColorScheme.colorScheme.text.primaryColor,
                        fontFamily = helloFontFamily(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight(700),
                    ),
                )

                Text(
                    text = "Gerencie o local onde seus vídeos estão armazenados.",
                    style = TextStyle(
                        fontFamily = helloFontFamily(),
                        color = ColorScheme.colorScheme.text.secondaryColor,
                        fontSize = 13.sp,
                    ),
                )
            }

            DefaultCardUI(
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = ColorScheme.colorScheme.screen.backgroundSecondary,
                                    shape = RoundedCornerShape(16.dp),
                                )
                                .borderDefault(shape = RoundedCornerShape(16.dp))
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Icon(
                                    painter = getDrawableIllustration(type = IC_FOLDER_FILL),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = ColorScheme.colorScheme.defaultColor,
                                )

                                Text(
                                    text = "CAMINHO DO DIRETÓRIO",
                                    style = TextStyle(
                                        color = ColorScheme.colorScheme.text.secondaryColor,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight(700),
                                    ),
                                )
                            }

                            Text(
                                text = state.currentFolderPath,
                                style = TextStyle(
                                    color = ColorScheme.colorScheme.text.primaryColor,
                                    fontSize = 13.sp,
                                    fontFamily = FontFamily.Monospace,
                                ),
                            )
                        }

                        state.errorMessage?.let { message ->
                            Text(
                                text = message,
                                style = TextStyle(
                                    fontFamily = helloFontFamily(),
                                    color = ColorScheme.colorScheme.alertColor,
                                    fontSize = 12.sp,
                                ),
                            )
                        }

                        PrimaryButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Reindexar arquivos",
                            isLoading = state.isLoading,
                            onClick = {
                                action(SettingsAction.OnReindexFiles)
                            },
                        )
                    }
                },
            )

            DefaultCardUI(
                shape = RoundedCornerShape(16.dp),
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Text(
                            text = "Informações",
                            style = TextStyle(
                                color = ColorScheme.colorScheme.defaultColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                        )

                        HorizontalDivider(color = ColorScheme.colorScheme.divider.color)

                        Text(
                            text = "Ao reindexar, os arquivos da pasta pública serão copiados para o armazenamento privado do app (play/videos e play/thumbs). A listagem e reprodução usam os arquivos privados.",
                            style = TextStyle(
                                color = ColorScheme.colorScheme.text.secondaryColor,
                                fontSize = 13.sp,
                                lineHeight = 20.sp,
                            ),
                        )
                    }
                },
            )
        }
    }
}

@Preview
@Composable
private fun SettingsPreview(
    @PreviewParameter(LightDarkModePreviewProvider::class) type: ThemeType,
) {
    HelloTheme(themeType = type) {
        SettingsContent(
            paddingValues = PaddingValues(),
            state = SettingsState(),
            action = {}
        )
    }
}
