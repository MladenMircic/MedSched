package rs.ac.bg.etf.diplomski.medsched.domain.model.response

import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Appointment
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.DoctorWorkTime

data class AvailableTimesResponse(
    val doctorWorkTimes: List<DoctorWorkTime>,
    val scheduledAppointments: List<Appointment>
)