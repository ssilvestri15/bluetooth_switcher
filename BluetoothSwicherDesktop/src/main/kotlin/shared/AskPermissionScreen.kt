package shared

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import java.net.Socket

@Composable
fun AskPermissionScreen(
    clientSocket: Socket,
    connectionRequest: NearbyManager.ConnectionRequest,
    onDeviceApproved: () -> Unit,
    onDeviceDenied: () -> Unit
) {
    Row(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxHeight(1f)
                .weight(1f)
                .padding(start = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column {
                Text(
                    text = "Connecting with",
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                )
                Text(
                    text = connectionRequest.host,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
                Text(
                    text = "A device is trying to connect to your PC. Allow it?",
                    color = Color.White,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 30.dp)
                )

                Column(
                    modifier = Modifier.padding(70.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF074cf2)),
                        onClick = {
                            onDeviceApproved()
                        }
                    ) {
                        Text(
                            text = "Approve",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 2.dp, start = 40.dp, end = 40.dp, bottom = 2.dp)
                        )
                    }
                    Text(
                        text = "Deny",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 15.dp).clickable {
                            onDeviceDenied()
                        },
                    )
                }

            }
        }
        Column(
            modifier = Modifier
                .fillMaxHeight(1f)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource("images/phone.png"),
                contentDescription = "phone image"
            )
        }
    }
}