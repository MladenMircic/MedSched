package rs.ac.bg.etf.diplomski.medsched.presentation.clinic.states

import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Category

data class ClinicHomeState(
    val searchKeyWord: String = "",
    val categoryList: List<Category> = listOf(),
    val selectedCategory: Int? = null
)