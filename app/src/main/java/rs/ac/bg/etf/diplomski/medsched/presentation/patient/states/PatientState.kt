package rs.ac.bg.etf.diplomski.medsched.presentation.patient.states

import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Category
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.DoctorForPatient

data class PatientState(
    val categoryList: List<Category> = listOf(),
    val selectedService: Int? = null,
    val searchKeyWord: String = "",
    val doctorList: List<DoctorForPatient> = listOf(),
    val selectedDoctor: Int? = null,
    val doctorsLoading: Boolean = false
)
