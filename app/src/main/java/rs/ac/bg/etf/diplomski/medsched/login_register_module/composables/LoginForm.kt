package rs.ac.bg.etf.diplomski.medsched.login_register_module.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.ui.theme.Blue40
import rs.ac.bg.etf.diplomski.medsched.ui.theme.Blue85
import rs.ac.bg.etf.diplomski.medsched.ui.theme.Blue95
import rs.ac.bg.etf.diplomski.medsched.ui.theme.RoundedShape20
import rs.ac.bg.etf.diplomski.medsched.utils.DEFAULT_FORM_PADDING
import rs.ac.bg.etf.diplomski.medsched.utils.LOGIN_BUTTON_HEIGHT
import rs.ac.bg.etf.diplomski.medsched.utils.LOGIN_BUTTON_PADDING

@Composable
fun LoginForm(
    email: String,
    password: String,
    updateEmail: (String) -> Unit,
    updatePassword: (String) -> Unit
) {

    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            label = { Text(text = stringResource(id = R.string.email)) },
            value = email,
            onValueChange = updateEmail,
            colors = TextFieldDefaults.textFieldColors(
                focusedLabelColor = Blue40,
                focusedIndicatorColor = Blue40,
                backgroundColor = Blue85,
                textColor = Blue40,
            ),
            shape = RoundedShape20,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = ""
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DEFAULT_FORM_PADDING)
        )
        OutlinedTextField(
            label = { Text(text = stringResource(id = R.string.password)) },
            value = password,
            onValueChange = updatePassword,
            colors = TextFieldDefaults.textFieldColors(
                focusedLabelColor = Blue40,
                focusedIndicatorColor = Blue40,
                backgroundColor = Blue85,
                textColor = Blue40,
            ),
            shape = RoundedShape20,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation =
                if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = ""
                )
            },
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                // Please provide localized description for accessibility services
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }){
                    Icon(imageVector = image, description)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DEFAULT_FORM_PADDING),
        )
        Button(
            onClick = { },
            shape = RoundedShape20,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Blue40
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
                color = Blue95,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}