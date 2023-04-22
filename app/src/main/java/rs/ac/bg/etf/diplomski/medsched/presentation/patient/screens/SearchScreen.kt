package rs.ac.bg.etf.diplomski.medsched.presentation.patient.screens

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.CARD_IMAGE_SIZE
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.DoctorForPatient
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.ClinicIdToNameMapUseCase
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.SearchHome
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.SearchToSchedule
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.events.SearchEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.stateholders.SearchViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.*
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel(),
    categoryIds: List<Int>,
    onBack: () -> Unit,
) {
    val navController = rememberAnimatedNavController()
    val density = LocalDensity.current
    AnimatedNavHost(
        navController = navController,
        startDestination = SearchHome.route,
        enterTransition = { SharedYAxisEnterTransition(density) },
        exitTransition = { SharedYAxisExitTransition(density) },
        popEnterTransition = { SharedYAxisPopEnterTransition(density) },
        popExitTransition = { SharedYAxisPopExitTransition(density) }
    ) {
        composable(route = SearchHome.route) {
            val modalBottomSheetState = rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Hidden,
                confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded },
                skipHalfExpanded = true
            )
            val coroutineScope = rememberCoroutineScope()

            val searchState by searchViewModel.searchState.collectAsState()
            val keyboardController = LocalSoftwareKeyboardController.current
            val focusRequester = remember { FocusRequester() }
            val focusManager = LocalFocusManager.current

            LaunchedEffect(true) {
                focusRequester.requestFocus()
            }

            LaunchedEffect(key1 = modalBottomSheetState.isVisible) {
                if (!modalBottomSheetState.isVisible && !searchState.filterApplied) {
                    searchViewModel.onEvent(SearchEvent.ClearFilter)
                }
            }

            ModalBottomSheetLayout(
                sheetState = modalBottomSheetState,
                sheetShape = RoundedShape20.copy(
                    bottomStart = ZeroCornerSize,
                    bottomEnd = ZeroCornerSize
                ),
                sheetContent = {
                    Surface(
                        color = MaterialTheme.colors.secondary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(380.dp)
                    ) {
                        Column {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(top = 12.dp)
                            ) {
                                val primaryColor = MaterialTheme.colors.primary
                                Canvas(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    drawLine(
                                        start = Offset(x = size.width / 2 - 50f, y = 0f),
                                        end = Offset(x = size.width / 2 + 50f, y = 0f),
                                        color = primaryColor,
                                        strokeWidth = 6.dp.toPx(),
                                        cap = StrokeCap.Round
                                    )
                                }
                                Spacer(modifier = Modifier.padding(top = 20.dp))
                                Text(
                                    text = stringResource(id = R.string.filter),
                                    fontFamily = Quicksand,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 24.sp,
                                    color = MaterialTheme.colors.textOnSecondary
                                )
                                Divider(
                                    color = MaterialTheme.colors.primary,
                                    thickness = 2.dp,
                                    modifier = Modifier
                                        .fillMaxWidth(0.9f)
                                        .padding(vertical = 10.dp)
                                )
                            }
                            Column(
                                modifier = Modifier.padding(start = 18.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.categories_list),
                                    fontFamily = Quicksand,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colors.textOnSecondary
                                )
                                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                                FlowRow(
                                    mainAxisSpacing = 8.dp,
                                    crossAxisSpacing = 10.dp,
                                ) {
                                    categoryIds.forEach { categoryId ->
                                        val isSelected by remember(searchState.filterCategories) {
                                            derivedStateOf {
                                                searchViewModel.isCategorySelected(categoryId)
                                            }
                                        }
                                        Surface(
                                            shape = RoundedShape20,
                                            color = if (isSelected)
                                                MaterialTheme.colors.primary
                                            else MaterialTheme.colors.secondary,
                                            border = BorderStroke(3.dp, MaterialTheme.colors.primary)
                                        ) {
                                            Text(
                                                text = stringResource(
                                                    id = ClinicIdToNameMapUseCase
                                                        .categoryIdToNameId(categoryId)!!
                                                ),
                                                fontFamily = Quicksand,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 16.sp,
                                                color = if (isSelected)
                                                    MaterialTheme.colors.textOnPrimary
                                                else  MaterialTheme.colors.textOnSecondary,
                                                modifier = Modifier
                                                    .padding(
                                                        start = 20.dp,
                                                        end = 20.dp,
                                                        top = 4.dp,
                                                        bottom = 8.dp
                                                    )
                                                    .clickable(
                                                        interactionSource = remember {
                                                            MutableInteractionSource()
                                                        },
                                                        indication = null
                                                    ) {
                                                        searchViewModel.onEvent(
                                                            SearchEvent.SetCategoryFilter(categoryId)
                                                        )
                                                    }
                                            )
                                        }
                                    }
                                }
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 12.dp)
                            ) {
                                Divider(
                                    color = MaterialTheme.colors.primary,
                                    thickness = 2.dp,
                                    modifier = Modifier
                                        .fillMaxWidth(0.9f)
                                        .padding(vertical = 10.dp)
                                )
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(top = 10.dp)
                                ) {
                                    val isListEmpty by remember {
                                        derivedStateOf{
                                            searchState.filterCategories.contains(0)
                                                    && searchState.filterCategories.size == 1
                                        }
                                    }
                                    val resetButtonWidth by animateFloatAsState(
                                        targetValue = if (isListEmpty) 0f else 0.4f,
                                        animationSpec = tween(durationMillis = 300)
                                    )
                                    val resetButtonAlpha by animateFloatAsState(
                                        targetValue = if (isListEmpty) 0f else 1f,
                                        animationSpec = tween(durationMillis = 200)
                                    )
                                    val applyButtonWidth by animateFloatAsState(
                                        targetValue = if (isListEmpty) 0.9f else 0.8f,
                                        animationSpec = tween(durationMillis = 300)
                                    )
                                    Button(
                                        onClick = {
                                            searchViewModel.onEvent(SearchEvent.ClearFilter)
                                        },
                                        shape = RoundedShape40,
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Color.Gray,
                                            contentColor = MaterialTheme.colors.textOnSecondary
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth(resetButtonWidth)
                                            .fillMaxHeight(0.7f)
                                            .alpha(resetButtonAlpha)
                                    ) {
                                        Text(
                                            text = stringResource(id = R.string.reset_filter),
                                            fontFamily = Quicksand,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 18.sp,
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        )
                                    }
                                    Button(
                                        onClick = {
                                            coroutineScope.launch {
                                                searchViewModel.onEvent(SearchEvent.ApplyFilter)
                                                modalBottomSheetState.hide()
                                            }
                                        },
                                        shape = RoundedShape40,
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = MaterialTheme.colors.selectable,
                                            contentColor = MaterialTheme.colors.textOnSecondary
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth(applyButtonWidth)
                                            .fillMaxHeight(0.7f)
                                    ) {
                                        Text(
                                            text = stringResource(id = R.string.apply_filter),
                                            fontFamily = Quicksand,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 18.sp,
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                scrimColor = Color.Black.copy(alpha = 0.32f),
                modifier = Modifier.animateEnterExit(
                    enter = FadeThroughEnterTransition,
                    exit = FadeThroughExitTransition
                )
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .background(color = MaterialTheme.colors.primary)
                            .fillMaxSize()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp, start = 16.dp)
                        ) {
                            AnimatedContent(
                                targetState = searchState.isSelectingMode
                            ) { state ->
                                when(state) {
                                    true -> {
                                        IconButton(
                                            onClick = {
                                                searchViewModel.onEvent(
                                                    SearchEvent.SetSelectingMode(false)
                                                )
                                            },
                                            modifier = Modifier.animateEnterExit(
                                                enter = FadeThroughEnterTransition,
                                                exit = FadeThroughExitTransition
                                            )
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "",
                                                tint = MaterialTheme.colors.textOnPrimary,
                                                modifier = Modifier.size(30.dp)
                                            )
                                        }
                                    }
                                    false -> {
                                        IconButton(
                                            onClick = {
                                                keyboardController?.hide()
                                                onBack()
                                            },
                                            modifier = Modifier.animateEnterExit(
                                                enter = FadeThroughEnterTransition,
                                                exit = FadeThroughExitTransition
                                            )
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.back),
                                                contentDescription = "",
                                                tint = MaterialTheme.colors.textOnPrimary,
                                                modifier = Modifier.size(25.dp)
                                            )
                                        }
                                    }
                                }
                            }


                            OutlinedTextField(
                                value = searchState.searchText,
                                onValueChange = {
                                    searchViewModel.onEvent(SearchEvent.UpdateSearchTextAndTriggerSearch(it))
                                },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.search),
                                        fontFamily = Quicksand
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = ""
                                    )
                                },
                                trailingIcon = {
                                    IconButton(
                                        onClick = {
                                            coroutineScope.launch {
                                                if (!modalBottomSheetState.isVisible) {
                                                    focusManager.clearFocus()
                                                    keyboardController?.hide()
                                                    modalBottomSheetState.show()
                                                }
                                            }
                                        },
                                        modifier = Modifier.padding(end = 6.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.filter),
                                            contentDescription = "",
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                },
                                shape = RoundedShape20,
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = MaterialTheme.colors.textOnSecondary,
                                    leadingIconColor = MaterialTheme.colors.textOnSecondary,
                                    trailingIconColor = MaterialTheme.colors.selectable,
                                    backgroundColor = MaterialTheme.colors.secondary,
                                    focusedIndicatorColor = MaterialTheme.colors.selectable,
                                    unfocusedIndicatorColor = MaterialTheme.colors.secondary,
                                    focusedLabelColor = MaterialTheme.colors.selectable,
                                    unfocusedLabelColor = MaterialTheme.colors.textOnSecondary,
                                    cursorColor = MaterialTheme.colors.textOnSecondary
                                ),
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        keyboardController?.hide()
                                    }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth(0.95f)
                                    .padding(start = 8.dp, bottom = 5.dp)
                                    .focusRequester(focusRequester)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp)
                        ) {
                            TabRow(
                                backgroundColor = MaterialTheme.colors.primary,
                                selectedTabIndex = searchState.selectedPage,
                                indicator = { tabPositions ->
                                    CustomIndicator(
                                        tabPositions = tabPositions,
                                        currentPage = searchState.selectedPage
                                    )
                                },
                                divider = {},
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .height(70.dp)
                                    .align(Alignment.Center)
                            ) {
                                searchState.tabs.forEachIndexed { index, tab ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .zIndex(2f)
                                    ) {
                                        Text(
                                            text = stringResource(id = tab),
                                            fontFamily = Quicksand,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 24.sp,
                                            color = if (searchState.selectedPage != index)
                                                MaterialTheme.colors.textOnPrimary
                                            else MaterialTheme.colors.textOnSecondary,
                                            modifier = Modifier
                                                .padding(bottom = 8.dp)
                                                .align(Alignment.Center)
                                                .clickable(
                                                    interactionSource = remember { MutableInteractionSource() },
                                                    indication = null
                                                ) {
                                                    searchViewModel.onEvent(
                                                        SearchEvent.SetSelectedPage(
                                                            index
                                                        )
                                                    )
                                                }
                                        )
                                    }

                                }
                            }
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.search_results),
                                    fontFamily = Quicksand,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 30.sp,
                                    color = MaterialTheme.colors.textOnPrimary,
                                    modifier = Modifier.weight(1f)
                                )
                                AnimatedVisibility(
                                    visible = searchState.isSelectingMode,
                                    enter = slideInHorizontally(
                                        initialOffsetX = { it }
                                    ) + fadeIn(animationSpec = tween(durationMillis = 200)),
                                    exit = slideOutHorizontally(
                                        targetOffsetX = { it }
                                    ) + fadeOut(animationSpec = tween(durationMillis = 200))
                                ) {
                                    Text(
                                        text = pluralStringResource(
                                            id = R.plurals.doctors_selected,
                                            searchState.multipleDoctorsIndexList.size,
                                            searchState.multipleDoctorsIndexList.size
                                        ),
                                        fontFamily = Quicksand,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 18.sp,
                                        color = MaterialTheme.colors.textOnPrimary,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            }
                            AnimatedContent(targetState = searchState.selectedPage) { page ->
                                when (page) {
                                    0 -> {
                                        LazyColumn(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(14.dp),
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .animateEnterExit(
                                                    enter = slideInHorizontally(
                                                        initialOffsetX = { -it }
                                                    ),
                                                    exit = slideOutHorizontally(
                                                        targetOffsetX = { -it }
                                                    )
                                                )
                                        ) {
                                            doctorsList(
                                                list = searchState.filteredDoctorList,
                                                doctorsLoading = searchState.dataLoading,
                                                onDoctorSelectedForSchedule = {
                                                    searchViewModel.onEvent(
                                                        SearchEvent.SelectDoctorForSchedule(it)
                                                    )
                                                    navController.navigate(SearchToSchedule.route)
                                                },
                                                isSelectingMode = searchState.isSelectingMode,
                                                setSelectingMode = {
                                                    searchViewModel.onEvent(
                                                        SearchEvent.SetSelectingMode(true)
                                                    )
                                                },
                                                multipleDoctorsIndices = searchState.multipleDoctorsIndexList,
                                                toggleDoctorSelected = {
                                                    searchViewModel.onEvent(
                                                        SearchEvent.ToggleMultipleDoctorSelected(it)
                                                    )
                                                },
                                                getSpecializationNameId = {
                                                    ClinicIdToNameMapUseCase.specializationIdToNameId(it)
                                                }
                                            )
                                        }
                                    }
                                    1 -> {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .animateEnterExit(
                                                    enter = slideInHorizontally(
                                                        initialOffsetX = { it }
                                                    ),
                                                    exit = slideOutHorizontally(
                                                        targetOffsetX = { it }
                                                    )
                                                )
                                        ) {
                                            Text(text = "TEXT2", modifier = Modifier.align(Alignment.Center))
                                        }
                                    }
                                }
                            }
                        }
                    }
                    AnimatedVisibility(
                        visible = searchState.isSelectingMode
                                && searchState.multipleDoctorsIndexList.isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.15f)
                            .align(Alignment.BottomCenter),
                        enter = slideInVertically(
                            initialOffsetY = { it }
                        ) + fadeIn(animationSpec = tween(durationMillis = 200)),
                        exit = slideOutVertically(
                            targetOffsetY = { it }
                        ) + fadeOut(animationSpec = tween(durationMillis = 200))
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colors.primaryVariant.copy(alpha = 0.1f)
                                )
                        ) {
                            Button(
                                onClick = { navController.navigate(SearchToSchedule.route) },
                                shape = RoundedShape20,
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = MaterialTheme.colors.selectable
                                ),
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .fillMaxHeight(0.6f)
                                    .align(Alignment.Center)
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

            BackHandler {
                if (modalBottomSheetState.isVisible) {
                    coroutineScope.launch {
                        modalBottomSheetState.hide()
                    }
                } else if (searchState.isSelectingMode) {
                    searchViewModel.onEvent(SearchEvent.SetSelectingMode(false))
                } else {
                    onBack()
                }
            }
        }
        composable(route = SearchToSchedule.route) {
            val doctors by remember {
                derivedStateOf {
                    searchViewModel.getSelectedDoctors()
                }
            }

            DoctorAppointmentScreen(
                doctors = doctors,
                onBackPressed = {
                    searchViewModel.onEvent(SearchEvent.ClearSelection)
                    navController.popBackStack()
                },
                modifier = Modifier.animateEnterExit(
                    enter = FadeThroughEnterTransition,
                    exit = FadeThroughExitTransition
                )
            )
        }
    }
}

