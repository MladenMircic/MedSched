package rs.ac.bg.etf.diplomski.medsched.presentation.clinic.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.datetime.LocalTime
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.BOOK_APPOINTMENT_BUTTON_HEIGHT
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.ClinicIdToNameMapUseCase
import rs.ac.bg.etf.diplomski.medsched.presentation.clinic.events.ClinicAddDoctorEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.clinic.stateholders.ClinicAddDoctorViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.composables.defaultButtonColors
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.*
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.CustomOutlinedTextField
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ClinicAddDoctorScreen(
    clinicAddDoctorViewModel: ClinicAddDoctorViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val addDoctorState by clinicAddDoctorViewModel.addDoctorState.collectAsState()

    LaunchedEffect(key1 = addDoctorState.messageId) {
        addDoctorState.messageId?.let {
            Toast.makeText(
                context,
                context.getString(it),
                Toast.LENGTH_LONG
            ).show()
            addDoctorState.messageId = null
        }
    }

    val scrollState = rememberScrollState()
    LaunchedEffect(key1 = addDoctorState.hasError) {
        if (addDoctorState.hasError) {
            scrollState.animateScrollTo(
                value = 0,
                animationSpec = tween(
                    durationMillis = 500
                )
            )
            clinicAddDoctorViewModel.onEvent(ClinicAddDoctorEvent.SetHasError(false))
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Text(
                text = stringResource(id = R.string.clinic_doctor_register),
                fontFamily = Quicksand,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                color = MaterialTheme.colors.textOnPrimary,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            CustomOutlinedTextField(
                label = stringResource(id = R.string.email),
                value = addDoctorState.email,
                onValueChange = {
                    clinicAddDoctorViewModel.onEvent(ClinicAddDoctorEvent.SetEmailField(it))
                },
                showError = addDoctorState.emailError != null,
                errorMessage = addDoctorState.emailError?.let { stringResource(id = it) } ?: "",
                leadingIconImageVector = Icons.Default.Email,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            CustomOutlinedTextField(
                label = stringResource(id = R.string.first_name),
                value = addDoctorState.firstName,
                onValueChange = {
                    clinicAddDoctorViewModel.onEvent(ClinicAddDoctorEvent.SetFirstNameField(it))
                },
                showError = addDoctorState.firstNameError != null,
                errorMessage = addDoctorState.firstNameError?.let { stringResource(id = it) } ?: "",
                leadingIconImageVector = Icons.Default.Person,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            CustomOutlinedTextField(
                label = stringResource(id = R.string.last_name),
                value = addDoctorState.lastName,
                onValueChange = {
                    clinicAddDoctorViewModel.onEvent(ClinicAddDoctorEvent.SetLastNameField(it))
                },
                showError = addDoctorState.lastNameError != null,
                errorMessage = addDoctorState.lastNameError?.let { stringResource(id = it) } ?: "",
                leadingIconImageVector = Icons.Default.Person,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            var passwordVisible by rememberSaveable { mutableStateOf(false) }
            CustomOutlinedTextField(
                label = stringResource(id = R.string.password),
                value = addDoctorState.password,
                onValueChange = {
                    clinicAddDoctorViewModel.onEvent(ClinicAddDoctorEvent.SetPasswordField(it))
                },
                showError = addDoctorState.passwordError != null,
                errorMessage = addDoctorState.passwordError?.let { stringResource(id = it) } ?: "",
                leadingIconImageVector = Icons.Default.Password,
                isPasswordField = true,
                isPasswordVisible = passwordVisible,
                onVisibilityChange = { passwordVisible = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
            CustomOutlinedTextField(
                label = stringResource(id = R.string.confirm_password),
                value = addDoctorState.confirmPassword,
                onValueChange = {
                    clinicAddDoctorViewModel.onEvent(ClinicAddDoctorEvent.SetConfirmPasswordField(it))
                },
                showError = addDoctorState.confirmPasswordError != null,
                errorMessage = addDoctorState.confirmPasswordError?.let { stringResource(id = it) } ?: "",
                leadingIconImageVector = Icons.Default.Password,
                isPasswordField = true,
                isPasswordVisible = confirmPasswordVisible,
                onVisibilityChange = { confirmPasswordVisible = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            CustomOutlinedTextField(
                label = stringResource(id = R.string.phone),
                value = addDoctorState.phone,
                onValueChange = {
                    clinicAddDoctorViewModel.onEvent(ClinicAddDoctorEvent.SetPhoneField(it))
                },
                showError = addDoctorState.phoneError != null,
                errorMessage = addDoctorState.phoneError?.let { stringResource(id = it) } ?: "",
                leadingIconImageVector = Icons.Default.Phone,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            var categoryDropdownExpanded by rememberSaveable { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = categoryDropdownExpanded,
                onExpandedChange = {
                    categoryDropdownExpanded = !categoryDropdownExpanded
                }
            ) {
                val selectedCategoryNameId = clinicAddDoctorViewModel.getSelectedCategoryNameId()
                OutlinedTextField(
                    label = { Text(text = stringResource(id = R.string.category)) },
                    readOnly = true,
                    value = if (addDoctorState.selectedCategory != null)
                        stringResource(
                            id = selectedCategoryNameId!!
                        )
                    else "",
                    onValueChange = {},
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = "Categories icon"
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = categoryDropdownExpanded
                        )
                    },
                    shape = RoundedShape20,
                    colors = defaultButtonColors(),
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                ExposedDropdownMenu(
                    expanded = categoryDropdownExpanded,
                    onDismissRequest = { categoryDropdownExpanded = false },
                    modifier = Modifier
                        .background(color = MaterialTheme.colors.secondary)
                ) {
                    addDoctorState.categoryList.forEachIndexed { index, category ->
                        DropdownMenuItem(
                            onClick = {
                                categoryDropdownExpanded = false
                                clinicAddDoctorViewModel.onEvent(
                                    ClinicAddDoctorEvent.SetCategoryField(index)
                                )
                            }
                        ) {
                            val categoryNameId = ClinicIdToNameMapUseCase.categoryIdToNameId(
                                category.id
                            )
                            Text(
                                text = stringResource(id = categoryNameId!!),
                                fontFamily = Quicksand,
                                fontSize = 18.sp,
                                color = MaterialTheme.colors.textOnSecondary
                            )
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp)
        ) {
            Text(
                text = stringResource(id = R.string.work_days),
                fontFamily = Quicksand,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                color = MaterialTheme.colors.textOnPrimary,
                modifier = Modifier.padding(start = 26.dp, top = 12.dp)
            )
            val daysOfWeek by remember {
                derivedStateOf {
                    addDoctorState.workDaysList.map { workDay ->
                        workDay.dayOfWeek.name.lowercase().substring(0, 3)
                            .replaceFirstChar { firstChar ->
                                firstChar.titlecase(Locale.getDefault())
                            }
                    }
                }
            }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
            ) {
                itemsIndexed(items = daysOfWeek) { index, dayOfWeek ->
                    val interactionSource = remember { MutableInteractionSource() }
                    Card(
                        backgroundColor = if (addDoctorState.selectedWorkDay == index)
                            MaterialTheme.colors.selectable
                        else MaterialTheme.colors.secondary,
                        contentColor = if (addDoctorState.selectedWorkDay == index)
                            Color.White
                        else MaterialTheme.colors.textOnSecondary,
                        shape = RoundedShape20,
                        modifier = Modifier
                            .width(100.dp)
                            .height(50.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                clinicAddDoctorViewModel.onEvent(
                                    ClinicAddDoctorEvent.SetSelectedWorkDay(index)
                                )
                            }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = dayOfWeek,
                                fontFamily = Quicksand,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }

            Text(
                text = stringResource(id = R.string.appointment_time),
                fontFamily = Quicksand,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                color = MaterialTheme.colors.textOnPrimary,
                modifier = Modifier.padding(start = 26.dp, top = 12.dp)
            )

            val currentlySelected = remember { mutableStateListOf<Boolean>() }
            val workHours: List<LocalTime> by remember {
                derivedStateOf {
                    val workHoursList = addDoctorState.selectedWorkDay?.let {
                        clinicAddDoctorViewModel.getWorkHoursForDay(it)
                    } ?: emptyList()
                    currentlySelected.clear()
                    currentlySelected.addAll(workHoursList.map { workHour ->
                        clinicAddDoctorViewModel.isHourSelected(workHour)
                    })
                    workHoursList
                }
            }

            LazyVerticalGrid(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                columns = GridCells.Fixed(3),
                modifier = Modifier.height(200.dp)
            ) {
                itemsIndexed(items = workHours) { index, workHour ->
//                    BorderAnimatedItem(
//                        isSelected = currentlySelected[index],
//                        borderColor = MaterialTheme.colors.textFieldOutline,
//                        onSelect = {
//                            currentlySelected[index] =
//                                clinicAddDoctorViewModel.toggleHourForWorkDay(workHour)
//                        }
//                    ) {
//                        Text(
//                            text = workHour.toString(),
//                            fontFamily = Quicksand,
//                            fontWeight = FontWeight.SemiBold,
//                            fontSize = 18.sp,
//                            textAlign = TextAlign.Center
//                        )
//                    }
                }
            }

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    shape = RoundedShape20,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.selectable
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(BOOK_APPOINTMENT_BUTTON_HEIGHT)
                        .align(Alignment.Center),
                    onClick = { clinicAddDoctorViewModel.onEvent(ClinicAddDoctorEvent.RegisterDoctor) }
                ) {
                    Text(
                        text = stringResource(id = R.string.register_button_text),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = BackgroundPrimaryLight,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 10.dp, bottom = 8.dp)
                    )
                }
            }
        }
    }
}