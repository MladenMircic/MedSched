package rs.ac.bg.etf.diplomski.medsched.presentation.utils

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import rs.ac.bg.etf.diplomski.medsched.presentation.composables.defaultButtonColors
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.RoundedShape20
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.textFieldText

@Composable
fun CustomOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    leadingIconImageVector: ImageVector,
    leadingIconImageDescription: String = "",
    isPasswordField: Boolean = false,
    isPasswordVisible: Boolean = false,
    onVisibilityChange: (Boolean) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    showError: Boolean = false,
    errorMessage: String = "",
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .padding(bottom = 10.dp),
            label = { Text(text = label) },
            leadingIcon = {
                Icon(
                    imageVector = leadingIconImageVector,
                    contentDescription = leadingIconImageDescription,
                    tint = if (showError)
                        MaterialTheme.colors.error
                    else
                        MaterialTheme.colors.textFieldText
                )
            },
            shape = RoundedShape20,
            isError = showError,
            trailingIcon = {
                if (showError && !isPasswordField) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "Show error icon"
                    )
                }
                if (isPasswordField) {
                    IconButton(
                        onClick = { onVisibilityChange(!isPasswordVisible) }
                    ) {
                        Icon(
                            imageVector = if (isPasswordVisible)
                                Icons.Filled.Visibility
                            else
                                Icons.Filled.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            },
            visualTransformation = when {
                isPasswordField && isPasswordVisible -> VisualTransformation.None
                isPasswordField -> PasswordVisualTransformation()
                else -> VisualTransformation.None
            },
            colors = defaultButtonColors(),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true
        )
        if (showError) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                modifier = modifier
                    .padding(start = 8.dp)
                    .offset(y = (-8).dp)
            )
        }
    }
}