package rs.ac.bg.etf.diplomski.medsched.presentation.patient.stateholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
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
import javax.inject.Inject

@HiltViewModel
class PatientHomeViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase,
    private val getServicesUseCase: GetServicesUseCase,
    private val imageRequestUseCase: ImageRequestUseCase,
    private val getDoctorsUseCase: GetDoctorsUseCase,
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
        getAllServices()
        getDoctorsForPatient()
    }

    fun onEvent(patientEvent: PatientEvent) {
        when (patientEvent) {
            is PatientEvent.SelectService -> {
                _patientState.update { it.copy(selectedService = patientEvent.index) }
                getDoctorsForPatient()
            }
            is PatientEvent.SearchTextChange -> {
                _patientState.update { it.copy(searchKeyWord = patientEvent.text) }
            }
            is PatientEvent.SearchForDoctor -> {
                _patientState.update { it.copy(doctorsLoading = true) }
                val patientState = _patientState.value
                val allDoctorList = patientState.allDoctorList
                _patientState.update {
                    it.copy(
                        currentDoctorList = allDoctorList.filter { doctor ->
                            "${doctor.firstName} ${doctor.lastName}"
                                .contains(patientState.searchKeyWord, ignoreCase = true)
                        },
                        doctorsLoading = false
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
            is PatientEvent.GetAllServices -> {
                getAllServices()
            }
            is PatientEvent.ScheduleAppointment -> {
                scheduleAppointment()
            }
            PatientEvent.SetScheduleMessageNull -> {
                _appointmentState.update { it.copy(scheduledMessageId = null) }
            }
        }
    }

    fun getSelectedDoctor(): DoctorForPatient =
        _patientState.value.allDoctorList[_patientState.value.selectedDoctor ?: 0]

    fun fetchScheduledAppointments() = viewModelScope.launch {
        val response = getScheduledAppointmentsUseCase(
            AppointmentRequest(
                doctorId = 1,
                date = _appointmentState.value.selectedDate
            )
        )

        response.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    val scheduledTimes = resource.data!!.map {
                            appointment ->  appointment.time
                    }
                    _appointmentState.update {
                        it.copy(
                            availableTimes = (0..12)
                                .toList().map { hour ->
                                    LocalTime(hour = 8 + hour, minute = 0)
                                }.filter { availableTime ->
                                    !scheduledTimes.any { scheduledTime ->
                                        scheduledTime == availableTime
                                    }
                                }
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
        when (val resource = getServicesForDoctorUseCase(1)) {
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
        val patientState = _patientState.value
        val serviceCategory: String = if (patientState.selectedService != null)
            "${patientState.categoryList[patientState.selectedService].id}"
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
                    _patientState.update {
                        it.copy(
                            allDoctorList = resource.data,
                            currentDoctorList = resource.data,
                            doctorsLoading = false
                        )
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {
                    _patientState.update { it.copy(doctorsLoading = true) }
                }
            }
        }
    }

    private fun getAllServices() = viewModelScope.launch {
        val response = getServicesUseCase()

        response.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data.let {
                        _patientState.update { patientState ->
                            patientState.copy(categoryList = resource.data!!.toMutableList().also {
                                it.add(0, Category(id = 0, name = "All"))
                            })
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