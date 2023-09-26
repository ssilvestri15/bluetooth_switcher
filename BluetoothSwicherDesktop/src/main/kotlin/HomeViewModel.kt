import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import onboarding.NearbyManagerListener
import java.net.Socket

data class HomeState(
    val connectedDevices: List<ConnectedDevice> = emptyList(),
    val showLoading: Boolean = false,
    val showAskForConnection: Boolean = false
)

class HomeViewModel() {

    val state: MutableState<HomeState> = mutableStateOf(HomeState())

    private val manager: MutableState<NearbyManager> = mutableStateOf(NearbyManager.getInstance())
    private val listener = object : NearbyManagerListener {
        override fun onAskPermission(clientSocket: Socket, connectionRequest: NearbyManager.ConnectionRequest) {
            state.value = state.value.copy(showAskForConnection = true)
        }

        override fun onMessageReceived() {
        }

        override fun onDeviceApproved() {
        }

        override fun onNewDeviceConnected() {
            state.value = state.value.copy(connectedDevices = manager.value.clients.values.toList())
        }
    }

    init {
        manager.value.listener = listener
        state.value = state.value.copy(connectedDevices = manager.value.clients.values.toList())
    }

}
