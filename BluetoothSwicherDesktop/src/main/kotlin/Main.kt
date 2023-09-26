import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.mayakapps.compose.windowstyler.WindowBackdrop
import com.mayakapps.compose.windowstyler.WindowCornerPreference
import com.mayakapps.compose.windowstyler.WindowFrameStyle
import com.mayakapps.compose.windowstyler.WindowStyle
import onboarding.OnBoardingScreen

@Composable
@Preview
fun App() {
}


fun main() = application {
    Window(
        state = rememberWindowState(position = WindowPosition(Alignment.Center)),
        title = "BluetoothSwitcher",
        icon = painterResource("images/BluetoothSwitcher.png"),
        onCloseRequest = ::exitApplication
    ) {

        val navController = rememberSaveable { NavController() }

        WindowStyle(
            isDarkTheme = true,
            backdropType = WindowBackdrop.Mica,
            frameStyle = WindowFrameStyle(cornerPreference = WindowCornerPreference.ROUNDED),
        )

        MaterialTheme {

            NavHost(startDestination = Screen.Main.SPLASH_SCREEN, navController) {
                composable(Screen.Main.SPLASH_SCREEN) {
                    SplashScreen(navController)
                }
                composable(Screen.Main.ONBOARDING_SCREEN) {
                    OnBoardingScreen(navController)
                }
                composable(Screen.Main.HOME_SCREEN) {
                    HomeScreen()
                }
            }.let {
                navController.navigateTo(Screen.Main.SPLASH_SCREEN)
            }


        }
    }
}