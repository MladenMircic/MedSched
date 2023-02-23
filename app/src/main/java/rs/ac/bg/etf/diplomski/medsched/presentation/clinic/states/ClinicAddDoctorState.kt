package rs.ac.bg.etf.diplomski.medsched.presentation.clinic.states

import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Category
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.WorkDay

data class ClinicAddDoctorState(
    val email: String = "",
    val emailError: Int? = null,
    val firstName: String = "",
    val firstNameError: Int? = null,
    val lastName: String = "",
    val lastNameError: Int? = null,
    val password: String = "",
    val passwordError: Int? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: Int? = null,
    val phone: String = "",
    val phoneError: Int? = null,
    val categoryList: List<Category> = listOf(),
    val selectedCategory: Int? = null,
    val selectedSpecialization: Int? = null,
    val workDaysList: List<WorkDay> = listOf(),
    val selectedWorkDay: Int? = null,
    val selectedWorkDaysList: List<WorkDay> = listOf(),
    var messageId: Int? = null,
    var hasError: Boolean = false
)