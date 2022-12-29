package rs.ac.bg.etf.diplomski.medsched.presentation.patient.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import kotlinx.coroutines.launch
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.CARD_IMAGE_SIZE
import rs.ac.bg.etf.diplomski.medsched.presentation.composables.defaultButtonColors
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.PatientViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.events.PatientEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.states.PatientState
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.*

@Composable
fun PatientHomeScreen(
    patientViewModel: PatientViewModel = hiltViewModel()
) {
    val user by patientViewModel.userFlow.collectAsState(initial = null)
    val patientState by patientViewModel.patientState.collectAsState()

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
                colorFilter = ColorFilter.tint(color = MaterialTheme.colors.textOnPrimary),
                contentScale = ContentScale.Fit
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
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
                        onValueChange = { patientViewModel.onEvent(PatientEvent.SearchTextChange(it)) },
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
                onItemSelected = {
                    patientViewModel.onEvent(PatientEvent.SelectService(it))
                }
            )
        }
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
    onItemSelected: (Int) -> Unit
) {

    val rememberLazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }

    Text(
        text = stringResource(id = R.string.categories),
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
        itemsIndexed(patientState.serviceList) { index, service ->
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
                        onItemSelected(index)
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
                        model = service.imageRequestBuilder?.build(),
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