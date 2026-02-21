package br.com.hellodev.onboarding.presenter.features.permission.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import br.com.hellodev.design.presenter.components.button.PrimaryButton
import br.com.hellodev.design.presenter.theme.ColorScheme
import br.com.hellodev.onboarding.presenter.features.permission.platform.rememberOpenAppSettingsAction
import br.com.hellodev.onboarding.presenter.features.permission.platform.rememberPermanentPermissionFlagStore
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import dev.icerock.moko.permissions.gallery.GALLERY
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import play.features.onboarding.generated.resources.Res
import play.features.onboarding.generated.resources.permission_placeholder

@Composable
fun PermissionScreen(
    navigateToHomeScreen: () -> Unit
) {
    val permissionsControllerFactory = rememberPermissionsControllerFactory()
    val permissionsController = remember(permissionsControllerFactory) {
        permissionsControllerFactory.createPermissionsController()
    }
    BindEffect(permissionsController)
    val permanentPermissionFlagStore = rememberPermanentPermissionFlagStore()

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isPermissionPermanentlyDenied by remember {
        mutableStateOf(permanentPermissionFlagStore.isPermanentlyDenied())
    }
    var isNavigated by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val openAppSettings = rememberOpenAppSettingsAction()
    val lifecycleOwner = LocalLifecycleOwner.current

    fun navigateToVideoList() {
        if (!isNavigated) {
            isNavigated = true
            navigateToHomeScreen()
        }
    }

    LaunchedEffect(permissionsController) {
        val hasPermission = permissionsController.isPermissionGranted(Permission.GALLERY)
        if (hasPermission) {
            isPermissionPermanentlyDenied = false
            permanentPermissionFlagStore.setPermanentlyDenied(false)
            navigateToVideoList()
        }
    }
    DisposableEffect(lifecycleOwner, permissionsController) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                scope.launch {
                    val hasPermission =
                        permissionsController.isPermissionGranted(Permission.GALLERY)
                    if (hasPermission) {
                        isPermissionPermanentlyDenied = false
                        permanentPermissionFlagStore.setPermanentlyDenied(false)
                        errorMessage = null
                        navigateToVideoList()
                    }
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        containerColor = ColorScheme.colorScheme.screen.backgroundPrimary,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Image(
                        painter = painterResource(Res.drawable.permission_placeholder),
                        contentDescription = null,
                        modifier = Modifier
                            .height(250.dp)
                    )

                    Spacer(modifier = Modifier.height(36.dp))

                    Text(
                        text = "Acesse seus videos",
                        style = TextStyle(
                            color = ColorScheme.colorScheme.text.primaryColor,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Para visualizar e reproduzir os videos salvos no dispositivo, precisamos da permissao para acessar os arquivos de midia.",
                        style = TextStyle(
                            color = ColorScheme.colorScheme.text.secondaryColor,
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Center
                        )
                    )

                    errorMessage?.let { message ->
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = message,
                            color = ColorScheme.colorScheme.alertColor,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                PrimaryButton(
                    onClick = {
                        scope.launch {
                            if (isPermissionPermanentlyDenied) {
                                val hasPermission =
                                    permissionsController.isPermissionGranted(Permission.GALLERY)
                                if (hasPermission) {
                                    isPermissionPermanentlyDenied = false
                                    permanentPermissionFlagStore.setPermanentlyDenied(false)
                                    errorMessage = null
                                    navigateToVideoList()
                                } else {
                                    openAppSettings()
                                }
                            } else {
                                errorMessage = null
                                try {
                                    permissionsController.providePermission(Permission.GALLERY)
                                    isPermissionPermanentlyDenied = false
                                    permanentPermissionFlagStore.setPermanentlyDenied(false)
                                    navigateToVideoList()
                                } catch (_: DeniedAlwaysException) {
                                    isPermissionPermanentlyDenied = true
                                    permanentPermissionFlagStore.setPermanentlyDenied(true)
                                    errorMessage =
                                        "Permissao negada permanentemente. Toque em Permitir para abrir as configurações do sistema."
                                } catch (_: DeniedException) {
                                    isPermissionPermanentlyDenied = false
                                    permanentPermissionFlagStore.setPermanentlyDenied(false)
                                    errorMessage =
                                        "Permissao negada. Toque em Permitir para tentar novamente."
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = if (isPermissionPermanentlyDenied) "Configuracoes" else "Permitir"
                )
            }
        }
    )
}
