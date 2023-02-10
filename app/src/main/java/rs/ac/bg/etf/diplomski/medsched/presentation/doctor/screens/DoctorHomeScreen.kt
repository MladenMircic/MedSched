package rs.ac.bg.etf.diplomski.medsched.presentation.doctor.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.CustomDateFormatter
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentForDoctor
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.User
import rs.ac.bg.etf.diplomski.medsched.presentation.doctor.stateholders.DoctorHomeViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.*

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DoctorHomeScreen(
    doctorHomeViewModel: DoctorHomeViewModel = hiltViewModel()
) {
    val appointmentForDoctorList by doctorHomeViewModel.appointmentsForDoctorFlow.collectAsState(
        initial = listOf()
    )
    val doctorState by doctorHomeViewModel.doctorState.collectAsState()
    val user: User? by doctorHomeViewModel.userFlow.collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight(0.12f)
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                user?.let {
                    Text(
                        text = "${stringResource(id = R.string.welcome)},",
                        fontFamily = Quicksand,
                        fontSize = 22.sp,
                        color = MaterialTheme.colors.textOnPrimary
                    )
                    Text(
                        text = "Dr ${it.firstName} ${it.lastName}",
                        fontFamily = Quicksand,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = MaterialTheme.colors.textOnPrimary,
                        modifier = Modifier.offset(y = (-8).dp)
                    )
                }
            }
            SubcomposeAsyncImage(
                model = doctorState.doctorImageRequest,
                contentDescription = "Doctor image",
                loading = {
                    CircularProgressIndicator(color = MaterialTheme.colors.textOnPrimary)
                },
                modifier = Modifier
                    .size(80.dp)
            )
        }
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 20.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            itemsIndexed(items = appointmentForDoctorList) { index, appointmentForDoctor ->
                AppointmentForDoctorCard(
                    appointmentForDoctor = appointmentForDoctor,
                    onCancelAppointment = { },
                    getServiceId = doctorHomeViewModel::serviceIdToNameId,
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

@Composable
fun AppointmentForDoctorCard(
    modifier: Modifier = Modifier,
    appointmentForDoctor: AppointmentForDoctor,
//    waitForDelete: Boolean,
//    toDelete: Boolean,
//    toggleRevealItem: () -> Unit,
    onCancelAppointment: () -> Unit,
//    onDeleteAppointment: () -> Unit,
    getServiceId: (Int) -> Int
) {
    var revealed by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(true) {
        revealed = true
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
                    .height(210.dp),
                backgroundColor = Blue85,
                shape = RoundedShape20
            ) {
                Column {
                    Row(modifier = Modifier.padding(16.dp)) {
                        Column {
                            Text(
                                text = appointmentForDoctor.patientName,
                                fontFamily = Quicksand,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = stringResource(
                                id = getServiceId(appointmentForDoctor.appointment.examId)
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

                                val date = appointmentForDoctor.appointment.date
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

                                val time = appointmentForDoctor.appointment.time
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
                                    imageVector = if (appointmentForDoctor.appointment.confirmed)
                                        Icons.Filled.EventAvailable
                                    else Icons.Filled.EventBusy,
                                    contentDescription = "Availability icon",
                                    tint = Color.Black
                                )
                                Spacer(modifier = Modifier.padding(start = 4.dp))
                                Text(
                                    text = if (appointmentForDoctor.appointment.confirmed)
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
                            //enabled = !waitForDelete,
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .fillMaxHeight(0.5f),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.selectable
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = if (appointmentForDoctor.appointment.confirmed)
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
//        Box(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(240.dp)
//                .clip(RoundedShape20)
//                .background(Color.Black.copy(alpha = if (waitForDelete) 0.3f else 0f))
//        ) {
//            if (waitForDelete) {
//                CircleDotLoader(color = Color.Black)
//            }
//        }
    }

}