package rs.ac.bg.etf.diplomski.medsched.domain.model.business

import kotlinx.datetime.LocalTime

data class ClinicForPatient(
    val id: String,
    val email: String,
    val name: String,
    val openingTime: LocalTime,
    val workHours: Int,
    var doctors: List<DoctorForPatient>
)