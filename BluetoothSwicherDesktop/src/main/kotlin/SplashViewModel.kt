import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.*


data class SplashState(
    var showLoading: Boolean = false,
    var screen: String
)

class SplashViewModel {

    var state = mutableStateOf(SplashState(screen = Screen.Main.SPLASH_SCREEN))
        private set


    init {
        goTo()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun goTo() {
        GlobalScope.launch {
            launch {
                delay(2000)
                if (isActive)
                    state.value = state.value.copy(showLoading = true)
            }
            launch {
                delay(1000)
                if (isActive)
                    state.value = SplashState(screen = Screen.Main.ONBOARDING_SCREEN)
            }
        }
    }

}