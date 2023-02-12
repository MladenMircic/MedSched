package rs.ac.bg.etf.diplomski.medsched.presentation.clinic.stateholders

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Category
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Clinic
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.WorkDay
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.DoctorRegisterRequest
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.ClinicIdToNameMapUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.GetUserUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.clinic.GetCategoriesClinicUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.clinic.RegisterDoctorUseCase
import rs.ac.bg.etf.diplomski.medsched.presentation.clinic.events.ClinicAddDoctorEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.clinic.states.ClinicAddDoctorState
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ClinicAddDoctorViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase,
    private val clinicIdToNameMapUseCase: ClinicIdToNameMapUseCase,
    private val getCategoriesClinicUseCase: GetCategoriesClinicUseCase,
    private val registerDoctorUseCase: RegisterDoctorUseCase
): ViewModel() {

    val userFlow = getUserUseCase.userFlow
    private val _addDoctorState = MutableStateFlow(ClinicAddDoctorState())
    val addDoctorState = _addDoctorState.asStateFlow()

    init {
        initWorkDaysLists()
        getAllCategories()
    }

    private fun initWorkDaysLists() {
        viewModelScope.launch {
            userFlow.collect { user ->
                val clinic: Clinic = user as Clinic
                val workDays: MutableList<WorkDay> = mutableListOf()
                val selectedWorkDays: MutableList<WorkDay> = mutableListOf()
                val workHours: List<LocalTime> = (0 until clinic.workHours).map {
                    LocalTime(clinic.openingTime.hour + it, 0, 0)
                }
                for (dayNumber in (1..5)) {
                    val dayOfWeek: DayOfWeek = DayOfWeek.of(dayNumber)
                    workDays.add(
                        WorkDay(
                            dayOfWeek = dayOfWeek,
                            workHours = workHours
                        )
                    )
                    selectedWorkDays.add(WorkDay(dayOfWeek = dayOfWeek))
                }
                _addDoctorState.update {
                    it.copy(
                        workDaysList = workDays,
                        selectedWorkDaysList = selectedWorkDays
                    )
                }
            }
        }
    }

    fun onEvent(clinicAddDoctorEvent: ClinicAddDoctorEvent) {
        when (clinicAddDoctorEvent) {
            is ClinicAddDoctorEvent.SetEmailField -> {
                _addDoctorState.update { it.copy(email = clinicAddDoctorEvent.text) }
            }
            is ClinicAddDoctorEvent.SetFirstNameField -> {
                _addDoctorState.update { it.copy(firstName = clinicAddDoctorEvent.text) }
            }
            is ClinicAddDoctorEvent.SetLastNameField -> {
                _addDoctorState.update { it.copy(lastName = clinicAddDoctorEvent.text) }
            }
            is ClinicAddDoctorEvent.SetPasswordField -> {
                _addDoctorState.update { it.copy(password = clinicAddDoctorEvent.text) }
            }
            is ClinicAddDoctorEvent.SetConfirmPasswordField -> {
                _addDoctorState.update { it.copy(confirmPassword = clinicAddDoctorEvent.text) }
            }
            is ClinicAddDoctorEvent.SetPhoneField -> {
                _addDoctorState.update { it.copy(phone = clinicAddDoctorEvent.text) }
            }
            is ClinicAddDoctorEvent.SetCategoryField -> {
                _addDoctorState.update { it.copy(selectedCategory = clinicAddDoctorEvent.index) }
            }
            is ClinicAddDoctorEvent.SetSpecializationField -> {
                _addDoctorState.update { it.copy(selectedSpecialization = clinicAddDoctorEvent.index) }
            }
            is ClinicAddDoctorEvent.SetSelectedWorkDay -> {
                _addDoctorState.update { it.copy(selectedWorkDay = clinicAddDoctorEvent.index) }
            }
            is ClinicAddDoctorEvent.RegisterDoctor -> {
                val addDoctorValue = _addDoctorState.value
                registerDoctor(DoctorRegisterRequest(
                    email = addDoctorValue.email,
                    password = addDoctorValue.password,
                    firstName = addDoctorValue.firstName,
                    lastName = addDoctorValue.lastName,
                    phone = addDoctorValue.phone,
                    categoryId = addDoctorValue.categoryList[addDoctorValue.selectedCategory!!].id,
                    specializationId = 1,
                    workDays = addDoctorValue.selectedWorkDaysList
                ))
            }
        }
    }

    private fun getAllCategories() = viewModelScope.launch {
        val response = getCategoriesClinicUseCase()

        response.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data.let {
                        _addDoctorState.update { addDoctorState ->
                            addDoctorState.copy(categoryList = resource.data!!)
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

    private fun registerDoctor(
        doctorRegisterRequest: DoctorRegisterRequest
    ) = viewModelScope.launch {
        val response = registerDoctorUseCase(doctorRegisterRequest)

        response.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    _addDoctorState.update {
                        ClinicAddDoctorState(messageId = R.string.doctor_register_success)
                    }
                    initWorkDaysLists()
                }
                is Resource.Error -> {}
                is Resource.Loading -> {}
            }
        }
    }

    fun categoryToNameId(category: Category): Int? =
        clinicIdToNameMapUseCase.categoryIdToNameId(categoryId = category.id)

    fun getSelectedCategoryNameId(): Int? {
        val addDoctorValue = _addDoctorState.value
        return addDoctorValue.selectedCategory?.let {
            clinicIdToNameMapUseCase.categoryIdToNameId(
                categoryId = addDoctorValue.categoryList[it].id
            )
        }
    }

    fun getWorkHoursForDay(dayIndex: Int): List<LocalTime> =
        _addDoctorState.value.workDaysList[dayIndex].workHours

    fun toggleHourForWorkDay(workHour: LocalTime): Boolean {
        var added = false
        val addDoctorValue = _addDoctorState.value
        addDoctorValue.selectedWorkDay?.let {
            _addDoctorState.update { addDoctorState ->
                addDoctorState.copy(
                    selectedWorkDaysList = addDoctorState
                        .selectedWorkDaysList
                        .toMutableList()
                        .also { workTimeList ->
                            val currentDay = workTimeList[it]
                            if (currentDay.workHours.contains(workHour)) {
                                currentDay.workHours = currentDay
                                    .workHours
                                    .toMutableList()
                                    .also { workHoursList ->
                                        workHoursList.remove(workHour)
                                    }
                            } else {
                                currentDay.workHours = currentDay
                                    .workHours
                                    .toMutableList()
                                    .also { workHoursList ->
                                        workHoursList.add(workHour)
                                    }
                                added = true
                            }
                        }
                )
            }
        }
        return added
    }

    fun isHourSelected(workHour: LocalTime): Boolean {
        val addDoctorValue = _addDoctorState.value
        return addDoctorValue.selectedWorkDay?.let {
            addDoctorValue
                .selectedWorkDaysList[it]
                .workHours
                .contains(workHour)
        } ?: false
    }
}