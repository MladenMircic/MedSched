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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
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
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Scheduled
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.PatientScheduledViewModel
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
        targetValue = if (scheduledState.isRefreshing || pullState.progress * triggerPx >= triggerPx)
            triggerPx.roundToInt()
        else (pullState.progress * triggerPx).roundToInt(),
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )
    val willRefresh by remember {
        derivedStateOf {
            pullState.progress * triggerPx >= triggerPx
        }
    }
    val hapticFeedback = LocalHapticFeedback.current
    LaunchedEffect(key1 = willRefresh) {
        if (willRefresh) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

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
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(vertical = 20.dp),
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            itemsIndexed(scheduledState.scheduledList) { index, scheduled ->
                                ScheduledAppointmentCard(
                                    scheduledAppointment = scheduled,
                                    isVisible = fullyVisibleIndices.contains(index),
                                    waitForDelete = scheduledState.deletingIndex == index,
                                    toDelete = scheduledState.lastDeleted == index,
                                    revealed = scheduledState.alreadyRevealed[index],
                                    toggleRevealItem = {
                                        patientScheduledViewModel.toggleRevealed(index)
                                    },
                                    onCancelAppointment = {
                                        patientScheduledViewModel.cancelAppointment(
                                            deleteIndex = index,
                                            appointmentId = scheduled.appointment.id
                                        )
                                    },
                                    onDeleteAppointment = {
                                        patientScheduledViewModel.deleteCancelledAppointment()
                                    },
                                    modifier = Modifier.animateItemPlacement(
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

@Composable
fun ScheduledAppointmentCard(
    modifier: Modifier = Modifier,
    scheduledAppointment: Scheduled,
    isVisible: Boolean,
    waitForDelete: Boolean,
    toDelete: Boolean,
    revealed: Boolean,
    toggleRevealItem: () -> Unit,
    onCancelAppointment: () -> Unit,
    onDeleteAppointment: () -> Unit
) {
    LaunchedEffect(isVisible) {
        if (isVisible && !revealed) {
            toggleRevealItem()
        }
    }
    LaunchedEffect(toDelete) {
        if (toDelete) {
            toggleRevealItem()
            delay(500L)
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
                targetOffsetX = { it }
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                backgroundColor = Blue85,
                shape = RoundedShape20
            ) {
                Column {
                    Row(modifier = Modifier.padding(16.dp)) {
                        Column {
                            Text(
                                text = scheduledAppointment.doctorName,
                                fontFamily = Quicksand,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                            Text(
                                text = scheduledAppointment.doctorSpecialization,
                                fontFamily = Quicksand,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        SubcomposeAsyncImage(
                            model = scheduledAppointment.doctorImageRequest,
                            contentDescription = "Doctor image",
                            loading = {
                                CircularProgressIndicator(color = Color.Black)
                            },
                            modifier = Modifier
                                .size(60.dp)
                        )
                    }
                    Divider(
                        color = Color.Black.copy(alpha = 0.3f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Row(Modifier.padding(start = 16.dp, end = 16.dp, top = 10.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CalendarToday,
                                contentDescription = "Calendar icon"
                            )
                            Spacer(modifier = Modifier.padding(start = 4.dp))
                            Text(
                                text = scheduledAppointment.dateAsString(),
                                fontFamily = Quicksand,
                                fontSize = 16.sp
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Schedule,
                                contentDescription = "Time icon"
                            )
                            Spacer(modifier = Modifier.padding(start = 4.dp))
                            Text(
                                text = scheduledAppointment.timeAsString(),
                                fontFamily = Quicksand,
                                fontSize = 16.sp
                            )
                        }
                    }
                    Button(
                        onClick = onCancelAppointment,
                        enabled = !waitForDelete,
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .fillMaxHeight()
                            .padding(16.dp)
                            .align(Alignment.End),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.selectable
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.cancel_appointment_button),
                            fontFamily = Quicksand,
                            fontSize = 14.sp,
                            color = BackgroundPrimaryLight
                        )
                    }
                }
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedShape20)
                .background(Color.Black.copy(alpha = if (waitForDelete) 0.3f else 0f))
        ) {
            if (waitForDelete) {
                CircleDotLoader(color = Color.Black)
            }
        }
    }

}