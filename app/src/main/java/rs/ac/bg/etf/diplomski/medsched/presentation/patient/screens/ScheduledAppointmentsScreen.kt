package rs.ac.bg.etf.diplomski.medsched.presentation.patient.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.CustomDateFormatter
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentForPatient
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.events.ScheduledAppointmentsEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.stateholders.PatientScheduledViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.*
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.CircleDotLoader
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.PulseRefreshLoading
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.animatedItems
import kotlin.math.min
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun ScheduledAppointmentsScreen(
    patientScheduledViewModel: PatientScheduledViewModel = hiltViewModel()
) {
    var canScroll by rememberSaveable { mutableStateOf(true) }
    val scheduledState by patientScheduledViewModel.scheduledState.collectAsState()

    val density = LocalDensity.current
    val trigger = remember { 100.dp }
    val triggerPx = remember { with(density) { trigger.toPx() } }
    val pullState = rememberPullRefreshState(
        refreshing = scheduledState.isRefreshing,
        onRefresh = {
            patientScheduledViewModel.onEvent(
                ScheduledAppointmentsEvent.RefreshAppointments
            )
        },
        refreshThreshold = trigger
    )
    val animatedOffset by animateIntAsState(
        targetValue = if (scheduledState.isRefreshing || pullState.progress >= 1f)
            triggerPx.roundToInt()
        else (pullState.progress * triggerPx).roundToInt(),
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )
    val willRefresh by remember {
        derivedStateOf {
            pullState.progress >= 1f
        }
    }
    val hapticFeedback = LocalHapticFeedback.current

    LaunchedEffect(key1 = scheduledState.isRefreshing) {
        canScroll = !scheduledState.isRefreshing
    }
    LaunchedEffect(key1 = willRefresh) {
        if (willRefresh) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    var confirmDialogOpen by rememberSaveable { mutableStateOf(false) }

    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        Box(modifier = Modifier.pullRefresh(pullState)) {
            Box(
                modifier = Modifier.background(color = MaterialTheme.colors.secondary)
            ) {
                PulseRefreshLoading(
                    progress = min(pullState.progress * 0.45f, 0.5f),
                    automatic = scheduledState.isRefreshing,
                    color = MaterialTheme.colors.textOnSecondary,
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.TopCenter)
                        .offset(y = (-60).dp)
                )
                Text(
                    text = if (!scheduledState.isRefreshing)
                        stringResource(id = R.string.release_to_refresh)
                    else stringResource(id = R.string.refreshing),
                    fontFamily = Quicksand,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.textOnSecondary,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 70.dp)
                )
                Box(
                    modifier = Modifier
                        .offset { IntOffset(x = 0, y = animatedOffset) }
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .fillMaxSize()
                        .background(MaterialTheme.colors.primary)
                ) {
                    AnimatedVisibility(
                        visible = scheduledState.isListEmpty,
                        enter = slideInHorizontally(
                            animationSpec = tween(500),
                            initialOffsetX = { -it }
                        ),
                        exit = slideOutHorizontally(
                            animationSpec = tween(500),
                            targetOffsetX = { it }
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = stringResource(
                                    id = R.string.no_available_appointments
                                ),
                                fontFamily = Quicksand,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 30.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.textOnPrimary
                            )
                        }
                    }
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 25.dp),
                        userScrollEnabled = canScroll,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        item(
                            key = 0
                        ) {
                            Text(
                                text = stringResource(id = R.string.scheduled_appointments),
                                fontFamily = Quicksand,
                                fontWeight = FontWeight.Bold,
                                fontSize = 36.sp,
                                color = MaterialTheme.colors.textOnPrimary,
                                modifier = Modifier.animateItemPlacement(
                                    animationSpec = tween(500)
                                )
                            )
                        }
                        animatedItems(
                            items = scheduledState.animatedAppointmentForPatientList,
                            key = { appointmentForPatient ->
                                appointmentForPatient.appointment.id
                            },
                            enter = fadeIn(tween(500)) +
                                    slideInHorizontally(
                                        animationSpec = tween(500),
                                        initialOffsetX = { -it / 2 }
                                    ),
                            exit = fadeOut(tween(500)) +
                                    slideOutHorizontally(
                                        animationSpec = tween(500),
                                        targetOffsetX = { it / 2 }
                                    ),
                            exitDuration = 500,
                            animateItemPlacementSpec = tween(500)
                        ) { appointmentForPatient ->
                            patientScheduledViewModel.onEvent(
                                ScheduledAppointmentsEvent.DoctorImageFetch(
                                    appointmentForPatient
                                )
                            )

                            AppointmentForPatientCard(
                                appointmentForPatient = appointmentForPatient,
                                waitForDelete = false,
                                onCancelAppointment = {
                                    patientScheduledViewModel.onEvent(
                                        ScheduledAppointmentsEvent.SetAppointmentToDelete(
                                            appointmentForPatient
                                        )
                                    )
                                    confirmDialogOpen = true
                                },
                                getDoctorSpecializationId =
                                patientScheduledViewModel::specializationIdToNameId,
                                getServiceId =
                                patientScheduledViewModel::serviceIdToNameId,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    if (confirmDialogOpen) {
        AlertDialog(
            onDismissRequest = {
                confirmDialogOpen = false
            },
            title = {
                Text(
                    text = stringResource(id = R.string.confirm_dialog_title),
                    fontFamily = Quicksand,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.textOnPrimary
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.confirm_dialog_text),
                    fontFamily = Quicksand,
                    color = MaterialTheme.colors.textOnPrimary
                )
            },
            dismissButton = {
                TextButton(onClick = { confirmDialogOpen = false }) {
                    Text(
                        text = stringResource(id = R.string.no),
                        fontFamily = Quicksand,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colors.selectable
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmDialogOpen = false
                        patientScheduledViewModel.onEvent(
                            ScheduledAppointmentsEvent.CancelAppointment
                        )
//                        patientScheduledViewModel.cancelAppointment(
//                            appointmentToDelete = appointmentToDelete!!
//                        )
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.yes),
                        fontFamily = Quicksand,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colors.selectable
                    )
                }
            },
            shape = RoundedCornerShape(10.dp),
            backgroundColor = MaterialTheme.colors.primary
        )
    }
}

