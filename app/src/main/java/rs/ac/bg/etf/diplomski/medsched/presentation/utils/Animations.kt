package rs.ac.bg.etf.diplomski.medsched.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.compose.*
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.Blue85

@Composable
fun Loader() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.dot_loader))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            keyPath = arrayOf("**"),
            property = LottieProperty.COLOR_FILTER,
            value = SimpleColorFilter(Blue85.toArgb())
        )
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        dynamicProperties = dynamicProperties
    )
}