fun LazyListScope.doctorsList(
    list: VisibilityList<DoctorForPatient>,
    doctorsLoading: Boolean,
    onDoctorSelectedForSchedule: (Int) -> Unit,
    isSelectingMode: Boolean,
    multipleDoctorsIndices: List<Int>,
    setSelectingMode: () -> Unit,
    toggleDoctorSelected: (Int) -> Unit,
    getSpecializationNameId: (Int) -> Int
) {
    if (!doctorsLoading) {
        animatedItemsIndexed(
            items = list,
            key = { _, doctor -> doctor.id },
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
        ) { index, doctor ->
            val hapticFeedback = LocalHapticFeedback.current
            var isPressed by remember { mutableStateOf(false) }
            val scaleAnimation by animateFloatAsState(
                targetValue = if (isPressed) 0.92f else 1f
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Card(
                    shape = RoundedShape20,
                    backgroundColor = MaterialTheme.colors.secondary,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(100.dp)
                        .align(Alignment.Center)
                        .graphicsLayer {
                            scaleX = scaleAnimation
                            scaleY = scaleAnimation
                        }
                        .pointerInput(key1 = isSelectingMode) {
                            detectTapGestures(
                                onPress = {
                                    try {
                                        isPressed = true
                                        awaitRelease()
                                    } finally {
                                        isPressed = false
                                        if (isSelectingMode) {
                                            hapticFeedback.performHapticFeedback(
                                                HapticFeedbackType.LongPress
                                            )
                                            toggleDoctorSelected(index)
                                        }
                                    }
                                },
                                onLongPress = {
                                    if (!isSelectingMode) {
                                        hapticFeedback.performHapticFeedback(
                                            HapticFeedbackType.LongPress
                                        )
                                        setSelectingMode()
                                        toggleDoctorSelected(index)
                                    }
                                },
                                onTap = {
                                    if (!isSelectingMode) {
                                        onDoctorSelectedForSchedule(index)
                                    }
                                }
                            )
                        }
                ) {
                    Box {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxSize()
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
                                modifier = Modifier
                                    .padding(start = 18.dp)
                            ) {
                                Text(
                                    text = doctor.firstName,
                                    fontFamily = Quicksand,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colors.textOnSecondary
                                )
                                Text(
                                    text = doctor.lastName,
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
                            val selectionBubbleAnimation by animateDpAsState(
                                targetValue = if (isSelectingMode) 25.dp else 0.dp,
                                animationSpec = tween(durationMillis = 100)
                            )
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Surface(
                                    color = Color.White.copy(alpha = 0.3f),
                                    shape = CircleShape,
                                    modifier = Modifier
                                        .size(selectionBubbleAnimation)
                                        .align(Alignment.Center)
                                ) {
                                    if (multipleDoctorsIndices.contains(index)) {
                                        ItemSelectedIcon()
                                    }
                                }
                            }
                        }
                    }
                }
                if (isSelectingMode && multipleDoctorsIndices.contains(index)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(100.dp)
                            .align(Alignment.Center)
                            .graphicsLayer {
                                scaleX = scaleAnimation
                                scaleY = scaleAnimation
                            }
                            .clip(shape = RoundedShape20)
                            .background(color = Color.White.copy(alpha = 0.1f))

                    )
                }
            }
        }
    } else {
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillParentMaxWidth()
            ) {
                HorizontalDotLoader(color = Blue85)
            }
        }
    }
}

@Composable
private fun CustomIndicator(tabPositions: List<TabPosition>, currentPage: Int) {
    val transition = updateTransition(currentPage, label = "")
    val indicatorStart by transition.animateDp(
        transitionSpec = {
            if (initialState < targetState) {
                spring(dampingRatio = 1f, stiffness = 500f)
            } else {
                spring(dampingRatio = 1f, stiffness = 2000f)
            }
        }, label = ""
    ) {
        tabPositions[it].left
    }

    val indicatorEnd by transition.animateDp(
        transitionSpec = {
            if (initialState < targetState) {
                spring(dampingRatio = 1f, stiffness = 2000f)
            } else {
                spring(dampingRatio = 1f, stiffness = 500f)
            }
        }, label = ""
    ) {
        tabPositions[it].right
    }

    Box(
        Modifier
            .offset(x = indicatorStart)
            .wrapContentSize(align = Alignment.TopStart)
            .width(indicatorEnd - indicatorStart)
            .padding(6.dp)
            .fillMaxSize()
            .background(color = MaterialTheme.colors.secondary, RoundedShape40)
            .zIndex(1f)
    )
}