package rs.ac.bg.etf.diplomski.medsched.presentation.patient.states

import androidx.compose.runtime.mutableStateListOf
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.DoctorForPatient
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.VisibilityList

data class SearchState(
    val fetchedDoctorList: List<DoctorForPatient> = listOf(),
    val filteredDoctorList: VisibilityList<DoctorForPatient> = VisibilityList(mutableStateListOf()),
    val searchText: String = "",
    val tabs: List<Int> = listOf(
        R.string.doctors_list,
        R.string.clinics_list
    ),
    val filterCategories: List<Int> = listOf(0),
    val filterApplied: Boolean = false,
    val dataLoading: Boolean = false,
    val selectedPage: Int = 0,
    val selectedDoctor: Int? = null,
    val multipleDoctorsIndexList: List<Int> = listOf(),
    val isSelectingMode: Boolean = false
)