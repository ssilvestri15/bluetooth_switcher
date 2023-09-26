package onboarding

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(Modifier.fillMaxSize(0.25f)) {
            Pulsating()
        }
        Text(
            text = "Aspettando i dispositivi",
            modifier = Modifier.padding(top = 25.dp),
            color = Color.White,
            fontWeight = FontWeight.Light,
        )
    }

}

@Composable
fun Pulsating(pulseFraction: Float = 1.2f) {
    val infiniteTransition = rememberInfiniteTransition()

    val outerScaleAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val innerScaleAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                0.5f at 1500 // Start at half of the outer circle animation duration
                1f at 3000 // End at the same time as the outer circle animation
            },
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val min = size.minDimension * 0.1f
        val outerRadius = min + (4 * min - min) * outerScaleAnimation
        val innerRadius = min + (4 * min - min) * innerScaleAnimation

        drawCircle(
            color = Color.White.copy(alpha = 0.9f),
            radius = min,
            center = Offset(centerX, centerY)
        )

        val alpha = 0.8f-outerScaleAnimation
        drawCircle(
            color = Color.White.copy(alpha = if (alpha < 0) 0f else alpha),
            radius = outerRadius,
            center = Offset(centerX, centerY)
        )

        val alpha2 = 0.8f-innerScaleAnimation
        drawCircle(
            color = Color.White.copy(alpha = if (alpha2 < 0) 0f else alpha2),
            radius = innerRadius,
            center = Offset(centerX, centerY)
        )


    }
}