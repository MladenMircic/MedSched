package rs.ac.bg.etf.diplomski.medsched.presentation.patient.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Patient
import rs.ac.bg.etf.diplomski.medsched.presentation.composables.SearchField
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.DoctorDetails
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.PatientHomeStart
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.events.PatientEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.stateholders.PatientHomeViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.states.PatientState
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.*
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.HorizontalDotLoader

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun PatientHomeScreen(
    patientHomeViewModel: PatientHomeViewModel = hiltViewModel(),
    toggleBottomBar: () -> Unit
) {
    val doctorDetailsNavController = rememberAnimatedNavController()
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

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
                patientHomeViewModel = patientHomeViewModel,
                navigateToDoctorDetails = {
                    doctorDetailsNavController.navigate(DoctorDetails.route)
                },
                searchDoctors = {
                    keyboardController?.hide()
                    patientHomeViewModel.onEvent(PatientEvent.SearchForDoctor)
                },
                toggleBottomBar = toggleBottomBar
            )
        }
        composable(
            route = DoctorDetails.route
        ) {
            DoctorAppointmentScreen(
                patientHomeViewModel = patientHomeViewModel,
                doctor = patientHomeViewModel.getSelectedDoctor(),
                onBackPressed = {
                    coroutineScope.launch {
                        delay(300L)
                        doctorDetailsNavController.popBackStack()
                        delay(200L)
                        patientHomeViewModel.onEvent(PatientEvent.SelectDoctor(null))
                        toggleBottomBar()
                    }
                }
            )
        }
    }

}

@Composable
fun PatientStart(
    patientHomeViewModel: PatientHomeViewModel,
    navigateToDoctorDetails: () -> Unit,
    searchDoctors: () -> Unit,
    toggleBottomBar: () -> Unit
) {
    val user by patientHomeViewModel.userFlow.collectAsState(initial = null)
    val patientState by patientHomeViewModel.patientState.collectAsState()
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
                    val patient = it as Patient
                    Text(
                        text = "${stringResource(id = R.string.welcome)},",
                        fontFamily = Quicksand,
                        fontSize = 22.sp,
                        color = MaterialTheme.colors.textOnPrimary
                    )
                    Text(
                        text = "${patient.firstName} ${patient.lastName}",
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
            SearchField(
                searchKeyWord = patientState.searchKeyWord,
                onKeyWordChange = {
                    patientHomeViewModel.onEvent(PatientEvent.SearchTextChange(it))
                },
                label = R.string.doctor_search,
                onSearchSubmit = searchDoctors,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(top = 16.dp))
            CategoriesList(
                patientState = patientState,
                onCategorySelected = {
                    patientHomeViewModel.onEvent(PatientEvent.SelectService(it))
                },
                getCategoryNameId = patientHomeViewModel::categoryIdToNameId
            )
            Spacer(modifier = Modifier.padding(top = 16.dp))
            DoctorsList(
                patientState = patientState,
                onDoctorSelected = {
                    patientHomeViewModel.onEvent(PatientEvent.SelectDoctor(it))
                    toggleBottomBar()
                },
                getSpecializationNameId = patientHomeViewModel::specializationIdToNameId
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
    onCategorySelected: (Int) -> Unit,
    getCategoryNameId: (Int) -> Int?
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
                    if (index == 0) {
                        Image(
                            painter = painterResource(id = R.drawable.all_organs),
                            contentDescription = "Service icon",
                            colorFilter = ColorFilter.tint(
                                color = if (patientState.selectedService == index)
                                    Color.White
                                else Color.Black
                            ),
                            modifier = Modifier
                                .size(CARD_IMAGE_SIZE)
                                .padding(bottom = 10.dp)
                        )
                    } else {
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
                    }
                    val categoryNameId = getCategoryNameId(service.id)
                    Text(
                        text = if (categoryNameId != null)
                            stringResource(id = categoryNameId)
                        else "",
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
    onDoctorSelected: (Int) -> Unit,
    getSpecializationNameId: (Int) -> Int
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
            itemsIndexed(patientState.currentDoctorList) { index, doctor ->
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
                            Text(
                                text = stringResource(
                                    id = getSpecializationNameId(doctor.specializationId)
                                ),
                                fontFamily = Quicksand,
                                fontSize = 16.sp,
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
            HorizontalDotLoader(color = Blue85)
        }
    }

}