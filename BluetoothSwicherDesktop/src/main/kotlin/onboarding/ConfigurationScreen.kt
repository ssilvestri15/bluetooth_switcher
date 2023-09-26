package onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConfigurationScreen(onButtonClicked: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(start = 160.dp, end = 160.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.height(200.dp).width(200.dp)
        ){}
        Text(
            text = "Inquadra il QRCode",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp)
        )
        Text(
            text = "Oppure cerca “BluetoothSwitcher” sul Google Play Store e segui le istruzioni",
            fontWeight = FontWeight.Light,
            fontSize = 16.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp, start = 20.dp, end = 20.dp)
        )
        Button(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3543f6)),
            onClick = {
                onButtonClicked()
            }
        ) {
            Text(
                text = "Ho aperto l’app sul telefono",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp, start = 40.dp, end = 40.dp, bottom = 10.dp)
            )
        }
    }
}