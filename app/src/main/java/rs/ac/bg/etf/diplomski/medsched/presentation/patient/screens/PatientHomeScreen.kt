package rs.ac.bg.etf.diplomski.medsched.presentation.patient.screens

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
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
import kotlinx.coroutines.launch
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.CARD_IMAGE_SIZE
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.ClinicForPatient
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Patient
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.ClinicIdToNameMapUseCase
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.DoctorDetails
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.PatientHomeStart
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.events.PatientEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.stateholders.PatientHomeViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.states.PatientState
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.*
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.VisibilityList
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.animatedItemsIndexed

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun PatientHomeScreen(
    patientHomeViewModel: PatientHomeViewModel = hiltViewModel(),
    toggleBottomBar: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToSearch: (String) -> Unit
) {
    val doctorDetailsNavController = rememberAnimatedNavController()
    val keyboardController = LocalSoftwareKeyboardController.current
    var transitionToScheduling by rememberSaveable { mutableStateOf(false) }

    AnimatedNavHost(
        navController = doctorDetailsNavController,
        startDestination = PatientHomeStart.route
    ) {
        composable(
            route = PatientHomeStart.route,
            enterTransition = {
                if (transitionToScheduling) {
                    fadeIn(tween(durationMillis = 1))
                } else {
                    slideInHorizontally(
                        animationSpec = tween(durationMillis = 300),
                        initialOffsetX = { -it }
                    )
                }
            },
            exitTransition = {
                if (transitionToScheduling) {
                    fadeOut(tween(durationMillis = 1))
                } else {
                    slideOutHorizontally(
                        animationSpec = tween(durationMillis = 300),
                        targetOffsetX = { -it }
                    )
                }
            }
        ) {
            PatientStart(
                patientHomeViewModel = patientHomeViewModel,
                navigateToDoctorDetails = {
                    transitionToScheduling = true
                    doctorDetailsNavController.navigate(DoctorDetails.route)
                },
                navigateToNotifications = onNavigateToNotifications,
                navigateToSearch = {
                    onNavigateToSearch(patientHomeViewModel.getCategoryIdsAsString())
                },
                    searchDoctors = {
                        keyboardController?.hide()
                        patientHomeViewModel.onEvent(PatientEvent.SearchForDoctor)
                    },
                    toggleBottomBar = toggleBottomBar
            )
        }
        composable(
            route = DoctorDetails.route,
            enterTransition = { fadeIn(animationSpec = tween(durationMillis = 1)) }
        ) {
//            DoctorAppointmentScreen(
//                doctors = patientHomeViewModel.getSelectedDoctor(),
//                onBackPressed = {
//                    coroutineScope.launch {
//                        delay(400L)
//                        doctorDetailsNavController.popBackStack()
//                        delay(200L)
//                        transitionToScheduling = false
//                        patientHomeViewModel.onEvent(PatientEvent.SelectDoctor(null))
//                    }
//                }
//            )
        }
    }

}

