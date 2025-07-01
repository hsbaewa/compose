package kr.co.hs.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
private fun PreviewIndeterminateCircularIndicator() {
    IndeterminateCircularIndicator(loading = true)
}

@Composable
fun IndeterminateCircularIndicator(
    modifier: Modifier = Modifier,
    text: String = "",
    backgroundColor: Color = Color(0xFF000000).copy(alpha = 0.8f),
    foregroundColor: Color = Color(0xFFFFFFFF).copy(alpha = 0.8f),
    loading: Boolean
) {
    if (!loading) return

    Box(
        modifier = modifier
            .background(backgroundColor)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {}
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(70.dp),
                color = foregroundColor,
                strokeWidth = 7.dp
            )

            if (text.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = text,
                    style = TextStyle(
                        color = foregroundColor,
                        fontSize = 27.5.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight(400),
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}