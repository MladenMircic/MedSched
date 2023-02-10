package rs.ac.bg.etf.diplomski.medsched.presentation.doctor.stateholders

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentForDoctor
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.ClinicIdToNameMapUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.GetUserUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.ImageRequestUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.doctor.GetAllAppointmentsForDoctorUseCase
import rs.ac.bg.etf.diplomski.medsched.presentation.doctor.states.DoctorState
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class DoctorHomeViewModel @Inject constructor(
    imageRequestUseCase: ImageRequestUseCase,
    getAllAppointmentsForDoctorUseCase: GetAllAppointmentsForDoctorUseCase,
    getUserUseCase: GetUserUseCase,
    private val clinicIdToNameMapUseCase: ClinicIdToNameMapUseCase
): ViewModel() {

    val userFlow = getUserUseCase.userFlow
    val appointmentsForDoctorFlow: Flow<List<AppointmentForDoctor>> =
        getAllAppointmentsForDoctorUseCase.appointmentsForDoctorFlow

    private val _doctorState = MutableStateFlow(DoctorState())
    val doctorState = _doctorState.asStateFlow()

    init {
        viewModelScope.launch {
            _doctorState.update {
                it.copy(
                    doctorImageRequest = withContext(Dispatchers.IO) {
                        imageRequestUseCase("/doctors/Doctor.png")
                    }
                )
            }
        }
    }

    fun serviceIdToNameId(serviceId: Int): Int =
        clinicIdToNameMapUseCase.serviceIdToNameId(serviceId = serviceId)
}