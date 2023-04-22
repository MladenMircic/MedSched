package rs.ac.bg.etf.diplomski.medsched.presentation.patient.screens

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.BOOK_APPOINTMENT_BUTTON_HEIGHT
import rs.ac.bg.etf.diplomski.medsched.commons.CustomDateFormatter
import rs.ac.bg.etf.diplomski.medsched.commons.DOCTOR_IMAGE_SIZE
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentInfo
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.DoctorForPatient
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.ClinicIdToNameMapUseCase
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.events.DoctorScheduleEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.stateholders.DoctorScheduleViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.*
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.BorderAnimatedItem
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.FadeEnterTransition
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.FadeExitTransition
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.HorizontalDotLoader
import java.util.*
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DoctorAppointmentScreen(
    modifier: Modifier = Modifier,
    doctorScheduleViewModel: DoctorScheduleViewModel = hiltViewModel(),
    doctors: List<DoctorForPatient>,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current
    val appointmentState by doctorScheduleViewModel.appointmentState.collectAsState()

    val pagerState = rememberPagerState()
    var lastPage by remember { mutableStateOf(-1) }

    val coroutineScope = rememberCoroutineScope()
    var snackBarJob by remember { mutableStateOf<Job?>(null) }
    val mutex by remember { mutableStateOf(Mutex()) }

    val currentDoctor by remember(key1 = appointmentState.selectedDoctorIndex) {
        derivedStateOf {
            doctors[appointmentState.selectedDoctorIndex]
        }
    }
    val currentAppointmentInfo by remember(
        key1 = appointmentState.appointmentInfoList,
        key2 = appointmentState.selectedDoctorIndex
    ) {
        derivedStateOf {
            if (appointmentState.appointmentInfoList.isNotEmpty()) {
                appointmentState.appointmentInfoList[appointmentState.selectedDoctorIndex]
            } else {
                AppointmentInfo()
            }
        }
    }

    LaunchedEffect(true) {
        doctorScheduleViewModel.initializeLists(doctors.map { it.id })
    }
    LaunchedEffect(key1 = appointmentState.selectedDoctorIndex) {
        doctorScheduleViewModel.onEvent(DoctorScheduleEvent.SetServicesLoading)
    }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (lastPage != -1) {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            }
            lastPage = page
            delay(100L)
            doctorScheduleViewModel.onEvent(DoctorScheduleEvent.SetSelectedDoctorIndex(page))
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
            doctorScheduleViewModel.onEvent(DoctorScheduleEvent.SetScheduleMessageNull)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.secondary)
                .padding(it)
        ) {
            Surface(
                color = MaterialTheme.colors.secondary,
                modifier = Modifier.weight(0.4f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    HorizontalPager(
                        count = doctors.size,
                        state = pagerState,
                        contentPadding = PaddingValues(horizontal = 128.dp),
                        modifier = Modifier.padding(top = 28.dp)
                    ) { page ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .graphicsLayer {
                                    val pageOffset = calculateCurrentOffsetForPage(page)
                                        .absoluteValue

                                    lerp(
                                        start = 0.5f,
                                        stop = 1f,
                                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                    ).also { scale ->
                                        scaleX = scale
                                        scaleY = scale
                                    }

                                    alpha = lerp(
                                        start = 0.5f,
                                        stop = 1f,
                                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                    )
                                }
                                .fillMaxSize()
                        ) {
                            AsyncImage(
                                model = currentDoctor.imageRequest,
                                contentDescription = "Doctor Image",
                                modifier = Modifier.size(DOCTOR_IMAGE_SIZE)
                            )
                            var text1Size by remember { mutableStateOf(26.sp) }
                            var text2Size by remember { mutableStateOf(26.sp) }
                            var text3Size by remember { mutableStateOf(20.sp) }

                            Text(
                                text = doctors[page].firstName,
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
                                text = doctors[page].lastName,
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
                                    id = ClinicIdToNameMapUseCase.specializationIdToNameId(
                                        doctors[page].specializationId
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
            Surface(
                color = MaterialTheme.colors.primary,
                shape = RoundedShape60
                    .copy(bottomStart = ZeroCornerSize, bottomEnd = ZeroCornerSize),
                modifier = Modifier.weight(0.6f)
            ) {
                val servicesVisibleState = remember {
                    MutableTransitionState(false)
                }
                val hiddenDateState = remember {
                    MutableTransitionState(false).apply {
                        targetState = !currentAppointmentInfo.isOnSelectedMonth
                    }
                }
                var dayAndMonth by remember { mutableStateOf("") }

                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = stringResource(id = R.string.schedule_appointment),
                            fontFamily = Quicksand,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = MaterialTheme.colors.textOnPrimary,
                            textAlign = TextAlign.Center
                        )
                    }

                    item {
                        Text(
                            text = stringResource(id = R.string.appointment_services),
                            fontFamily = Quicksand,
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                            color = MaterialTheme.colors.textOnPrimary,
                            modifier = Modifier.padding(start = 6.dp, top = 6.dp, bottom = 8.dp)
                        )
                    }

                    item {
                        LaunchedEffect(key1 = appointmentState.servicesLoading) {
                            servicesVisibleState.targetState = !appointmentState.servicesLoading &&
                                    appointmentState.availableServicesList.isNotEmpty()
                        }

                        AnimatedVisibility(
                            visibleState = servicesVisibleState,
                            modifier = Modifier.fillMaxSize(),
                            enter = FadeEnterTransition,
                            exit = FadeExitTransition
                        ) {
                            FlowRow(
                                mainAxisSpacing = 8.dp,
                                mainAxisAlignment = FlowMainAxisAlignment.Center,
                                crossAxisSpacing = 10.dp,
                                crossAxisAlignment = FlowCrossAxisAlignment.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                appointmentState
                                    .availableServicesList[appointmentState.selectedServicesIndex]
                                    .forEach { service ->
                                        BorderAnimatedItem(
                                            isSelected = currentAppointmentInfo
                                                .selectedServicesList
                                                .contains(service),
                                            backgroundColor = MaterialTheme.colors.calendarField,
                                            borderColor = MaterialTheme.colors.textFieldOutline,
                                            shape = RoundedCornerShape(10.dp),
                                            onSelect = {
                                                doctorScheduleViewModel.onEvent(
                                                    DoctorScheduleEvent.ToggleSelectedService(service)
                                                )
                                            },
                                            modifier = Modifier.wrapContentSize()
                                        ) {
                                            Text(
                                                text = stringResource(
                                                    id = ClinicIdToNameMapUseCase.serviceIdToNameId(service.id)
                                                ),
                                                fontFamily = Quicksand,
                                                fontSize = 16.sp,
                                                color = MaterialTheme.colors.textOnSecondary,
                                                modifier = Modifier.padding(10.dp)
                                            )
                                        }
                                }
                            }
                        }
                        if (servicesVisibleState.isIdle && !servicesVisibleState.currentState) {
                            LaunchedEffect(true) {
                                doctorScheduleViewModel.fetchServicesForDoctor(currentDoctor)
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                HorizontalDotLoader(color = Blue85)
                            }
                        }
                    }

                    item {
                        AvailableTimesCalendar(
                            availableDates = appointmentState.availableDates,
                            selectedDate = currentAppointmentInfo.selectedDate,
                            currentMonth = appointmentState.currentMonth,
                            onMonthChanged = { month ->
                                doctorScheduleViewModel.onEvent(
                                    DoctorScheduleEvent.SetCurrentMonth(month)
                                )
                            },
                            onDateSelected = { date ->
                                doctorScheduleViewModel.onEvent(
                                    DoctorScheduleEvent.SetCurrentDate(date)
                                )
                            }
                        )
                    }

                    item {
                        LaunchedEffect(
                            key1 = currentAppointmentInfo.isOnSelectedMonth
                        ) {
                            hiddenDateState.targetState = !currentAppointmentInfo.isOnSelectedMonth
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = R.string.appointment_time),
                                fontFamily = Quicksand,
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp,
                                color = MaterialTheme.colors.textOnPrimary,

                                modifier = Modifier.padding(horizontal = 6.dp)
                            )
                            AnimatedVisibility(
                                visibleState = hiddenDateState,
                                enter = slideInHorizontally(
                                    animationSpec = tween(durationMillis = 300),
                                ) + fadeIn(
                                    animationSpec = tween(durationMillis = 300)
                                ),
                                exit = slideOutHorizontally(
                                    animationSpec = tween(durationMillis = 300),
                                ) + fadeOut(
                                    animationSpec = tween(durationMillis = 300)
                                )
                            ) {
                                LaunchedEffect(true) {
                                    if (currentAppointmentInfo.selectedDate != null) {
                                        dayAndMonth = CustomDateFormatter.dateAsString(
                                            currentAppointmentInfo.selectedDate!!,
                                            omitYear = true
                                        )
                                    }
                                }
                                Text(
                                    text = "- $dayAndMonth",
                                    fontFamily = Quicksand,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colors.textOnPrimary,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }

                    item {
                        FlowRow(
                            mainAxisSpacing = 8.dp,
                            crossAxisSpacing = 10.dp,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            appointmentState.currentAvailableTimesList.forEach { time ->
                                BorderAnimatedItem(
                                    isSelected = currentAppointmentInfo.selectedTime == time,
                                    shape = RoundedCornerShape(10.dp),
                                    backgroundColor = MaterialTheme.colors.calendarField,
                                    borderColor = MaterialTheme.colors.textFieldOutline,
                                    onSelect = {
                                        doctorScheduleViewModel.onEvent(
                                            DoctorScheduleEvent.SetAppointmentTime(time)
                                        )
                                    },
                                    modifier = Modifier.wrapContentSize()
                                ) {
                                    Text(
                                        text = time.toString(),
                                        fontFamily = Quicksand,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 18.sp,
                                        modifier = Modifier.padding(
                                            horizontal = 25.dp,
                                            vertical = 15.dp
                                        )
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Button(
                            shape = RoundedShape20,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.selectable
                            ),
//                            enabled = appointmentState.selectedTimesList.isNotEmpty() &&
//                                    appointmentState.selectedTimesList[pagerState.currentPage] != null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(BOOK_APPOINTMENT_BUTTON_HEIGHT),
                            onClick = {
                                if (pagerState.currentPage < pagerState.pageCount - 1) {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(
                                            pagerState.currentPage + 1
                                        )
                                    }
                                } else {
                                    doctorScheduleViewModel.onEvent(
                                        DoctorScheduleEvent.ScheduleAppointment(doctors)
                                    )
                                }
                            }
                        ) {
                            Text(
                                text = if (pagerState.currentPage < pagerState.pageCount - 1)
                                    stringResource(id = R.string.next_button_text)
                                else
                                    stringResource(id = R.string.schedule_appointment_button),
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

        BackHandler(enabled = true) {
            snackBarJob?.cancel()
            onBackPressed()
        }
    }
}

// Calendar composable

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AvailableTimesCalendar(
    availableDates: List<LocalDate>,
    selectedDate: LocalDate?,
    currentMonth: Month,
    onMonthChanged: (Month) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) -180f else 0f,
        animationSpec = tween(durationMillis = 200)
    )
    val remainingMonths: List<Month> by remember {
        derivedStateOf {
            val listOfMonths: MutableList<Month> = mutableListOf()
            val remainingMonths: Long = 12L - currentMonth.value.toLong()
            var currentMonthOffset = 0L
            while (currentMonthOffset <= remainingMonths) {
                listOfMonths.add(currentMonth.plus(currentMonthOffset))
                currentMonthOffset++
            }
            listOfMonths
        }
    }
    Column {
        Row(
            modifier = Modifier.padding(start = 6.dp, end = 6.dp, top = 6.dp, bottom = 14.dp)
        ) {
            Text(
                text = stringResource(id = R.string.appointment_date),
                fontFamily = Quicksand,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = MaterialTheme.colors.textOnPrimary,
                modifier = Modifier.weight(1f)
            )
            Box {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { expanded = true }
                ) {
                    Text(
                        text = currentMonth.name
                            .lowercase()
                            .replaceFirstChar {
                                if (it.isLowerCase())
                                    it.titlecase(Locale.getDefault())
                                else
                                    it.toString()
                            },
                        fontFamily = Quicksand,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colors.textOnPrimary.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 7.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier
                            .size(35.dp)
                            .padding(top = 10.dp)
                            .graphicsLayer {
                                rotationZ = arrowRotation
                            },
                        tint = MaterialTheme.colors.textOnPrimary.copy(alpha = 0.8f),
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .background(color = MaterialTheme.colors.secondary)
                ) {
                    remainingMonths.forEach { month ->
                        DropdownMenuItem(
                            onClick = {
                                onMonthChanged(month)
                                expanded = false
                            }
                        ) {
                            Text(
                                text = month.name
                                    .lowercase()
                                    .replaceFirstChar {
                                        if (it.isLowerCase())
                                            it.titlecase(Locale.getDefault())
                                        else
                                            it.toString()
                                    },
                                fontFamily = Quicksand,
                                fontSize = 16.sp,
                                color = MaterialTheme.colors.textOnSecondary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(start = 4.dp)
        ) {
            items(items = availableDates) { date ->
                BorderAnimatedItem(
                    isSelected = date == selectedDate,
                    shape = RoundedCornerShape(10.dp),
                    backgroundColor = MaterialTheme.colors.calendarField,
                    borderColor = MaterialTheme.colors.textFieldOutline,
                    onSelect = { onDateSelected(date) },
                    modifier = Modifier
                        .width(60.dp)
                        .height(90.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp)
                    ) {
                        Text(
                            text = date.dayOfWeek.name
                                .lowercase()
                                .replaceFirstChar {
                                    if (it.isLowerCase())
                                        it.titlecase(Locale.getDefault())
                                    else
                                        it.toString()
                                }
                                .substring(0, 3),
                            fontSize = 14.sp,
                            fontFamily = Quicksand,
                            color = MaterialTheme.colors.textOnSecondary.copy(alpha = 0.8f)
                        )
                        Text(
                            text = date.dayOfMonth.toString(),
                            fontFamily = Quicksand,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.textOnSecondary
                        )
                    }
                }
            }
        }
    }
}