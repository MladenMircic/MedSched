package rs.ac.bg.etf.diplomski.medsched.presentation.login_register.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.LOGIN_BUTTON_HEIGHT
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.states.RegisterState
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.BackgroundPrimaryLight
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.RoundedShape20
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.selectable
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.CustomOutlinedTextField

@Composable
fun RegisterForm(
    registerState: RegisterState,
    updateEmail: (String) -> Unit,
    updatePassword: (String) -> Unit,
    updateConfirmPassword: (String) -> Unit,
    updatePhone: (String) -> Unit,
    updateLBO: (String) -> Unit,
    onRegisterButtonClick: () -> Unit
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column {
        CustomOutlinedTextField(
            value = registerState.email,
            onValueChange = updateEmail,
            label = stringResource(id = R.string.email),
            showError = registerState.emailError != null,
            errorMessage = registerState.emailError?.let { stringResource(id = it) } ?: "",
            leadingIconImageVector = Icons.Filled.Email,
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
            value = registerState.password,
            onValueChange = updatePassword,
            label = stringResource(id = R.string.password),
            isPasswordField = true,
            isPasswordVisible = passwordVisible,
            onVisibilityChange = { passwordVisible = it },
            showError = registerState.passwordError != null,
            errorMessage = registerState.passwordError?.let { stringResource(id = it) } ?: "",
            leadingIconImageVector = Icons.Filled.Password,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier
                .fillMaxWidth(0.8f)
        )
        CustomOutlinedTextField(
            value = registerState.confirmPassword,
            onValueChange = updateConfirmPassword,
            label = stringResource(id = R.string.confirm_password),
            isPasswordField = true,
            isPasswordVisible = confirmPasswordVisible,
            onVisibilityChange = { confirmPasswordVisible = it },
            showError = registerState.confirmPasswordError != null,
            errorMessage = registerState.confirmPasswordError?.let { stringResource(id = it) } ?: "",
            leadingIconImageVector = Icons.Filled.Password,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier
                .fillMaxWidth(0.8f)
        )
        CustomOutlinedTextField(
            value = registerState.phone,
            onValueChange = updatePhone,
            label = stringResource(id = R.string.phone),
            showError = registerState.phoneError != null,
            errorMessage = registerState.phoneError?.let { stringResource(id = it) } ?: "",
            leadingIconImageVector = Icons.Filled.Phone,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier
                .fillMaxWidth(0.8f)
        )
        CustomOutlinedTextField(
            value = registerState.ssn,
            onValueChange = updateLBO,
            label = stringResource(id = R.string.social_security_number),
            showError = registerState.ssnError != null,
            errorMessage = registerState.ssnError?.let { stringResource(id = it) } ?: "",
            leadingIconImageVector = Icons.Filled.MedicalServices,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            modifier = Modifier
                .fillMaxWidth(0.8f)
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onRegisterButtonClick,
                shape = RoundedShape20,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.selectable
                ),
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(LOGIN_BUTTON_HEIGHT)
            ) {
                Text(
                    text = stringResource(id = R.string.register_button_text),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BackgroundPrimaryLight,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}