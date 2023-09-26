package onboarding

import NavController
import NavHost
import Screen
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import shared.AskPermissionScreen
import java.net.Socket

interface NearbyManagerListener {
    fun onAskPermission(clientSocket: Socket, connectionRequest: NearbyManager.ConnectionRequest)
    fun onMessageReceived()
    fun onDeviceApproved()
    fun onNewDeviceConnected()
}

data class AskRequest(
    val clientSocket: Socket,
    val connectionRequest: NearbyManager.ConnectionRequest
)

@Composable
@Preview
fun OnBoardingScreen(
    mainController: NavController
) {


    var askRequest: AskRequest? by remember { mutableStateOf(null) }
    var isDeviceApproved by remember { mutableStateOf(false) }

    if (isDeviceApproved) {
        mainController.navigateTo(Screen.Main.HOME_SCREEN)
        return
    }

    val page = remember { mutableStateOf(Screen.OnBoarding.WELCOME_SCREEN) }

    val manager by remember {
        mutableStateOf(NearbyManager.getInstance(listener = object : NearbyManagerListener {
            override fun onAskPermission(clientSocket: Socket, connectionRequest: NearbyManager.ConnectionRequest) {
                askRequest = AskRequest(clientSocket, connectionRequest)
                page.value = Screen.Shared.ASK_PERMISSION_SCREEN
            }

            override fun onMessageReceived() {
            }

            override fun onDeviceApproved() {
                isDeviceApproved = true
            }
            override fun onNewDeviceConnected() {}
        }))
    }

    when (page.value) {
        Screen.OnBoarding.WELCOME_SCREEN -> {
            WelcomeScreen {
                page.value = Screen.OnBoarding.CONFIG_SCREEN
            }
        }

        Screen.OnBoarding.CONFIG_SCREEN -> {
            ConfigurationScreen {
                page.value = Screen.OnBoarding.LOADING_SCREEN
            }
        }

        Screen.OnBoarding.LOADING_SCREEN -> {
            LoadingScreen()
            manager.startAdvertising()
        }

        Screen.Shared.ASK_PERMISSION_SCREEN -> {
            askRequest?.let {
                AskPermissionScreen(
                    clientSocket = it.clientSocket,
                    connectionRequest = it.connectionRequest,
                    onDeviceApproved = {
                        manager.approveDevice(it.clientSocket, it.connectionRequest)
                    },
                    onDeviceDenied = {
                        page.value = Screen.OnBoarding.LOADING_SCREEN
                    }
                )
            }
        }
    }

}
