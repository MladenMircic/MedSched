package rs.ac.bg.etf.diplomski.medsched.presentation.patient.screens

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import io.github.boguszpawlowski.composecalendar.selection.SelectionState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.BOOK_APPOINTMENT_BUTTON_HEIGHT
import rs.ac.bg.etf.diplomski.medsched.commons.DOCTOR_IMAGE_SIZE
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.DoctorForPatient
import rs.ac.bg.etf.diplomski.medsched.presentation.composables.defaultButtonColors
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.events.PatientEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.stateholders.PatientHomeViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.*
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.BorderAnimatedItem
import java.time.format.TextStyle
import java.util.*
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DoctorAppointmentScreen(
    patientHomeViewModel: PatientHomeViewModel,
    doctor: DoctorForPatient,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current
    val appointmentState by patientHomeViewModel.appointmentState.collectAsState()
    var showScreen by rememberSaveable { mutableStateOf(false) }
    var examNameDropdownExpanded by rememberSaveable { mutableStateOf(false) }

    val pagerState = rememberPagerState()
    var lastPage by remember { mutableStateOf(-1) }

    val coroutineScope = rememberCoroutineScope()
    var snackBarJob by remember { mutableStateOf<Job?>(null) }
    val mutex by remember { mutableStateOf(Mutex()) }

    LaunchedEffect(true) {
        patientHomeViewModel.fetchServicesForDoctor()
        patientHomeViewModel.fetchAvailableHoursForDate()
        delay(200L)
        showScreen = true
    }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (lastPage != -1) {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            }
            lastPage = page
        }
    }

    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = appointmentState.scheduledMessageId) {
        appointmentState.scheduledMessageId?.let {
            coroutineScope.launch {
                mutex.withLock {
                    snackBarJob = coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = context.getString(it),
                            duration = SnackbarDuration.Indefinite
                        )
                    }
                    delay(1200L)
                    snackBarJob!!.cancel()
                }
            }
            patientHomeViewModel.onEvent(PatientEvent.SetScheduleMessageNull)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.secondary)
                .padding(it)
        ) {
            AnimatedVisibility(
                visible = showScreen,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = EaseOutCubic
                    )
                ) + slideInVertically(
                    initialOffsetY = { offset -> -offset / 2 },
                    animationSpec = tween(
                        durationMillis = 600,
                        easing = EaseOutCubic
                    )
                ),
                exit = fadeOut(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = EaseOutCubic
                    )
                ) + slideOutVertically(
                    targetOffsetY = { offset -> -offset / 2 },
                    animationSpec = tween(
                        durationMillis = 600,
                        easing = EaseOutCubic
                    )
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
            ) {
                Surface(color = MaterialTheme.colors.secondary) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        HorizontalPager(
                            count = 10,
                            state = pagerState,
                            contentPadding = PaddingValues(horizontal = 128.dp),
                            modifier = Modifier.padding(top = 28.dp)
                        ) { page ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .graphicsLayer {
                                        // Calculate the absolute offset for the current page from the
                                        // scroll position. We use the absolute value which allows us to mirror
                                        // any effects for both directions
                                        val pageOffset = calculateCurrentOffsetForPage(page)
                                            .absoluteValue

                                        // We animate the scaleX + scaleY, between 85% and 100%
                                        lerp(
                                            start = 0.5f,
                                            stop = 1f,
                                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                        ).also { scale ->
                                            scaleX = scale
                                            scaleY = scale
                                        }

                                        // We animate the alpha, between 50% and 100%
                                        alpha = lerp(
                                            start = 0.5f,
                                            stop = 1f,
                                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                        )
                                    }
                                    .fillMaxSize()
                            ) {
                                AsyncImage(
                                    model = doctor.imageRequest,
                                    contentDescription = "Doctor Image",
                                    modifier = Modifier.size(DOCTOR_IMAGE_SIZE)
                                )
                                var text1Size by remember { mutableStateOf(26.sp) }
                                var text2Size by remember { mutableStateOf(26.sp) }
                                var text3Size by remember { mutableStateOf(20.sp) }

                                Text(
                                    text = doctor.firstName,
                                    fontFamily = Quicksand,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = text1Size,
                                    color = MaterialTheme.colors.textOnSecondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    onTextLayout = { textLayoutResult ->
                                        val maxCurrentLineIndex: Int = textLayoutResult.lineCount - 1

                                        if (textLayoutResult.isLineEllipsized(maxCurrentLineIndex)) {
                                            text1Size = text1Size.times(0.9f)
                                        }
                                    }
                                )
                                Text(
                                    text = doctor.lastName,
                                    fontFamily = Quicksand,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = text2Size,
                                    color = MaterialTheme.colors.textOnSecondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    onTextLayout = { textLayoutResult ->
                                        val maxCurrentLineIndex: Int = textLayoutResult.lineCount - 1

                                        if (textLayoutResult.isLineEllipsized(maxCurrentLineIndex)) {
                                            text2Size = text2Size.times(0.9f)
                                        }
                                    }
                                )
                                Text(
                                    text = stringResource(
                                        id = patientHomeViewModel.specializationIdToNameId(
                                            doctor.specializationId
                                        )
                                    ),
                                    fontFamily = Quicksand,
                                    fontSize = text3Size,
                                    color = MaterialTheme.colors.textOnSecondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    onTextLayout = { textLayoutResult ->
                                        val maxCurrentLineIndex: Int = textLayoutResult.lineCount - 1

                                        if (textLayoutResult.isLineEllipsized(maxCurrentLineIndex)) {
                                            text3Size = text3Size.times(0.9f)
                                        }
                                    }
                                )
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
                                backgroundColor = MaterialTheme.colors.primary,
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = data.message,
                                    fontFamily = Quicksand,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colors.textOnPrimary
                                )
                            }
                        }
                    }
                }
            }
            AnimatedVisibility(
                visible = showScreen,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 200,
                        easing = EaseOutCubic
                    )
                ) + slideInVertically(
                    initialOffsetY = { offset -> offset },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = EaseOutCubic
                    )
                ),
                exit = fadeOut(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = EaseOutCubic
                    )
                ) + slideOutVertically(
                    targetOffsetY = { offset -> offset },
                    animationSpec = tween(
                        durationMillis = 600,
                        easing = EaseOutCubic
                    )
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colors.secondary)
                    .weight(0.6f)
            ) {
                Surface(
                    color = MaterialTheme.colors.primary,
                    shape = RoundedShape60
                        .copy(bottomStart = ZeroCornerSize, bottomEnd = ZeroCornerSize)
                ) {
                    LazyVerticalGrid(
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        columns = GridCells.Fixed(3)
                    ) {
                        item(
                            span = { GridItemSpan(3) }
                        ) {
                            Text(
                                text = stringResource(id = R.string.schedule_appointment),
                                fontFamily = Quicksand,
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                color = MaterialTheme.colors.textOnPrimary,
                                textAlign = TextAlign.Center
                            )
                        }
                        item(
                            span = { GridItemSpan(3) }
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                ExposedDropdownMenuBox(
                                    expanded = examNameDropdownExpanded,
                                    onExpandedChange = {
                                        examNameDropdownExpanded = !examNameDropdownExpanded
                                    },
                                    modifier = Modifier.fillMaxWidth(0.8f)
                                ) {
                                    OutlinedTextField(
                                        label = { Text(text = stringResource(id = R.string.exam_name)) },
                                        readOnly = true,
                                        value = if (appointmentState.currentExamNameId != null)
                                            stringResource(id = appointmentState.currentExamNameId!!)
                                        else "",
                                        onValueChange = {},
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(
                                                expanded = examNameDropdownExpanded
                                            )
                                        },
                                        shape = RoundedShape20,
                                        colors = defaultButtonColors()
                                    )
                                    ExposedDropdownMenu(
                                        expanded = examNameDropdownExpanded,
                                        onDismissRequest = { examNameDropdownExpanded = false },
                                        modifier = Modifier
                                            .background(color = MaterialTheme.colors.secondary)
                                    ) {
                                        appointmentState.examNameIdList.forEach { examNameId ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    examNameDropdownExpanded = false
                                                    patientHomeViewModel.onEvent(
                                                        PatientEvent.SetAppointmentExamNameId(examNameId)
                                                    )
                                                }
                                            ) {
                                                Text(
                                                    text = stringResource(id = examNameId),
                                                    fontFamily = Quicksand,
                                                    fontSize = 18.sp,
                                                    color = MaterialTheme.colors.textOnSecondary
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        item(
                            span = { GridItemSpan(3) }
                        ) {
                            DateTimeCalendar(
                                today = LocalDate.now(),
                                onSelectionChanged = { selection ->
                                    patientHomeViewModel.onEvent(
                                        PatientEvent.SetAppointmentDate(selection.firstOrNull())
                                    )
                                    patientHomeViewModel.fetchAvailableHoursForDate()
                                },
                                dayContent = { dayState -> DayContent(dayState = dayState) },
                            )
                        }
                        item(
                            span = { GridItemSpan(3) }
                        ) {
                            Text(
                                text = stringResource(id = R.string.appointment_time),
                                fontFamily = Quicksand,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = MaterialTheme.colors.textOnPrimary
                            )
                        }
                        itemsIndexed(appointmentState.availableTimes) { index, time ->
                            BorderAnimatedItem(
                                isSelected = appointmentState.selectedTime == index,
                                borderColor = MaterialTheme.colors.textFieldOutline,
                                onSelect = {
                                    patientHomeViewModel.onEvent(PatientEvent.SetAppointmentTime(index))
                                }
                            ) {
                                Text(
                                    text = time.toString(),
                                    fontFamily = Quicksand,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        item(
                            span = { GridItemSpan(3) }
                        ) {
                            Button(
                                shape = RoundedShape20,
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = MaterialTheme.colors.selectable
                                ),
                                enabled = appointmentState.selectedTime != -1,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(BOOK_APPOINTMENT_BUTTON_HEIGHT),
                                onClick = { patientHomeViewModel.onEvent(PatientEvent.ScheduleAppointment) }
                            ) {
                                Text(
                                    text = stringResource(id = R.string.schedule_appointment_button),
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
        }

        BackHandler(enabled = true) {
            showScreen = false
            snackBarJob?.cancel()
            onBackPressed()
            patientHomeViewModel.onEvent(PatientEvent.ClearAvailableHours)
        }
    }
}


// Calendar composable with it's utilities

@Composable
fun BoxScope.DayContent(
    dayState: KotlinDayState<DynamicSelectionState>,
) {
    val selectable = dayState.date >= LocalDate.now()
    val isSelected = dayState.selectionState.isDateSelected(dayState.date)
    var modifier = Modifier
        .fillMaxWidth()
        .padding(2.dp)
        .clip(RoundedCornerShape(10.dp))
    if (selectable) {
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                dayState.selectionState.onDateSelected(dayState.date)
            }
    }

    val cardColor: Color = if (isSelected)
        MaterialTheme.colors.selectable
    else if (selectable)
        MaterialTheme.colors.calendarField
    else
        MaterialTheme.colors.calendarField.copy(alpha = 0.4f)

    Card(
        backgroundColor = cardColor,
        modifier = modifier
    ) {
        Text(
            text = dayState.date.dayOfMonth.toString(),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .aspectRatio(1f),
            color = if (isSelected) Color.White else MaterialTheme.colors.calendarFieldText,
            textAlign = TextAlign.Center,
            fontFamily = Quicksand,
            fontWeight = if (dayState.isCurrentDay) FontWeight.Bold else null
        )
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateTimeCalendar(
    modifier: Modifier = Modifier,
    today: LocalDate,
    onSelectionChanged: (List<LocalDate>) -> Unit,
    dayContent: @Composable BoxScope.(KotlinDayState<DynamicSelectionState>) -> Unit,
) {
    SelectableCalendar(
        calendarState = rememberSelectableCalendarState(
            initialSelection = listOf(today.toJavaLocalDate()),
            confirmSelectionChange = { selection ->
                onSelectionChanged(selection.map { it.toKotlinLocalDate() }); true
            },
            initialSelectionMode = SelectionMode.Single,
        ),
        today = today.toJavaLocalDate(),
        showAdjacentMonths = false,
        dayContent = { dayState ->
            dayContent(
                KotlinDayState(
                    date = dayState.date.toKotlinLocalDate(),
                    isCurrentDay = dayState.isCurrentDay,
                    selectionState = dayState.selectionState,
                )
            )
        },
        weekHeader = { daysOfWeek ->
            Row(modifier = modifier) {
                daysOfWeek.forEach { dayOfWeek ->
                    Text(
                        textAlign = TextAlign.Center,
                        text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                        modifier = modifier
                            .weight(1f)
                            .wrapContentHeight(),
                        color = MaterialTheme.colors.textOnPrimary
                    )
                }
            }
        },
        monthHeader = { monthState ->
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = monthState.currentMonth.month
                        .getDisplayName(TextStyle.FULL, Locale.getDefault())
                        .lowercase()
                        .replaceFirstChar { it.titlecase() },
                    fontFamily = Quicksand,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = MaterialTheme.colors.textOnPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = monthState.currentMonth.year.toString(),
                    fontFamily = Quicksand,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = MaterialTheme.colors.textOnPrimary
                )
            }
        },
        modifier = modifier
    )
}

data class KotlinDayState<T : SelectionState>(
    val date: LocalDate,
    val isCurrentDay: Boolean,
    val selectionState: T,
)

private fun SelectionState.isDateSelected(date: LocalDate) = isDateSelected(date.toJavaLocalDate())
private fun SelectionState.onDateSelected(date: LocalDate) = onDateSelected(date.toJavaLocalDate())