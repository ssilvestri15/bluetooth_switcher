package onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
fun WelcomeScreen(
    onButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(start = 160.dp, end = 160.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource("images/phone.png"), "")
        Text(
            text = "Connetti il tuo telefono",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp)
        )
        Text(
            text = "Per una migliore esperienza scarica l’applicazione anche sul tuo smartphone Andorid.",
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
                text = "Scarica",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp, start = 40.dp, end = 40.dp, bottom = 10.dp)
            )
        }
    }
}