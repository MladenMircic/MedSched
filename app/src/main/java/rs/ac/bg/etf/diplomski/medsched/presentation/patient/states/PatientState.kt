package rs.ac.bg.etf.diplomski.medsched.presentation.patient.states

import rs.ac.bg.etf.diplomski.medsched.domain.model.business.DoctorForPatient
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Service

data class PatientState(
    val serviceList: List<Service> = listOf(),
    val selectedService: Int? = null,
    val searchKeyWord: String = "",
    val doctorList: List<DoctorForPatient> = listOf(),
    val selectedDoctor: Int? = null,
    val doctorsLoading: Boolean = false
)
