package rs.ac.bg.etf.diplomski.medsched.presentation.patient.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.CustomDateFormatter
import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.NotificationPatientEntity
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.events.PatientEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.stateholders.PatientHomeViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.Quicksand
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.selectable
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.textOnPrimary

@Composable
fun NotificationsScreen(
    patientHomeViewModel: PatientHomeViewModel,
    onBack: () -> Unit
) {
    val notifications by patientHomeViewModel.notificationsFlow.collectAsState(initial = listOf())
    val state = rememberLazyListState()
    val visibleItemIds: List<Int> by remember {
        derivedStateOf {
            val layoutInfo = state.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (visibleItemsInfo.isEmpty()) {
                emptyList()
            } else {
                visibleItemsInfo.map { it.index }
            }
        }
    }
    val readIndices = remember { mutableStateListOf<Int>() }

    LaunchedEffect(key1 = visibleItemIds) {
        visibleItemIds.forEach { index ->
            if (!readIndices.contains(index)) {
                readIndices.add(index)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 16.dp)
        ) {
            IconButton(
                onClick = {
                    patientHomeViewModel.onEvent(
                        PatientEvent.UpdateNotificationsRead(readIndices)
                    )
                    onBack()
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "",
                    tint = MaterialTheme.colors.textOnPrimary,
                    modifier = Modifier.size(25.dp)
                )
            }
            Text(
                text = stringResource(id = R.string.notifications),
                fontFamily = Quicksand,
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                color = MaterialTheme.colors.textOnPrimary,
                modifier = Modifier.padding(start = 4.dp, bottom = 5.dp)
            )
        }
        LazyColumn(
            state = state,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(vertical = 24.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items = notifications) { notification ->
                NotificationItem(notification = notification)
            }
        }
    }

    BackHandler {
        patientHomeViewModel.onEvent(
            PatientEvent.UpdateNotificationsRead(readIndices)
        )
        onBack()
    }
}

@Composable
fun NotificationItem(
    notification: NotificationPatientEntity
) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Row {
            when (notification.type) {
                NotificationType.SCHEDULED -> {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(60.dp)
                            .background(color = Color(0xFF2DD0AE).copy(alpha = 0.2f))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.calendar_confirmed),
                            contentDescription = "",
                            tint = Color(0xFF2DD0AE),
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
                NotificationType.CANCELLED -> {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(60.dp)
                            .background(color = Color(0xFFFF667A).copy(alpha = 0.2f))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.calendar_cancelled),
                            contentDescription = "",
                            tint = Color(0xFFFF667A),
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(start = 14.dp)) {
                Text(
                    text = when (notification.type) {
                        NotificationType.SCHEDULED -> {
                            stringResource(id = R.string.appointment_scheduled_heading)
                        }
                        NotificationType.CANCELLED -> {
                            stringResource(id = R.string.appointment_cancelled_heading)
                        }
                    },
                    fontFamily = Quicksand,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.textOnPrimary
                )
                val date by remember {
                    derivedStateOf {
                        CustomDateFormatter.dateAsString(notification.dateNotified)
                    }
                }
                val time by remember {
                    derivedStateOf {
                        CustomDateFormatter.timeAsString(notification.timeNotified)
                    }
                }
                Row {
                    Text(
                        text = "$date | $time",
                        fontFamily = Quicksand,
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.textOnPrimary
                    )
                    AnimatedVisibility(
                        visible = !notification.read,
                        enter = fadeIn(
                            animationSpec = tween(durationMillis = 300)
                        ),
                        exit = fadeOut(
                            animationSpec = tween(durationMillis = 300)
                        )
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colors.selectable.copy(alpha = 0.4f),
                                contentColor = MaterialTheme.colors.textOnPrimary
                            ),
                            modifier = Modifier
                                .width(80.dp)
                                .height(30.dp)
                                .padding(start = 12.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = stringResource(id = R.string.new_notification),
                                    fontFamily = Quicksand,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(bottom = 3.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
        Text(
            text = when (notification.type) {
                NotificationType.SCHEDULED -> {
                    stringResource(
                        id = R.string.appointment_scheduled_content,
                        notification.doctorName,
                        CustomDateFormatter.dateAsString(notification.dateOfAction),
                        CustomDateFormatter.timeAsString(notification.timeOfAction)
                    )
                }
                NotificationType.CANCELLED -> {
                    stringResource(
                        id = R.string.appointment_cancelled_content,
                        notification.doctorName,
                        CustomDateFormatter.dateAsString(notification.dateOfAction),
                        CustomDateFormatter.timeAsString(notification.timeOfAction)
                    )
                }
            },
            fontFamily = Quicksand,
            fontSize = 16.sp,
            color = MaterialTheme.colors.textOnPrimary,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 12.dp)
        )
    }
}

enum class NotificationType {
    SCHEDULED, CANCELLED
}