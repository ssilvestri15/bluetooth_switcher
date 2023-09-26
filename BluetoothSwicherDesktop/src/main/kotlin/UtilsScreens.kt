import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorScreen() {

}


@Composable
fun HomeScreen() {


    val viewModel = remember { HomeViewModel() }
    val selected = remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .padding(25.dp),
    ){
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            items(viewModel.state.value.connectedDevices.size) { index ->
                ConnectedDeviceItem(
                    selected = selected.value == index,
                    connectedDevice = viewModel.state.value.connectedDevices[index]
                ) {
                    selected.value = index
                }
            }
        }
        Text(
            text = "Bluetooth devices connected to your PC",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(top = 25.dp)
        )
    }



}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ConnectedDeviceItem(selected: Boolean = false, connectedDevice: ConnectedDevice, onClick: () -> Unit) {
    if (selected) {
        Card(
            backgroundColor = Color(0xFF3543f6),
            elevation = 0.dp,
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .height(120.dp)
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Image(
                    painter = painterResource("images/phone.png"),
                    contentDescription = connectedDevice.host,
                    contentScale = ContentScale.FillHeight
                )
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 15.dp)
                ) {
                    Text(
                        text = connectedDevice.host,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(
                        text = connectedDevice.host,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    } else {
        Card(
            modifier = Modifier
                .height(120.dp)
                .width(120.dp),
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            shape = RoundedCornerShape(10.dp),
            onClick = {
                onClick()
            },
        ) {
            Column(
                Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier.fillMaxHeight(0.70f),
                    painter = painterResource("images/phone.png"),
                    contentDescription = connectedDevice.host,
                    contentScale = ContentScale.FillHeight
                )
                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = connectedDevice.host,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
fun SplashScreen(navigator: NavController) {

    val viewModel = remember { SplashViewModel() }

    when (viewModel.state.value.screen) {
        Screen.Main.SPLASH_SCREEN -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(0.25f),
                    painter = painterResource("images/BluetoothSwitcher.png"),
                    contentDescription = "Logo"
                )
                if (viewModel.state.value.showLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(top = 50.dp),
                        color = Color.White,
                        strokeCap = StrokeCap.Round,
                    )
                }
            }
        }

        Screen.Main.ONBOARDING_SCREEN -> {
            navigator.navigateTo(Screen.Main.ONBOARDING_SCREEN)
        }

        Screen.Main.HOME_SCREEN -> {
            navigator.navigateTo(Screen.Main.HOME_SCREEN)
        }

        else -> {
            ErrorScreen()
        }
    }
}

@Composable
fun AskForConnectionScreen() {

}