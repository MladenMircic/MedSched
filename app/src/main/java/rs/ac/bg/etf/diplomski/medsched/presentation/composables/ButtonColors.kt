package rs.ac.bg.etf.diplomski.medsched.presentation.composables

import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.textFieldBackground
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.textFieldOutline
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.textFieldText

@Composable
fun defaultButtonColors(): TextFieldColors = TextFieldDefaults.textFieldColors(
    focusedLabelColor = MaterialTheme.colors.textFieldOutline,
    focusedIndicatorColor = MaterialTheme.colors.textFieldOutline,
    backgroundColor = MaterialTheme.colors.textFieldBackground,
    textColor = MaterialTheme.colors.textFieldText,
    cursorColor = MaterialTheme.colors.textFieldText
)