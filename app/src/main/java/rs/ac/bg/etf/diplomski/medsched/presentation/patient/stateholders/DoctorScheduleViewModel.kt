package rs.ac.bg.etf.diplomski.medsched.presentation.patient.stateholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.plus
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Appointment
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentInfo
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.DoctorForPatient
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Patient
import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.AppointmentForPatientEntity
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.AvailableTimesRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.response.AvailableTimesResponse
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.ClinicIdToNameMapUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.GetUserUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.NotificationsUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.GetScheduledAppointmentsUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.GetServicesForDoctorUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.ScheduleAppointmentUseCase
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.events.DoctorScheduleEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.states.AppointmentState
import javax.inject.Inject

@HiltViewModel
class DoctorScheduleViewModel @Inject constructor(
    private val getServicesForDoctorUseCase: GetServicesForDoctorUseCase,
    private val getScheduledAppointmentsUseCase: GetScheduledAppointmentsUseCase,
    private val scheduleAppointmentUseCase: ScheduleAppointmentUseCase,
    private val notificationsUseCase: NotificationsUseCase,
    getUserUseCase: GetUserUseCase,
): ViewModel() {

    private val _appointmentState = MutableStateFlow(AppointmentState())
    val appointmentState = _appointmentState.asStateFlow()

    private val userFlow = getUserUseCase.userFlow

    fun initializeLists(doctorIds: List<String>) {
        _appointmentState.update {
            it.copy(
                doctorIds = doctorIds,
                availableServicesList = List(
                    size = doctorIds.size,
                    init = { listOf() }
                ),
                appointmentInfoList = List(
                    size = doctorIds.size,
                    init = { AppointmentInfo() }
                )
            )
        }
        fetchAvailableTimesForDoctors(doctorIds)
    }

    fun onEvent(doctorScheduleEvent: DoctorScheduleEvent) {
        when (doctorScheduleEvent) {

            is DoctorScheduleEvent.SetSelectedDoctorIndex -> {
                _appointmentState.update { state ->
                    val newCurrentMonth =
                        if (state.appointmentInfoList[doctorScheduleEvent.index].selectedDate != null)
                            state.appointmentInfoList[doctorScheduleEvent.index].selectedDate?.month!!
                        else
                            state.currentMonth
                    state.copy(
                        appointmentInfoList = state.appointmentInfoList.toMutableList().also {
                            it[doctorScheduleEvent.index] = it[doctorScheduleEvent.index].copy(
                                isOnSelectedMonth = true
                            )
                        },
                        currentMonth = newCurrentMonth,
                        selectedDoctorIndex = doctorScheduleEvent.index,
                        availableDates = state.appointmentInfoList[doctorScheduleEvent.index]
                            .allMonthsDates[newCurrentMonth] ?: listOf(),
                        currentAvailableTimesList = state.allAvailableTimesList.filter {
                            it.doctorId == state.doctorIds[doctorScheduleEvent.index] &&
                                    it.dayOfWeek == state
                                        .appointmentInfoList[doctorScheduleEvent.index]
                                        .selectedDate
                                        ?.dayOfWeek &&
                                    state.scheduledAppointments.firstOrNull { appointment ->
                                        appointment.doctorId == it.doctorId &&
                                                appointment.date ==
                                                state.appointmentInfoList[doctorScheduleEvent.index]
                                                    .selectedDate &&
                                                appointment.time == it.time
                                    } == null
                        }.map { it.time }
                    )
                }
            }

            is DoctorScheduleEvent.ToggleSelectedService -> {
                _appointmentState.update { state ->
                    state.copy(
                        appointmentInfoList = state
                            .appointmentInfoList
                            .toMutableList()
                            .also { list ->
                                val updatedServicesList = list[state.selectedServicesIndex]
                                    .selectedServicesList
                                    .toMutableList()
                                    .also { currentDoctorServicesList ->
                                        if (currentDoctorServicesList
                                                .contains(doctorScheduleEvent.service)
                                        ) {
                                            currentDoctorServicesList.remove(
                                                doctorScheduleEvent.service
                                            )
                                        } else {
                                            currentDoctorServicesList.add(
                                                doctorScheduleEvent.service
                                            )
                                        }
                                    }
                                list[state.selectedServicesIndex] = list[state.selectedServicesIndex]
                                    .copy(
                                        selectedServicesList = updatedServicesList
                                    )
                            }
                    )
                }
            }

            is DoctorScheduleEvent.SetCurrentMonth -> {
                _appointmentState.update { state ->
                    state.copy(
                        appointmentInfoList = state.appointmentInfoList.toMutableList().also {
                            var selectedAppointmentInfo = it[state.selectedDoctorIndex]
                            selectedAppointmentInfo = selectedAppointmentInfo.copy(
                                isOnSelectedMonth = selectedAppointmentInfo.selectedDate == null ||
                                        doctorScheduleEvent.month == selectedAppointmentInfo.selectedDate?.month!!
                            )
                            it[state.selectedDoctorIndex] = selectedAppointmentInfo
                        },
                        currentMonth = doctorScheduleEvent.month,
                        availableDates = state
                            .appointmentInfoList[state.selectedDoctorIndex]
                            .allMonthsDates[doctorScheduleEvent.month]!!
                    )
                }
            }

            is DoctorScheduleEvent.SetAppointmentExamNameId -> {
                _appointmentState.update { it.copy(currentExamNameId = doctorScheduleEvent.nameId) }
            }

            is DoctorScheduleEvent.SetCurrentDate -> {
                _appointmentState.update { state ->
                    val date = doctorScheduleEvent.date ?: LocalDate.now()
                    state.copy(
                        appointmentInfoList = state.appointmentInfoList.toMutableList().also {
                            it[state.selectedDoctorIndex] = it[state.selectedDoctorIndex].copy(
                                selectedDate = date,
                                selectedTime = null,
                                isOnSelectedMonth = true
                            )
                        },
                        currentAvailableTimesList = state.allAvailableTimesList.filter {
                            it.doctorId == state.doctorIds[state.selectedDoctorIndex] &&
                                    it.dayOfWeek == date.dayOfWeek &&
                                    state.scheduledAppointments.firstOrNull { appointment ->
                                        appointment.doctorId == it.doctorId &&
                                                appointment.date == date &&
                                                appointment.time == it.time
                                    } == null
                        }.map { it.time }
                    )
                }
            }

            is DoctorScheduleEvent.SetAppointmentTime -> {
                _appointmentState.update { state ->
                    state.copy(
                        appointmentInfoList = state.appointmentInfoList.toMutableList().also {
                            it[state.selectedDoctorIndex] = it[state.selectedDoctorIndex].copy(
                                selectedTime = doctorScheduleEvent.time
                            )
                        },
                    )
                }
            }

            is DoctorScheduleEvent.ScheduleAppointment -> {
                scheduleAppointment(doctorScheduleEvent.doctors)
            }

            is DoctorScheduleEvent.SetScheduleMessageNull -> {
                _appointmentState.update { it.copy(scheduledMessageId = null) }
            }

            is DoctorScheduleEvent.SetServicesLoading -> {
                _appointmentState.update {
                    it.copy(servicesLoading = true)
                }
            }
        }
    }

    fun fetchServicesForDoctor(doctor: DoctorForPatient) = viewModelScope.launch {
        _appointmentState.update {
            it.copy(servicesLoading = true)
        }
        delay(500L)
        val appointmentStateValue = _appointmentState.value
        if (appointmentStateValue
                .availableServicesList[appointmentStateValue.selectedDoctorIndex].isNotEmpty()) {
            _appointmentState.update { state ->
                state.copy(
                    selectedServicesIndex = state.selectedDoctorIndex,
                    servicesLoading = false
                )
            }
            cancel()
        }
        when (val resource = getServicesForDoctorUseCase(doctor.id)) {
            is Resource.Success -> {
                val serviceList = resource.data!!
                _appointmentState.update { state ->
                    state.copy(
                        selectedServicesIndex = state.selectedDoctorIndex,
                        servicesLoading = false,
                        availableServicesList = state.availableServicesList.toMutableList().also {
                            it[state.selectedDoctorIndex] = serviceList
                        },
                        examNameIdList = serviceList.map {
                            ClinicIdToNameMapUseCase.serviceIdToNameId(it.id)
                        },
                        currentExamNameId = ClinicIdToNameMapUseCase
                            .serviceIdToNameId(serviceList[0].id)
                    )
                }
            }
            is Resource.Error -> TODO()
            is Resource.Loading -> {}
        }
    }

    private fun fetchAvailableTimesForDoctors(doctorIds: List<String>) = viewModelScope.launch {
        userFlow.collect { user ->
            val patient = user as Patient
            val response = getScheduledAppointmentsUseCase(
                AvailableTimesRequest(doctorIds, patient.id)
            )

            response.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val job = setAllAvailableDatesForDoctors(doctorIds, resource.data!!)
                        _appointmentState.update { state ->
                            state.copy(
                                scheduledAppointments = resource.data.scheduledAppointments,
                                allAvailableTimesList = resource.data.doctorWorkTimes
                            )
                        }
                        job.join()
                        onEvent(DoctorScheduleEvent.SetSelectedDoctorIndex(0))
                        onEvent(DoctorScheduleEvent.SetCurrentMonth(LocalDate.now().month))
                    }
                    is Resource.Error -> {

                    }
                    is Resource.Loading -> {

                    }
                }
            }
        }
    }

    private fun setAllAvailableDatesForDoctors(
        doctorIds: List<String>,
        availableTimesResponse: AvailableTimesResponse
    ) = viewModelScope.launch(Dispatchers.Default) {
        val allDoctorsDatesMap: MutableList<MutableMap<Month, List<LocalDate>>> = MutableList(
            doctorIds.size,
            init = { mutableMapOf() }
        )
        var listOfDates: MutableList<LocalDate> = mutableListOf()
        doctorIds.forEachIndexed { index, doctorId ->
            var currentDay: LocalDate = LocalDate.now()
            val currentYear: Int = currentDay.year
            var currentMonth: Month = currentDay.month
            while (currentDay.year == currentYear) {
                while (currentDay.month == currentMonth) {
                    if (availableTimesResponse.doctorWorkTimes.firstOrNull {
                            it.doctorId == doctorId && it.dayOfWeek == currentDay.dayOfWeek
                        } != null
                    ) {
                        listOfDates.add(currentDay)
                    }
                    currentDay = currentDay.plus(1, DateTimeUnit.DAY)
                }
                allDoctorsDatesMap[index][currentMonth] = listOfDates
                listOfDates = mutableListOf()
                currentMonth = currentDay.month
            }
        }
        _appointmentState.update { state ->
            state.copy(
                appointmentInfoList = state.appointmentInfoList.toMutableList().also { appointmentInfos ->
                    allDoctorsDatesMap.forEachIndexed { index, currentDateMap ->
                        appointmentInfos[index] = appointmentInfos[index].copy(
                            allMonthsDates = currentDateMap
                        )
                    }
                }
            )
        }
//        while (currentMonthDay.year == today.year) {
//            while (currentMonthDay.month == month) {
//                if (appointmentStateValue
//                        .allAvailableTimesList
//                        .firstOrNull {
//                            it.doctorId == doctorId && it.dayOfWeek == currentMonthDay.dayOfWeek
//                        } != null
//                ) {
//                    listOfDates.add(currentMonthDay)
//                }
//                currentMonthDay = currentMonthDay.plus(1, DateTimeUnit.DAY)
//            }
//        }
    }

    private fun scheduleAppointment(doctors: List<DoctorForPatient>) = viewModelScope.launch {
        val appointmentInfo = _appointmentState.value
        userFlow.collect { user ->
            val appointments = appointmentInfo.appointmentInfoList.mapIndexed { index, appointmentInfo ->
                Appointment(
                    date = appointmentInfo.selectedDate!!,
                    time = appointmentInfo.selectedTime!!,
                    doctorId = doctors[index].id,
                    patientId = user!!.id,
                    services = appointmentInfo.selectedServicesList,
                    confirmed = true,
                    cancelledBy = -1
                )
            }
            val response = scheduleAppointmentUseCase(appointments)

            response.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        scheduleAppointmentUseCase.inLocal(
                            appointments.mapIndexed { index, appointment ->
                                AppointmentForPatientEntity(
                                    date = appointment.date,
                                    time = appointment.time,
                                    appointmentId = resource.data!![index],
                                    doctorId = doctors[index].id,
                                    doctorName = "${doctors[index].firstName} ${doctors[index].lastName}",
                                    doctorSpecializationId = doctors[index].specializationId,
                                    patientId = user!!.id,
                                    confirmed = true,
                                    cancelledBy = -1
                                )
                            },
                            appointments.map {
                                it.services
                            }
                        )
                        _appointmentState.update {
                            it.copy(
                                scheduledMessageId = R.string.schedule_success
                            )
                        }
//                        val currentDateTime = Instant.fromEpochMilliseconds(Date().time)
//                            .toLocalDateTime(TimeZone.currentSystemDefault())
//                        notificationsUseCase.sendNotification(
//                            NotificationPatientEntity(
//                                type = NotificationType.SCHEDULED,
//                                doctorName = "${doctor.firstName} ${doctor.lastName}",
//                                dateOfAction = appointmentInfo.selectedDate,
//                                timeOfAction = selectedTime,
//                                dateNotified = currentDateTime.date,
//                                timeNotified = currentDateTime.time
//                            )
//                        )
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