package rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = BackgroundPrimaryDark,
    primaryVariant = BackgroundSecondaryDark,
    secondary = BackgroundSecondaryDark,
    background = DarkBackgroundColor
)

private val LightColorPalette = lightColors(
    primary = BackgroundPrimaryLight,
    primaryVariant = Purple700,
    secondary = BackgroundSecondaryLight,
    background = BackgroundPrimaryLight

    /* Other default colors to override

    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun MedSchedTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}