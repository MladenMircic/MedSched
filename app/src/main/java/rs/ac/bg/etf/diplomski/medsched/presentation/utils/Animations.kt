package rs.ac.bg.etf.diplomski.medsched.presentation.utils

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.compose.*
import rs.ac.bg.etf.diplomski.medsched.R

@Composable
fun HorizontalDotLoader(color: Color) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(
        R.raw.horizontal_dot_loading)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            keyPath = arrayOf("**"),
            property = LottieProperty.COLOR_FILTER,
            value = SimpleColorFilter(color.toArgb())
        )
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        dynamicProperties = dynamicProperties,
        modifier = Modifier
            .size(100.dp)
            .graphicsLayer {
                scaleX = 6f
                scaleY = 6f
            }
    )
}

@Composable
fun CircleDotLoader(
    modifier: Modifier = Modifier,
    color: Color
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(
        R.raw.circle_dot_loading)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            keyPath = arrayOf("**"),
            property = LottieProperty.COLOR_FILTER,
            value = SimpleColorFilter(color.toArgb())
        )
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        dynamicProperties = dynamicProperties,
        modifier = modifier
    )
}

@Composable
fun PulseRefreshLoading(
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    automatic: Boolean = true,
    color: Color
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(
        R.raw.pulse_loading)
    )
    val animationProgress: Float = if (automatic) {
        animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever
        ).value
    }
    else progress

    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            keyPath = arrayOf("**"),
            property = LottieProperty.COLOR_FILTER,
            value = SimpleColorFilter(color.toArgb())
        )
    )
    LottieAnimation(
        composition = composition,
        progress = { animationProgress },
        dynamicProperties = dynamicProperties,
        modifier = modifier
    )
}

@Composable
fun EditAnimation(
    modifier: Modifier = Modifier,
    color: Color,
    progress: Float
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(
        R.raw.fab_edit)
    )
    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            keyPath = arrayOf("**"),
            property = LottieProperty.COLOR_FILTER,
            value = SimpleColorFilter(color.toArgb())
        )
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        dynamicProperties = dynamicProperties,
        modifier = modifier
    )
}
