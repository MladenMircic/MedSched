package rs.ac.bg.etf.diplomski.medsched.presentation.patient.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.ChangeEmail
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.ChangeInfo
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.ChangePassword
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.EditProfileViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.events.EditProfileEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.*
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.CircleDotLoader
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.CustomOutlinedTextField

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditProfileScreen(
    editProfileViewModel: EditProfileViewModel = hiltViewModel(),
    editType: String
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val editProfileState by editProfileViewModel.editProfileState.collectAsState()
    var revealContent by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(true) {
        delay(500L)
        revealContent = true
    }

    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = editProfileState.updateMessageId) {
        editProfileState.updateMessageId?.let { messageId ->
            scaffoldState.snackbarHostState.showSnackbar(
                context.getString(messageId)
            )
            editProfileViewModel.onEvent(
                EditProfileEvent.ResetUpdateEmailMessage
            )
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState },
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Box {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(it)) {
                AnimatedVisibility(
                    visible = revealContent,
                    enter = slideInVertically(
                        initialOffsetY = { offset -> offset / 4 },
                        animationSpec = tween(durationMillis = 300)
                    ) + fadeIn(
                        animationSpec = tween(durationMillis = 300)
                    )
                ) {
                    when (editType) {
                        ChangeEmail.route -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = stringResource(id = R.string.change_email),
                                    fontFamily = Quicksand,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 28.sp,
                                    color = MaterialTheme.colors.textOnPrimary
                                )
                                CustomOutlinedTextField(
                                    label = stringResource(id = R.string.new_email),
                                    showError = editProfileState.newEmailErrorId != null,
                                    errorMessage = editProfileState.newEmailErrorId?.let { errorId ->
                                        stringResource(id = errorId)
                                    } ?: "",
                                    value = editProfileState.newEmail,
                                    onValueChange = { value ->
                                        editProfileViewModel.onEvent(
                                            EditProfileEvent.ChangeEmailText(value)
                                        )
                                    },
                                    leadingIconImageVector = Icons.Default.Email,
                                    modifier = Modifier.fillMaxWidth(0.8f)
                                )
                                Button(
                                    onClick = {
                                        editProfileViewModel.onEvent(EditProfileEvent.ConfirmNewEmail)
                                        keyboardController?.hide()
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MaterialTheme.colors.selectable
                                    ),
                                    enabled = !editProfileState.isInProgress,
                                    contentPadding = PaddingValues(0.dp),
                                    shape = RoundedShape20,
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .fillMaxHeight(0.1f)
                                ) {
                                    if (editProfileState.isInProgress) {
                                        CircleDotLoader(
                                            color = BackgroundPrimaryLight,
                                            modifier = Modifier.size(150.dp)
                                        )
                                    }
                                    else {
                                        Text(
                                            text = stringResource(id = R.string.confirm),
                                            fontFamily = Quicksand,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            color = BackgroundPrimaryLight
                                        )
                                    }
                                }
                            }
                        }
                        ChangePassword.route -> {
                            var oldPasswordVisible by rememberSaveable { mutableStateOf(false) }
                            var newPasswordVisible by rememberSaveable { mutableStateOf(false) }
                            var confirmNewPasswordVisible by rememberSaveable { mutableStateOf(false) }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = stringResource(id = R.string.change_password),
                                    fontFamily = Quicksand,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 28.sp,
                                    color = MaterialTheme.colors.textOnPrimary
                                )
                                CustomOutlinedTextField(
                                    label = stringResource(id = R.string.old_password),
                                    isPasswordField = true,
                                    isPasswordVisible = oldPasswordVisible,
                                    showError = editProfileState.oldPasswordErrorId != null,
                                    errorMessage = editProfileState.oldPasswordErrorId?.let { errorId ->
                                        stringResource(id = errorId)
                                    } ?: "",
                                    onVisibilityChange = {
                                        oldPasswordVisible = !oldPasswordVisible
                                    },
                                    value = editProfileState.oldPassword,
                                    onValueChange = { value ->
                                        editProfileViewModel.onEvent(
                                            EditProfileEvent.ChangeOldPasswordText(value)
                                        )
                                    },
                                    leadingIconImageVector = Icons.Default.Password,
                                    modifier = Modifier.fillMaxWidth(0.8f)
                                )
                                CustomOutlinedTextField(
                                    label = stringResource(id = R.string.new_password),
                                    isPasswordField = true,
                                    isPasswordVisible = newPasswordVisible,
                                    onVisibilityChange = {
                                        newPasswordVisible = !newPasswordVisible
                                    },
                                    showError = editProfileState.newPasswordErrorId != null,
                                    errorMessage = editProfileState.newPasswordErrorId?.let { errorId ->
                                        stringResource(id = errorId)
                                    } ?: "",
                                    value = editProfileState.newPassword,
                                    onValueChange = { value ->
                                        editProfileViewModel.onEvent(
                                            EditProfileEvent.ChangeNewPasswordText(value)
                                        )
                                    },
                                    leadingIconImageVector = Icons.Default.Password,
                                    modifier = Modifier.fillMaxWidth(0.8f)
                                )
                                CustomOutlinedTextField(
                                    label = stringResource(id = R.string.confirm_new_password),
                                    isPasswordField = true,
                                    isPasswordVisible = confirmNewPasswordVisible,
                                    onVisibilityChange = {
                                        confirmNewPasswordVisible = !confirmNewPasswordVisible
                                    },
                                    showError = editProfileState.confirmNewPasswordErrorId != null,
                                    errorMessage = editProfileState.confirmNewPasswordErrorId
                                        ?.let { errorId -> stringResource(id = errorId) } ?: "",
                                    value = editProfileState.confirmNewPassword,
                                    onValueChange = {
                                        editProfileViewModel.onEvent(
                                            EditProfileEvent.ChangeConfirmNewPasswordText(it)
                                        )
                                    },
                                    leadingIconImageVector = Icons.Default.Password,
                                    modifier = Modifier.fillMaxWidth(0.8f)
                                )
                                Button(
                                    onClick = {
                                        editProfileViewModel.onEvent(EditProfileEvent.ConfirmNewPassword)
                                        keyboardController?.hide()
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MaterialTheme.colors.selectable
                                    ),
                                    enabled = !editProfileState.isInProgress,
                                    contentPadding = PaddingValues(0.dp),
                                    shape = RoundedShape20,
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .fillMaxHeight(0.1f)
                                ) {
                                    if (editProfileState.isInProgress) {
                                        CircleDotLoader(
                                            color = BackgroundPrimaryLight,
                                            modifier = Modifier.size(150.dp)
                                        )
                                    }
                                    else {
                                        Text(
                                            text = stringResource(id = R.string.confirm),
                                            fontFamily = Quicksand,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            color = BackgroundPrimaryLight
                                        )
                                    }
                                }
                            }
                        }
                        ChangeInfo.route -> {
                            Text(text = "RADI3", color = Color.White)
                        }
                    }
                }
            }
            SnackbarHost(
                hostState = scaffoldState.snackbarHostState,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(Alignment.Bottom)
                    .align(Alignment.BottomCenter)
            ) { data ->
                Snackbar(
                    shape = RoundedCornerShape(10.dp),
                    backgroundColor = MaterialTheme.colors.secondary,
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = data.message,
                        fontFamily = Quicksand,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.textOnSecondary
                    )
                }
            }
        }
    }
}