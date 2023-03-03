package rs.ac.bg.etf.diplomski.medsched.presentation.patient.stateholders

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Appointment
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Category
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.DoctorForPatient
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.AppointmentRequest
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.ClinicIdToNameMapUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.GetUserUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.ImageRequestUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.*
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.events.PatientEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.states.AppointmentState
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
    private val getScheduledAppointmentsUseCase: GetScheduledAppointmentsUseCase,
    private val getServicesForDoctorUseCase: GetServicesForDoctorUseCase,
    private val scheduleAppointmentUseCase: ScheduleAppointmentUseCase,
    private val clinicIdToNameMapUseCase: ClinicIdToNameMapUseCase
): ViewModel() {

    val userFlow = getUserUseCase.userFlow

    private val _patientState = MutableStateFlow(PatientState())
    val patientState = _patientState.asStateFlow()

    private val _appointmentState = MutableStateFlow(AppointmentState())
    val appointmentState = _appointmentState.asStateFlow()

    init {
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
            is PatientEvent.SetAppointmentExamNameId -> {
                _appointmentState.update { it.copy(currentExamNameId = patientEvent.nameId) }
            }
            is PatientEvent.SetAppointmentDate -> {
                _appointmentState.update {
                    it.copy(selectedDate = patientEvent.date ?: LocalDate.now())
                }
            }
            is PatientEvent.SetAppointmentTime -> {
                _appointmentState.update {
                    it.copy(selectedTime = patientEvent.timeIndex)
                }
            }
            is PatientEvent.SelectDoctor -> {
                _patientState.update { it.copy(selectedDoctor = patientEvent.index) }
            }
            is PatientEvent.GetAllCategories -> {
                getAllCategories()
            }
            is PatientEvent.ScheduleAppointment -> {
                scheduleAppointment()
            }
            PatientEvent.SetScheduleMessageNull -> {
                _appointmentState.update { it.copy(scheduledMessageId = null) }
            }
            PatientEvent.ClearAvailableHours -> {
                _appointmentState.update { it.copy(
                    selectedDate = LocalDate.now()
                ) }
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

    fun getSelectedDoctor(): DoctorForPatient =
        _patientState.value.allDoctorList[_patientState.value.selectedDoctor ?: 0]

    fun fetchAvailableHoursForDate() = viewModelScope.launch {
        val patientStateValue = _patientState.value
        val response = getScheduledAppointmentsUseCase(
            AppointmentRequest(
                doctorId = patientStateValue.allDoctorList[patientStateValue.selectedDoctor!!].id,
                date = _appointmentState.value.selectedDate
            )
        )

        response.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    _appointmentState.update {
                        it.copy(
                            availableTimes = resource.data!!,
                            selectedTime = -1
                        )
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
            }
        }
    }

    fun fetchServicesForDoctor() = viewModelScope.launch {
        val patientStateValue = _patientState.value
        when (val resource = getServicesForDoctorUseCase(
            patientStateValue.allDoctorList[patientStateValue.selectedDoctor!!].id
        )) {
            is Resource.Success -> {
                val serviceList = resource.data!!
                _appointmentState.update { state ->
                    state.copy(
                        serviceList = serviceList,
                        examNameIdList = serviceList.map {
                            clinicIdToNameMapUseCase.serviceIdToNameId(it.id)
                        },
                        currentExamNameId = clinicIdToNameMapUseCase
                            .serviceIdToNameId(serviceList[0].id)
                    )
                }
            }
            is Resource.Error -> TODO()
            is Resource.Loading -> TODO()
        }
    }

    fun categoryIdToNameId(categoryId: Int): Int? =
        clinicIdToNameMapUseCase.categoryIdToNameId(categoryId)

    fun specializationIdToNameId(specializationId: Int): Int =
        clinicIdToNameMapUseCase.specializationIdToNameId(specializationId)

    private fun getDoctorsForPatient() = viewModelScope.launch {
        delay(300L)
        val patientState = _patientState.value
        val serviceCategory: String = if (patientState.selectedCategory != null)
            "${patientState.categoryList[patientState.selectedCategory].id}"
        else ""
        val response = getDoctorsUseCase(serviceCategory)

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

    private fun scheduleAppointment() = viewModelScope.launch {
        val appointmentInfo = _appointmentState.value
        val selectedTime = appointmentInfo.availableTimes[appointmentInfo.selectedTime]
        userFlow.collect { user ->
            val response = scheduleAppointmentUseCase(Appointment(
                date = appointmentInfo.selectedDate,
                time = selectedTime,
                doctorId = getSelectedDoctor().id,
                patientId = user!!.id,
                examId = appointmentInfo.serviceList[
                    appointmentInfo.examNameIdList.indexOf(appointmentInfo.currentExamNameId)
                ].id,
                confirmed = true,
                cancelledBy = -1
            ))

            response.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _appointmentState.update {
                            it.copy(
                                availableTimes = it.availableTimes
                                    .filter { time ->
                                        time != selectedTime
                                    },
                                selectedTime = -1,
                                scheduledMessageId = R.string.schedule_success
                            )
                        }
                    }
                    is Resource.Error -> {
                        _appointmentState.update {
                            it.copy(scheduledMessageId = R.string.schedule_failure)
                        }
                    }
                    is Resource.Loading -> {

                    }
                }
            }
        }
    }
}