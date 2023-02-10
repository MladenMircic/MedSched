package rs.ac.bg.etf.diplomski.medsched.presentation.patient.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import kotlinx.coroutines.delay
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.CustomDateFormatter
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentForPatient
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.stateholders.PatientScheduledViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.*
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.CircleDotLoader
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.PulseRefreshLoading
import kotlin.math.min
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun ScheduledAppointmentsScreen(
    patientScheduledViewModel: PatientScheduledViewModel = hiltViewModel()
) {
    val scheduledState by patientScheduledViewModel.scheduledState.collectAsState()
    val appointmentWithDoctorList by patientScheduledViewModel.appointmentsWithDoctorFlow
        .collectAsState(initial = listOf())

    val lazyListState = rememberLazyListState()
    val fullyVisibleIndices: List<Int> by remember {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (visibleItemsInfo.isEmpty()) {
                emptyList()
            } else {
                val fullyVisibleItemsInfo = visibleItemsInfo.toMutableList()

                val lastItem = fullyVisibleItemsInfo.last()

                val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset

                if (lastItem.offset + lastItem.size / 2 > viewportHeight) {
                    fullyVisibleItemsInfo.removeLast()
                }

                val firstItemIfLeft = fullyVisibleItemsInfo.firstOrNull()
                if (firstItemIfLeft != null &&
                    firstItemIfLeft.offset < layoutInfo.viewportStartOffset
                ) {
                    fullyVisibleItemsInfo.removeFirst()
                }

                fullyVisibleItemsInfo.map { it.index }
            }
        }
    }

    val density = LocalDensity.current
    val trigger = remember { 80.dp }
    val triggerPx = remember { with(density) { trigger.toPx() } }
    val pullState = rememberPullRefreshState(
        refreshing = scheduledState.isRefreshing,
        onRefresh = patientScheduledViewModel::refreshAppointments,
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

    LaunchedEffect(key1 = willRefresh) {
        if (willRefresh) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }
    LaunchedEffect(key1 = fullyVisibleIndices) {
        if (!scheduledState.isRefreshing) {
            fullyVisibleIndices.forEach { index ->
                patientScheduledViewModel.triggerRevealForIndex(index)
                delay(100L)
            }
        }
    }
    LaunchedEffect(key1 = scheduledState.revealNew) {
        if (scheduledState.revealNew) {
            fullyVisibleIndices.forEach { index ->
                patientScheduledViewModel.triggerRevealForIndex(index)
                delay(100L)
            }
            patientScheduledViewModel.setRevealNew(false)
        }
    }

    var confirmDialogOpen by rememberSaveable { mutableStateOf(false) }
    var appointmentToDelete by remember { mutableStateOf<AppointmentForPatient?>(null) }

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
                Box(
                    modifier = Modifier
                        .offset { IntOffset(x = 0, y = animatedOffset) }
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .fillMaxSize()
                        .background(MaterialTheme.colors.primary)
                ) {
                    Column(
                        modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 25.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.scheduled_appointments),
                            fontFamily = Quicksand,
                            fontWeight = FontWeight.Bold,
                            fontSize = 36.sp,
                            color = MaterialTheme.colors.textOnPrimary
                        )
                        Spacer(modifier = Modifier.padding(top = 10.dp))
                        LazyColumn(
                            state = lazyListState,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            contentPadding = PaddingValues(vertical = 20.dp),
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            itemsIndexed(
                                appointmentWithDoctorList,
                                key = { index, _ -> index }
                            ) { index, appointmentForPatient ->
                                AnimatedVisibility(
                                    visible = !scheduledState.deletedList.contains(appointmentForPatient),
                                    exit = shrinkVertically(
                                        animationSpec = tween(
                                            durationMillis = 300,
                                            easing = EaseOutCubic
                                        ),
                                        shrinkTowards = Alignment.Top
                                    )
                                ) {
                                    patientScheduledViewModel
                                        .fetchDoctorImageForAppointment(appointmentForPatient)

                                    AppointmentForPatientCard(
                                        appointmentForPatient = appointmentForPatient,
                                        waitForDelete = scheduledState.appointmentToDelete ==
                                                appointmentForPatient,
                                        toDelete = scheduledState.lastAppointmentDeleted ==
                                                appointmentForPatient,
                                        revealed = if (scheduledState.alreadyRevealed.size > index)
                                            scheduledState.alreadyRevealed[index]
                                        else false,
                                        toggleRevealItem = {
                                            patientScheduledViewModel.toggleRevealed(index)
                                        },
                                        onCancelAppointment = {
                                            appointmentToDelete = appointmentForPatient
                                            confirmDialogOpen = true
                                        },
                                        onDeleteAppointment = {
                                            patientScheduledViewModel
                                                .markAppointmentDeleted(appointmentForPatient)
                                        },
                                        getDoctorSpecializationId =
                                            patientScheduledViewModel::specializationIdToNameId,
                                        getServiceId =
                                            patientScheduledViewModel::serviceIdToNameId,
                                        modifier = Modifier
                                            .padding(top = 8.dp)
                                            .animateItemPlacement(
                                                animationSpec = tween(
                                                    durationMillis = 500
                                                )
                                            )
                                    )
                                }
                            }
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
                        patientScheduledViewModel.cancelAppointment(
                            appointmentToDelete = appointmentToDelete!!
                        )
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
    toDelete: Boolean,
    revealed: Boolean,
    toggleRevealItem: () -> Unit,
    onCancelAppointment: () -> Unit,
    onDeleteAppointment: () -> Unit,
    getDoctorSpecializationId: (Int) -> Int,
    getServiceId: (Int) -> Int
) {
    LaunchedEffect(toDelete) {
        if (toDelete) {
            toggleRevealItem()
            delay(400L)
            onDeleteAppointment()
        }
    }
    Box(modifier = modifier) {
        AnimatedVisibility(
            visible = revealed,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 500
                )
            ) + slideInHorizontally(
                animationSpec = tween(
                    durationMillis = 500,
                    easing = EaseOut
                ),
                initialOffsetX = { -it / 2 }
            ),
            exit = fadeOut(
                animationSpec = tween(
                    durationMillis = 500
                )
            ) + slideOutHorizontally(
                animationSpec = tween(
                    durationMillis = 500,
                    easing = EaseOut
                ),
                targetOffsetX = { it / 2 }
            )
        ) {
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
                                    text = CustomDateFormatter.dateAsString(
                                        date.dayOfMonth,
                                        date.month.name,
                                        date.year
                                    ),
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
                                    text = CustomDateFormatter.timeAsString(
                                        time.hour,
                                        time.minute
                                    ),
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

}