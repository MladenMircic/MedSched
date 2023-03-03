package rs.ac.bg.etf.diplomski.medsched.presentation.utils

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.calendarField
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.textFieldOutline

@Composable
fun BorderAnimatedItem(
    isSelected: Boolean,
    borderColor: Color,
    onSelect: () -> Unit,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val borderWidth by animateDpAsState(
        targetValue = if (isSelected) 3.dp else 0.dp,
        animationSpec = tween(durationMillis = 300)
    )
    val contentScale by animateFloatAsState(
        targetValue = if (isSelected) 0.85f else 1f,
        animationSpec = tween(durationMillis = 300)
    )

    Card(
        shape = RoundedCornerShape(10.dp),
        backgroundColor = MaterialTheme.colors.calendarField,
        contentColor = if (isSelected)
            Color.White
        else MaterialTheme.colors.textFieldOutline,
        border = if (isSelected)
            BorderStroke(borderWidth, borderColor)
        else null,
        modifier = Modifier
            .size(50.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onSelect()
            }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .graphicsLayer {
                    scaleX = contentScale
                    scaleY = contentScale
                }
                .fillMaxSize()
        ) {
            content()
        }
    }
}