@Composable
fun AppointmentForPatientCard(
    modifier: Modifier = Modifier,
    appointmentForPatient: AppointmentForPatient,
    waitForDelete: Boolean,
    onCancelAppointment: () -> Unit,
    getDoctorSpecializationId: (Int) -> Int,
    getServiceId: (Int) -> Int
) {
    Box(modifier = modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            backgroundColor = Blue85,
            shape = RoundedShape20
        ) {
            Column {
                Row(modifier = Modifier.padding(16.dp)) {
                    Column {
                        Text(
                            text = appointmentForPatient.doctorName,
                            fontFamily = Quicksand,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                        Text(
                            text = stringResource(
                                id = getDoctorSpecializationId(
                                    appointmentForPatient.doctorSpecializationId
                                )
                            ),
                            fontFamily = Quicksand,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    SubcomposeAsyncImage(
                        model = appointmentForPatient.doctorImageRequest,
                        contentDescription = "Doctor image",
                        loading = {
                            CircularProgressIndicator(color = Color.Black)
                        },
                        modifier = Modifier
                            .size(60.dp)
                    )
                }
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = stringResource(
                            id = getServiceId(appointmentForPatient.appointment.examId)
                        ),
                        fontFamily = Quicksand,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .weight(1f)
                    )
                }
                Divider(
                    color = Color.Black.copy(alpha = 0.3f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 10.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CalendarMonth,
                                contentDescription = "Calendar icon",
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.padding(start = 4.dp))

                            val date = appointmentForPatient.appointment.date
                            Text(
                                text = CustomDateFormatter.dateAsString(date),
                                fontFamily = Quicksand,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(top = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Schedule,
                                contentDescription = "Time icon",
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.padding(start = 4.dp))

                            val time = appointmentForPatient.appointment.time
                            Text(
                                text = CustomDateFormatter.timeAsString(time),
                                fontFamily = Quicksand,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(top = 6.dp)
                        ) {
                            Icon(
                                imageVector = if (appointmentForPatient.appointment.confirmed)
                                    Icons.Filled.EventAvailable
                                else Icons.Filled.EventBusy,
                                contentDescription = "Availability icon",
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.padding(start = 4.dp))
                            Text(
                                text = if (appointmentForPatient.appointment.confirmed)
                                    stringResource(id = R.string.confirmed_appointment)
                                else stringResource(id = R.string.cancelled_appointment),
                                fontFamily = Quicksand,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        }
                    }
                    Button(
                        onClick = onCancelAppointment,
                        enabled = !waitForDelete,
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .fillMaxHeight(0.5f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.selectable
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = if (appointmentForPatient.appointment.confirmed)
                                stringResource(id = R.string.cancel_appointment_button)
                            else stringResource(id = R.string.dismiss_appointment_button),
                            fontFamily = Quicksand,
                            fontSize = 14.sp,
                            color = BackgroundPrimaryLight
                        )
                    }
                }
            }
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedShape20)
            .background(Color.Black.copy(alpha = if (waitForDelete) 0.3f else 0f))
    ) {
        if (waitForDelete) {
            CircleDotLoader(color = Color.Black)
        }
    }
}

