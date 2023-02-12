package rs.ac.bg.etf.diplomski.medsched.presentation.clinic.stateholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Category
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.GetUserUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.clinic.GetCategoriesClinicUseCase
import rs.ac.bg.etf.diplomski.medsched.presentation.clinic.events.ClinicHomeEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.clinic.states.ClinicHomeState
import javax.inject.Inject

@HiltViewModel
class ClinicHomeViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase,
    private val getCategoriesClinicUseCase: GetCategoriesClinicUseCase
): ViewModel() {

    val userFlow = getUserUseCase.userFlow
    private val _clinicHomeState = MutableStateFlow(ClinicHomeState())
    val clinicState = _clinicHomeState.asStateFlow()

    init {
        getAllCategories()
    }

    fun onEvent(clinicHomeEvent: ClinicHomeEvent) {
        when (clinicHomeEvent) {
            is ClinicHomeEvent.SearchTextChange -> {
                _clinicHomeState.update { it.copy(searchKeyWord = clinicHomeEvent.text) }
            }
            is ClinicHomeEvent.CategorySelect -> {
                _clinicHomeState.update { it.copy(selectedCategory = clinicHomeEvent.index) }
            }
            is ClinicHomeEvent.SearchForDoctor -> {

            }
        }
    }

    private fun getAllCategories() = viewModelScope.launch {
        val response = getCategoriesClinicUseCase()

        response.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data.let {
                        _clinicHomeState.update { clinicState ->
                            clinicState.copy(categoryList = resource.data!!.toMutableList().also {
                                it.add(0, Category(id = 0, name = "All"))
                            })
                        }
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
            }
        }
    }
}