@Composable
fun PatientStart(
    patientHomeViewModel: PatientHomeViewModel,
    navigateToDoctorDetails: () -> Unit,
    navigateToNotifications: () -> Unit,
    navigateToSearch: () -> Unit,
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
            } else {
                toggleBottomBar()
            }
        }
    )
    val alpha by animateFloatAsState(
        targetValue = if (patientState.selectedDoctor != null) 1f else 0f,
        animationSpec = tween(durationMillis = 500)
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
    ) {
        item {
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
                BadgedBox(
                    badge = {
                        if (patientState.newNotificationCount > 0) {
                            Badge(
                                backgroundColor = MaterialTheme.colors.secondary,
                                contentColor = MaterialTheme.colors.textOnSecondary
                            ) {
                                Text(
                                    text = "${patientState.newNotificationCount}",
                                    fontFamily = Quicksand
                                )
                            }
                        }
                    },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    IconButton(
                        onClick = navigateToNotifications,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.notification_bell),
                            contentDescription = "",
                            tint = MaterialTheme.colors.textOnPrimary
                        )
                    }
                }
            }
        }
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(top = 12.dp)
            ) {
                Card(
                    backgroundColor = MaterialTheme.colors.secondary,
                    contentColor = MaterialTheme.colors.textOnSecondary,
                    shape = RoundedShape20,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(60.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { navigateToSearch() }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        Row(
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "",
                                modifier = Modifier.padding(top = 2.dp)
                            )
                            Text(
                                text = stringResource(id = R.string.search),
                                fontFamily = Quicksand,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(start = 12.dp, bottom = 2.dp)
                            )
                        }
                        Icon(
                            painter = painterResource(id = R.drawable.filter),
                            contentDescription = "",
                            tint = MaterialTheme.colors.selectable,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
//                SearchField(
//                    searchKeyWord = patientState.searchKeyWord,
//                    onKeyWordChange = {
//                        patientHomeViewModel.onEvent(PatientEvent.SearchTextChange(it))
//                    },
//                    label = R.string.search,
//                    onSearchSubmit = searchDoctors,
//                    modifier = Modifier.fillMaxWidth()
//                )
            }
        }
        item {
            CategoriesList(
                patientState = patientState,
                onCategorySelected = {
                    patientHomeViewModel.onEvent(PatientEvent.SelectCategory(it))
                },
                getCategoryNameId = {
                    ClinicIdToNameMapUseCase.categoryIdToNameId(it)
                }
            )
        }
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val animationState = remember {
                    MutableTransitionState(false).apply {
                        targetState = true
                    }
                }
                LaunchedEffect(key1 = animationState.currentState) {
                    if (animationState.isIdle && !animationState.currentState) {
                        patientHomeViewModel.onEvent(PatientEvent.ToggleDoctorsClinics)
                        animationState.targetState = true
                    }
                }
                AnimatedVisibility(
                    visibleState = animationState,
                    enter = slideInHorizontally(
                        animationSpec = tween(durationMillis = 500),
                        initialOffsetX = { -it }
                    ) + fadeIn(
                        animationSpec = tween(durationMillis = 300)
                    ),
                    exit = slideOutHorizontally(
                            animationSpec = tween(durationMillis = 500),
                            targetOffsetX = { -it }
                    ) + fadeOut (
                        animationSpec = tween(durationMillis = 300)
                    ),
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = if (patientState.showingDoctors)
                            stringResource(id = R.string.doctors_list)
                        else stringResource(id = R.string.clinics_list),
                        fontFamily = Quicksand,
                        fontWeight = FontWeight.Bold,
                        fontSize = 34.sp,
                        color = MaterialTheme.colors.textOnPrimary
                    )
                }
                AnimatedVisibility(
                    visibleState = animationState,
                    enter = slideInHorizontally(
                        animationSpec = tween(durationMillis = 500),
                        initialOffsetX = { it }
                    ) + fadeIn(
                        animationSpec = tween(durationMillis = 300)
                    ),
                    exit = slideOutHorizontally(
                        animationSpec = tween(durationMillis = 500),
                        targetOffsetX = { it }
                    ) + fadeOut(
                        animationSpec = tween(durationMillis = 300)
                    ),
                    modifier = Modifier
                        .padding(top = 9.dp, end = 16.dp)
                ) {
                    Text(
                        text = if (patientState.showingDoctors)
                            stringResource(id = R.string.show_clinics)
                        else stringResource(id = R.string.show_doctors),
                        fontFamily = Quicksand,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.selectable,
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                patientHomeViewModel.onEvent(PatientEvent.ClearCurrentShowingList)
                                animationState.targetState = false
                            }
                    )
                }
            }
        }
        if (patientState.showingDoctors) {
//            doctorsList(
//                list = patientState.currentDoctorList,
//                doctorsLoading = patientState.dataLoading,
//                onDoctorSelectedForSchedule = {
//                    patientHomeViewModel.onEvent(PatientEvent.SelectDoctor(it))
//                    toggleBottomBar()
//                },
//                getSpecializationNameId = {
//                    ClinicIdToNameMapUseCase.specializationIdToNameId(it)
//                }
//            )
        } else {
            clinicsList(
                list = patientState.currentClinicList
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
        itemsIndexed(patientState.categoryList) { index, category ->
            Card(
                shape = RoundedShape40,
                backgroundColor = if (patientState.selectedCategory == index)
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
                                color = if (patientState.selectedCategory == index)
                                    Color.White
                                else Color.Black
                            ),
                            modifier = Modifier
                                .size(CARD_IMAGE_SIZE)
                                .padding(bottom = 10.dp)
                        )
                    } else {
                        SubcomposeAsyncImage(
                            model = category.imageRequest,
                            contentDescription = "Service icon",
                            loading = {
                                CircularProgressIndicator(color = Color.Black)
                            },
                            colorFilter = ColorFilter.tint(
                                color = if (patientState.selectedCategory == index)
                                    Color.White
                                else Color.Black
                            ),
                            modifier = Modifier
                                .size(CARD_IMAGE_SIZE)
                                .padding(bottom = 10.dp)
                        )
                    }
                    val categoryNameId = getCategoryNameId(category.id)
                    Text(
                        text = if (categoryNameId != null)
                            stringResource(id = categoryNameId)
                        else "",
                        fontFamily = Quicksand,
                        fontWeight = FontWeight.SemiBold,
                        color = if (patientState.selectedCategory == index)
                            Color.White
                        else Color.Black
                    )
                }
            }
        }
    }
}

