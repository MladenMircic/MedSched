package rs.ac.bg.etf.diplomski.medsched.presentation.patient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.repository.PatientRepository
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.GetServicesUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.ImageRequestUseCase
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.events.PatientEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.states.PatientState
import javax.inject.Inject

@HiltViewModel
class PatientViewModel @Inject constructor(
    patientRepository: PatientRepository,
    private val getServicesUseCase: GetServicesUseCase,
    private val imageRequestUseCase: ImageRequestUseCase
): ViewModel() {

    val userFlow = patientRepository.user

    private val _patientState = MutableStateFlow(PatientState())
    val patientState = _patientState.asStateFlow()

    init { getAllServices() }

    fun onEvent(patientEvent: PatientEvent) {
        when (patientEvent) {
            is PatientEvent.SelectService -> {
                _patientState.update { it.copy(selectedService = patientEvent.index) }
            }
            is PatientEvent.SearchTextChange -> {
                _patientState.update { it.copy(searchKeyWord = patientEvent.text) }
            }
            is PatientEvent.SearchForDoctor -> {

            }
            is PatientEvent.GetAllServices -> {
                getAllServices()
            }
        }
    }

    private fun getAllServices() = viewModelScope.launch {
        val response = getServicesUseCase()

        response.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    for (service in resource.data!!) {
                        service.imageRequestBuilder = withContext(Dispatchers.IO) {
                            imageRequestUseCase("${service.name}.png")
                        }
                    }
                    resource.data.let {
                        _patientState.update { it.copy(serviceList = resource.data) }
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