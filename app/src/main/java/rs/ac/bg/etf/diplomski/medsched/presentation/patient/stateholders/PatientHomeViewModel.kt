package rs.ac.bg.etf.diplomski.medsched.presentation.patient.stateholders

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Category
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.DoctorForPatient
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.GetUserUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.ImageRequestUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.NotificationsUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.GetCategoriesPatientUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.GetClinicsUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.GetDoctorsUseCase
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.events.PatientEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.states.PatientState
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.animated
import javax.inject.Inject

@HiltViewModel
class PatientHomeViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase,
    private val getCategoriesPatientUseCase: GetCategoriesPatientUseCase,
    private val imageRequestUseCase: ImageRequestUseCase,
    private val getDoctorsUseCase: GetDoctorsUseCase,
    private val getClinicsUseCase: GetClinicsUseCase,
    private val notificationsUseCase: NotificationsUseCase
): ViewModel() {

    val userFlow = getUserUseCase.userFlow
    val notificationsFlow = notificationsUseCase.notificationsFlow

    private val _patientState = MutableStateFlow(PatientState())
    val patientState = _patientState.asStateFlow()

    init {
        viewModelScope.launch {
            notificationsFlow.collect { notifications ->
                _patientState.update {
                    it.copy(
                        newNotificationCount = notifications.count { notification ->
                            !notification.read
                        }
                    )
                }
            }
        }
        getAllCategories()
        getDoctorsForPatient()
    }

    fun onEvent(patientEvent: PatientEvent) {
        when (patientEvent) {
            is PatientEvent.SelectCategory -> {
                if (_patientState.value.selectedCategory != patientEvent.index) {
                    _patientState.update { state ->
                        if (state.showingDoctors) {
                            getDoctorsForPatient()
                            state.copy(
                                selectedCategory = patientEvent.index,
                                currentDoctorList = state.currentDoctorList.also { list ->
                                    list.clear(true)
                                }
                            )
                        } else {
                            getClinicsForPatient()
                            state.copy(
                                selectedCategory = patientEvent.index,
                                currentClinicList = state.currentClinicList.also { list ->
                                    list.clear(true)
                                }
                            )
                        }
                    }
                }
            }
            is PatientEvent.SearchTextChange -> {
                _patientState.update { it.copy(searchKeyWord = patientEvent.text) }
            }
            is PatientEvent.SearchForDoctor -> {
                _patientState.update { it.copy(dataLoading = true) }
                val patientState = _patientState.value
                val allDoctorList = patientState.allDoctorList
                _patientState.update {
                    it.copy(
                        currentDoctorList = allDoctorList.filter { doctor ->
                            "${doctor.firstName} ${doctor.lastName}"
                                .contains(patientState.searchKeyWord, ignoreCase = true)
                        }.toMutableStateList().animated,
                        dataLoading = false
                    )
                }
            }
            is PatientEvent.SelectDoctor -> {
                _patientState.update { it.copy(selectedDoctor = patientEvent.index) }
            }
            is PatientEvent.GetAllCategories -> {
                getAllCategories()
            }
            is PatientEvent.UpdateNotificationsRead -> {
                updateNotifications(patientEvent.indices)
            }
            PatientEvent.ToggleDoctorsClinics -> {
                _patientState.update { state ->
                    if (state.showingDoctors) {
                        getClinicsForPatient()
                    } else {
                        getDoctorsForPatient()
                    }
                    state.copy(showingDoctors = !state.showingDoctors)
                }
            }
            PatientEvent.ClearCurrentShowingList -> {
                if (_patientState.value.showingDoctors) {
                    _patientState.update { state ->
                        state.copy(
                            currentDoctorList = state.currentDoctorList.also { list ->
                                list.clear(animated = true)
                            }
                        )
                    }
                } else {
                    _patientState.update { state ->
                        state.copy(
                            currentClinicList = state.currentClinicList.also { list ->
                                list.clear(animated = true)
                            }
                        )
                    }
                }
            }
        }
    }

    fun getCategoryIdsAsString(): String =
        _patientState.value.categoryList
            .joinToString(separator = ",") { it.id.toString() }

    fun getSelectedDoctor(): DoctorForPatient =
        _patientState.value.allDoctorList[_patientState.value.selectedDoctor ?: 0]

    private fun getDoctorsForPatient() = viewModelScope.launch {
        delay(300L)
        val patientState = _patientState.value
        val serviceCategory: String = if (patientState.selectedCategory != null)
            "${patientState.categoryList[patientState.selectedCategory].id}"
        else ""
        val response = getDoctorsUseCase("", serviceCategory)

        response.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    for (doctor in resource.data!!) {
                        doctor.imageRequest = withContext(Dispatchers.IO) {
                            imageRequestUseCase("/doctors/Doctor.png")
                        }
                    }
                    _patientState.update { state ->
                        state.copy(
                            allDoctorList = resource.data,
                            currentDoctorList = resource.data.toMutableStateList().animated,
                            dataLoading = false
                        )
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {
                    _patientState.update { it.copy(dataLoading = true) }
                }
            }
        }
    }

    private fun getClinicsForPatient() = viewModelScope.launch {
        delay(300L)
        val patientState = _patientState.value
        val serviceCategory: String = if (patientState.selectedCategory != null)
            "${patientState.categoryList[patientState.selectedCategory].id}"
        else ""
        val response = getClinicsUseCase(serviceCategory)

        response.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    _patientState.update { state ->
                        state.copy(
                            currentClinicList = resource.data!!.toMutableStateList().animated,
                            dataLoading = false
                        )
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {
                    _patientState.update { it.copy(dataLoading = true) }
                }
            }
        }
    }

    private fun getAllCategories() = viewModelScope.launch {
        val response = getCategoriesPatientUseCase()

        response.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data.let {
                        _patientState.update { patientState ->
                            patientState.copy(
                                categoryList = resource.data!!.toMutableList().also {
                                    it.add(0, Category(id = 0, name = "All"))
                                },
                                selectedCategory = 0
                            )
                        }
                    }
                    for (service in resource.data!!) {
                        service.imageRequest = withContext(Dispatchers.IO) {
                            imageRequestUseCase("/services/${service.name}.png")
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

    private fun updateNotifications(visibleIndices: List<Int>) = viewModelScope.launch {
        val notifications = notificationsFlow.first().filterIndexed { index, notification ->
            visibleIndices.contains(index) && !notification.read
        }
        notifications.forEach { notification ->
            notification.read = true
        }
        notificationsUseCase.updateNotifications(notifications)
    }
}