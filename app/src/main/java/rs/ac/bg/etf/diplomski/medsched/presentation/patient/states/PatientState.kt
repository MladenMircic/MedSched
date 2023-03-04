package rs.ac.bg.etf.diplomski.medsched.presentation.patient.states

import androidx.compose.runtime.mutableStateListOf
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Category
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.ClinicForPatient
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.DoctorForPatient
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.VisibilityList

data class PatientState(
    val categoryList: List<Category> = listOf(),
    val selectedCategory: Int? = null,
    val searchKeyWord: String = "",
    val allDoctorList: List<DoctorForPatient> = listOf(),
    val currentDoctorList: VisibilityList<DoctorForPatient> = VisibilityList(mutableStateListOf()),
    val currentClinicList: VisibilityList<ClinicForPatient> = VisibilityList(mutableStateListOf()),
    val selectedDoctor: Int? = null,
    val dataLoading: Boolean = false,
    val showingDoctors: Boolean = true,
    val newNotificationCount: Int = 0
)
