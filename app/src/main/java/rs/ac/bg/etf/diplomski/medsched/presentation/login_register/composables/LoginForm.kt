package rs.ac.bg.etf.diplomski.medsched.presentation.login_register.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.*
import rs.ac.bg.etf.diplomski.medsched.commons.DEFAULT_FORM_PADDING
import rs.ac.bg.etf.diplomski.medsched.commons.LOGIN_BUTTON_HEIGHT
import rs.ac.bg.etf.diplomski.medsched.commons.LOGIN_BUTTON_PADDING
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.CustomOutlinedTextField

@Composable
fun LoginForm(
    email: String,
    emailError: String?,
    password: String,
    passwordError: String?,
    updateEmail: (String) -> Unit,
    updatePassword: (String) -> Unit,
    onLoginButtonClick: () -> Unit
) {

    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column {
        CustomOutlinedTextField(
            value = email,
            onValueChange = updateEmail,
            label = stringResource(id = R.string.email),
            showError = emailError != null,
            errorMessage = emailError ?: "",
            leadingIconImageVector = Icons.Default.Email,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier
                .fillMaxWidth(0.8f)
        )
        CustomOutlinedTextField(
            value = password,
            onValueChange = updatePassword,
            label = stringResource(id = R.string.password),
            isPasswordField = true,
            isPasswordVisible = passwordVisible,
            onVisibilityChange = { passwordVisible = it },
            showError = passwordError != null,
            errorMessage = passwordError ?: "",
            leadingIconImageVector = Icons.Default.Password,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth(0.8f)
        )
        Button(
            onClick = onLoginButtonClick,
            shape = RoundedShape20,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.selectable
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(LOGIN_BUTTON_HEIGHT)
                .padding(horizontal = LOGIN_BUTTON_PADDING, vertical = 10.dp),
        ) {
            Text(
                text = stringResource(id = R.string.login_button_text),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BackgroundPrimaryLight,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}