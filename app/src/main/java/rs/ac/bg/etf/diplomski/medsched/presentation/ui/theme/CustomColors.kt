package rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

val Colors.textOnPrimary: Color
    get() = if (isLight) TextOnPrimaryLight else TextOnPrimaryDark

val Colors.selectable: Color
    get() = if (isLight) SelectableLight else SelectableDark

val Colors.selectableBottomNavbar: Color
    get() = if (isLight) SelectableLight else BackgroundPrimaryDark

val Colors.textFieldBackground: Color
    get() = if (isLight) TextFieldBackgroundLight else TextFieldBackgroundDark

val Colors.textFieldText: Color
    get() = if (isLight) TextFieldTextLight else TextFieldTextDark

val Colors.textFieldOutline: Color
    get() = if (isLight) TextFieldOutlineLight else TextFieldOutlineDark

val Colors.success: Color
    get() = if (isLight) SuccessLight else SuccessDark

val Colors.logo: Color
    get() = if (isLight) BackgroundSecondaryLight else BackgroundPrimaryLight