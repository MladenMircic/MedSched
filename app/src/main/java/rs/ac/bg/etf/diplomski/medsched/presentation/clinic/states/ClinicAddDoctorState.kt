package rs.ac.bg.etf.diplomski.medsched.presentation.clinic.states

import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Category
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.WorkDay

data class ClinicAddDoctorState(
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val phone: String = "",
    val categoryList: List<Category> = listOf(),
    val selectedCategory: Int? = null,
    val selectedSpecialization: Int? = null,
    val workDaysList: List<WorkDay> = listOf(),
    val selectedWorkDay: Int? = null,
    val selectedWorkDaysList: List<WorkDay> = listOf(),
    var messageId: Int? = null
)