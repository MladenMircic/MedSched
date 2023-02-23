package rs.ac.bg.etf.diplomski.medsched.presentation.clinic.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Clinic
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.User
import rs.ac.bg.etf.diplomski.medsched.presentation.clinic.events.ClinicHomeEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.clinic.stateholders.ClinicHomeViewModel
import rs.ac.bg.etf.diplomski.medsched.presentation.composables.SearchField
import rs.ac.bg.etf.diplomski.medsched.presentation.ui.theme.*

@Composable
fun ClinicHomeScreen(
    clinicHomeViewModel: ClinicHomeViewModel = hiltViewModel()
) {
    val user: User? by clinicHomeViewModel.userFlow.collectAsState(initial = null)
    val clinic: Clinic? = user as Clinic?
    val clinicState by clinicHomeViewModel.clinicState.collectAsState()

    Column {
        Surface(
            color = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
            ) {
                TextButton(
                    onClick = {
                        clinicHomeViewModel.onEvent(ClinicHomeEvent.Logout)
                    }
                ) {
                    androidx.compose.material.Text(
                        text = stringResource(id = R.string.logout),
                        fontFamily = Quicksand,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.textOnPrimary,
                        modifier = Modifier.padding(bottom = 3.dp, end = 8.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Logout button icon",
                        tint = MaterialTheme.colors.textOnPrimary
                    )
                }
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            clinic?.let {
                Text(
                    text = it.name,
                    fontFamily = Quicksand,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 30.sp,
                    color = MaterialTheme.colors.textOnPrimary
                )
                SearchField(
                    searchKeyWord = clinicState.searchKeyWord,
                    onKeyWordChange = { keyWord ->
                        clinicHomeViewModel.onEvent(ClinicHomeEvent.SearchTextChange(keyWord))
                    },
                    label = R.string.doctor_search,
                    onSearchSubmit = { clinicHomeViewModel.onEvent(ClinicHomeEvent.SearchForDoctor) },
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .fillMaxWidth()
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    itemsIndexed(items = clinicState.categoryList) { index, category ->
                        val interactionSource = remember { MutableInteractionSource() }
                        Surface(
                            shape = RoundedShape20,
                            color = if (clinicState.selectedCategory == index)
                                MaterialTheme.colors.selectable
                            else MaterialTheme.colors.calendarField,
                            modifier = Modifier
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    clinicHomeViewModel.onEvent(ClinicHomeEvent.CategorySelect(index))
                                }
                        ) {
                            Text(
                                text = category.name,
                                fontFamily = Quicksand,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = if (clinicState.selectedCategory == index)
                                    Color.White
                                else MaterialTheme.colors.calendarFieldText,
                                modifier = Modifier.padding(horizontal = 26.dp, vertical = 16.dp)
                            )
                        }
                    }
                }

                LazyColumn {

                }
            }
        }
    }
}