fun LazyListScope.clinicsList(
    list: VisibilityList<ClinicForPatient>
) {
    animatedItemsIndexed(
        items = list,
        enter = fadeIn(tween(300)) +
                slideInHorizontally(
                    animationSpec = tween(300),
                    initialOffsetX = { -it / 2 }
                ),
        exit = fadeOut(tween(300)) +
                slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { it / 2 }
                ),
        exitDuration = 300,
        animateItemPlacementSpec = tween(500)
    ) { index, clinic ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            var doctorListExpanded by rememberSaveable { mutableStateOf(false) }
            val arrowRotateAnimation by animateFloatAsState(
                targetValue = if (doctorListExpanded) 180f else 0f,
                animationSpec = tween(durationMillis = 200)
            )
            val listExpandAnimation by animateDpAsState(
                targetValue = if (doctorListExpanded) 200.dp else 0.dp,
                animationSpec = tween(durationMillis = 200)
            )
            Card(
                shape = RoundedShape20,
                backgroundColor = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(100.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        doctorListExpanded = !doctorListExpanded
                    }
            ) {
                Box {
                    Text(
                        text = clinic.name,
                        fontFamily = Quicksand,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colors.textOnSecondary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    IconButton(
                        onClick = { doctorListExpanded = !doctorListExpanded },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .graphicsLayer {
                                rotationZ = arrowRotateAnimation
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "",
                            tint = MaterialTheme.colors.textOnSecondary
                        )
                    }
                }
            }
            LazyColumn(
                contentPadding = PaddingValues(start = 32.dp, top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(listExpandAnimation))
            {
                itemsIndexed(items = clinic.doctors) { index, doctor ->
                    Card(
                        shape = RoundedShape20,
                        backgroundColor = MaterialTheme.colors.secondary,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(100.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
//                            SubcomposeAsyncImage(
//                                model = doctor.imageRequest,
//                                contentDescription = "Doctor image",
//                                loading = {
//                                    CircularProgressIndicator(
//                                        color = MaterialTheme.colors.textOnSecondary,
//                                        modifier = Modifier.size(CARD_IMAGE_SIZE)
//                                    )
//                                },
//                                modifier = Modifier
//                                    .padding(start = 10.dp)
//                                    .clip(RoundedShape20)
//                                    .size(CARD_IMAGE_SIZE)
//                            )
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
//                                Text(
//                                    text = stringResource(
//                                        id = getSpecializationNameId(doctor.specializationId)
//                                    ),
//                                    fontFamily = Quicksand,
//                                    fontSize = 16.sp,
//                                    color = MaterialTheme.colors.textOnSecondary
//                                )
                            }
                        }
                    }
                }
            }
        }
    }
}