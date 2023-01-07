package rs.ac.bg.etf.diplomski.medsched.presentation.patient.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.CARD_IMAGE_SIZE
import rs.ac.bg.etf.diplomski.medsched.presentation.composables.defaultButtonColors
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.DoctorDetails
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.PatientHomeStart
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.PatientViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.events.PatientEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.states.PatientState
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.*
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.Loader

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PatientHomeScreen(
    patientViewModel: PatientViewModel = hiltViewModel(),
    toggleBottomBar: () -> Unit
) {
    val doctorDetailsNavController = rememberAnimatedNavController()
    val coroutineScope = rememberCoroutineScope()

    AnimatedNavHost(
        navController = doctorDetailsNavController,
        startDestination = PatientHomeStart.route,
        enterTransition = { fadeIn(tween(durationMillis = 1)) },
        exitTransition = { fadeOut(tween(durationMillis = 1)) }
    ) {
        composable(
            route = PatientHomeStart.route
        ) {
            PatientStart(
                patientViewModel = patientViewModel,
                navigateToDoctorDetails = {
                    doctorDetailsNavController.navigate(DoctorDetails.route)
                },
                toggleBottomBar = toggleBottomBar
            )
        }
        composable(
            route = DoctorDetails.route
        ) {
            DoctorAppointmentScreen(
                patientViewModel = patientViewModel,
                doctor = patientViewModel.getSelectedDoctor(),
                onBackPressed = {
                    coroutineScope.launch {
                        delay(300L)
                        doctorDetailsNavController.popBackStack()
                        delay(200L)
                        patientViewModel.onEvent(PatientEvent.SelectDoctor(null))
                        toggleBottomBar()
                    }
                }
            )
        }
    }

}

@Composable
fun PatientStart(
    patientViewModel: PatientViewModel,
    navigateToDoctorDetails: () -> Unit,
    toggleBottomBar: () -> Unit
) {
    val user by patientViewModel.userFlow.collectAsState(initial = null)
    val patientState by patientViewModel.patientState.collectAsState()
    val scale by animateFloatAsState(
        targetValue = if (patientState.selectedDoctor != null) 25f else 1f,
        animationSpec = tween(
            durationMillis = 500,
            easing = LinearEasing
        ),
        finishedListener = {
            if (patientState.selectedDoctor != null) {
                navigateToDoctorDetails()
            }
        }
    )
    val alpha by animateFloatAsState(
        targetValue = if (patientState.selectedDoctor != null) 1f else 0f,
        animationSpec = tween(durationMillis = 500)
    )

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
                        text = "${user!!.firstName} ${user!!.lastName}",
                        fontFamily = Quicksand,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = MaterialTheme.colors.textOnPrimary,
                        modifier = Modifier.offset(y = (-8).dp)
                    )
                }
            }
            Image(
                painter = painterResource(id = R.drawable.caduceus),
                contentDescription = "",
                colorFilter = ColorFilter.tint(
                    color = MaterialTheme.colors.textOnPrimary
                ),
                contentScale = ContentScale.Fit
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 30.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.CenterEnd
                ) {
                    OutlinedTextField(
                        value = patientState.searchKeyWord,
                        onValueChange = {
                            patientViewModel.onEvent(PatientEvent.SearchTextChange(it))
                        },
                        shape = RoundedShape20,
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.doctor_search),
                                fontFamily = Quicksand
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                // TODO implement doctor search
                            }
                        ),
                        colors = defaultButtonColors(),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(60.dp)
                    )
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.textFieldOutline
                        ),
                        shape = RoundedShape20,
                        modifier = Modifier
                            .height(60.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Doctor search button"
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(top = 16.dp))
            CategoriesList(
                patientState = patientState,
                onCategorySelected = {
                    patientViewModel.onEvent(PatientEvent.SelectService(it))
                }
            )
            Spacer(modifier = Modifier.padding(top = 16.dp))
            DoctorsList(
                patientState = patientState,
                onDoctorSelected = {
                    patientViewModel.onEvent(PatientEvent.SelectDoctor(it))
                    toggleBottomBar()
                }
            )
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .scale(scale)
                .graphicsLayer(alpha = alpha)
                .clip(CircleShape)
                .background(color = MaterialTheme.colors.secondary)
        )
    }
}

suspend fun LazyListState.animateScrollAndCentralizeItem(index: Int) {
    val itemInfo = this.layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }
    if (itemInfo != null) {
        val center = layoutInfo.viewportEndOffset / 2
        val childCenter = itemInfo.offset + itemInfo.size / 2
        animateScrollBy((childCenter - center).toFloat())
    } else {
        animateScrollToItem(index)
    }
}

@Composable
fun CategoriesList(
    patientState: PatientState,
    onCategorySelected: (Int) -> Unit
) {

    val rememberLazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }

    Text(
        text = stringResource(id = R.string.categories_list),
        fontFamily = Quicksand,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        color = MaterialTheme.colors.textOnPrimary,
        modifier = Modifier.padding(start = 16.dp)
    )
    Spacer(modifier = Modifier.padding(top = 26.dp))
    LazyRow(
        state = rememberLazyListState,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        itemsIndexed(patientState.categoryList) { index, service ->
            Card(
                shape = RoundedShape40,
                backgroundColor = if (patientState.selectedService == index)
                    MaterialTheme.colors.selectable
                else Blue85,
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onCategorySelected(index)
                        coroutineScope.launch {
                            rememberLazyListState
                                .animateScrollAndCentralizeItem(index)
                        }
                    }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    SubcomposeAsyncImage(
                        model = service.imageRequest,
                        contentDescription = "Service icon",
                        loading = {
                            CircularProgressIndicator(color = Color.Black)
                        },
                        colorFilter = ColorFilter.tint(
                            color = if (patientState.selectedService == index)
                                Color.White
                            else Color.Black
                        ),
                        modifier = Modifier
                            .size(CARD_IMAGE_SIZE)
                            .padding(bottom = 10.dp)
                    )
                    Text(
                        text = service.name,
                        fontFamily = Quicksand,
                        fontWeight = FontWeight.SemiBold,
                        color = if (patientState.selectedService == index)
                            Color.White
                        else Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun DoctorsList(
    patientState: PatientState,
    onDoctorSelected: (Int) -> Unit
) {
    Text(
        text = stringResource(id = R.string.doctors_list),
        fontFamily = Quicksand,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        color = MaterialTheme.colors.textOnPrimary,
        modifier = Modifier.padding(start = 16.dp)
    )
    if (!patientState.doctorsLoading) {
        Spacer(modifier = Modifier.padding(top = 26.dp))
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(patientState.doctorList) { index, doctor ->
                Card(
                    shape = RoundedShape20,
                    backgroundColor = MaterialTheme.colors.secondary,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(100.dp)
                        .clickable {
                            onDoctorSelected(index)
                        }
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SubcomposeAsyncImage(
                            model = doctor.imageRequest,
                            contentDescription = "Doctor image",
                            loading = {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colors.textOnSecondary,
                                    modifier = Modifier.size(CARD_IMAGE_SIZE)
                                )
                            },
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .clip(RoundedShape20)
                                .size(CARD_IMAGE_SIZE)
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(start = 10.dp)
                        ) {
                            Text(
                                text = "${doctor.firstName} ${doctor.lastName}",
                                fontFamily = Quicksand,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colors.textOnSecondary
                            )
                        }
                    }
                }
            }
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Loader()
        }
    }

}