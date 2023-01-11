package rs.ac.bg.etf.diplomski.medsched.presentation.patient.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.leinardi.android.speeddial.compose.FabWithLabel
import com.leinardi.android.speeddial.compose.SpeedDial
import com.leinardi.android.speeddial.compose.SpeedDialOverlay
import com.leinardi.android.speeddial.compose.SpeedDialState
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.presentation.composables.CustomBottomBar
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.*
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.*

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PatientScreen(navController: NavHostController = rememberAnimatedNavController()) {
    var bottomBarVisible by rememberSaveable { mutableStateOf(true) }
    var speedDialState by rememberSaveable { mutableStateOf(SpeedDialState.Collapsed) }
    var overlayVisible: Boolean by rememberSaveable { mutableStateOf(speedDialState.isExpanded()) }
    var speedDialVisible by rememberSaveable { mutableStateOf(false) }
    var currentProfileDestination by remember {
        mutableStateOf<PatientProfileDestinations>(MainProfile)
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = bottomBarVisible,
                enter = slideInVertically(
                    animationSpec = tween(
                        durationMillis = 200,
                        delayMillis = 300
                    ),
                    initialOffsetY = { it }
                ),
                exit = slideOutVertically(
                    animationSpec = tween(
                        durationMillis = 500
                    ),
                    targetOffsetY = { it }
                )
            ) {
                CustomBottomBar(
                    destinations = patientRoutes,
                    navController = navController
                )
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = speedDialVisible,
                enter = scaleIn(
                    animationSpec = tween(
                        durationMillis = 200,
                        delayMillis = 100
                    ),
                ),
                exit = scaleOut(
                    animationSpec = tween(
                        durationMillis = 200,
                        delayMillis = 100
                    ),
                )
            ) {
                SpeedDial(
                    state = speedDialState,
                    fabShape = CircleShape,
                    fabClosedContainerColor = MaterialTheme.colors.selectable,
                    fabOpenedContainerColor = MaterialTheme.colors.selectable,
                    onFabClick = { expanded ->
                        overlayVisible = !expanded
                        speedDialState = speedDialState.toggle()
                    },
                    fabOpenedContent = {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close Icon",
                            tint = Color.White
                        )
                    },
                    fabClosedContent = {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit Icon",
                            tint = MaterialTheme.colors.textOnSecondary
                        )
                    }
                ) {
                    item {
                        FabWithLabel(
                            onClick = {
                                currentProfileDestination = ChangeInfo
                                speedDialState = SpeedDialState.Collapsed
                            },
                            labelContent = {
                                Text(
                                    text = stringResource(id = R.string.change_info),
                                    fontFamily = Quicksand,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            labelColors = AssistChipDefaults.elevatedAssistChipColors(
                                labelColor = MaterialTheme.colors.textOnPrimary,
                                containerColor = Blue85
                            ),
                            fabContainerColor = Blue85
                        ) {
                            Icon(Icons.Default.Info, null)
                        }
                    }
                    item {
                        FabWithLabel(
                            onClick = {
                                currentProfileDestination = ChangePassword
                                speedDialState = SpeedDialState.Collapsed
                            },
                            labelContent = {
                                Text(
                                    text = stringResource(id = R.string.change_password),
                                    fontFamily = Quicksand,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            labelColors = AssistChipDefaults.elevatedAssistChipColors(
                                labelColor = MaterialTheme.colors.textOnPrimary,
                                containerColor = Blue85
                            ),
                            fabContainerColor = Blue85
                        ) {
                            Icon(Icons.Default.Password, null)
                        }
                    }
                    item {
                        FabWithLabel(
                            onClick = {
                                currentProfileDestination = ChangeEmail
                                speedDialState = SpeedDialState.Collapsed
                            },
                            labelContent = {
                                Text(
                                    text = stringResource(id = R.string.change_email),
                                    fontFamily = Quicksand,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            labelColors = AssistChipDefaults.elevatedAssistChipColors(
                                labelColor = MaterialTheme.colors.textOnPrimary,
                                containerColor = Blue85
                            ),
                            fabContainerColor = Blue85
                        ) {
                            Icon(Icons.Default.Email, null)
                        }
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        backgroundColor = MaterialTheme.colors.primary
    ) {
        AnimatedNavHost(
            navController = navController,
            startDestination = PatientHome.route,
            modifier = Modifier.padding(it),
            enterTransition = { fadeIn(tween(durationMillis = 1)) },
            exitTransition = { fadeOut(tween(durationMillis = 1)) }
        ) {
            composable(route = PatientHome.route) {
                LaunchedEffect(true) {
                    speedDialVisible = false
                    overlayVisible = false
                    speedDialState = SpeedDialState.Collapsed
                }
                PatientHomeScreen(toggleBottomBar = {
                    bottomBarVisible = !bottomBarVisible
                })
            }
            composable(route = PatientScheduled.route) {
                LaunchedEffect(true) {
                    speedDialVisible = false
                    overlayVisible = false
                    speedDialState = SpeedDialState.Collapsed
                }
                ScheduledAppointmentsScreen()
            }
            composable(route = PatientInfo.route) {
                LaunchedEffect(key1 = currentProfileDestination) {
                    speedDialVisible = currentProfileDestination == MainProfile
                    bottomBarVisible = currentProfileDestination == MainProfile
                    overlayVisible = false
                }
                PatientProfileScreen(
                    profileDestination = currentProfileDestination,
                    setStartProfileDestination = {
                        speedDialState = SpeedDialState.Collapsed
                        currentProfileDestination = MainProfile
                    }
                )
            }
        }
        SpeedDialOverlay(
            visible = overlayVisible,
            color = Color.Black.copy(alpha = 0.66f),
            onClick = {
                overlayVisible = false
                speedDialState = speedDialState.toggle()
            },
        )
    }
}