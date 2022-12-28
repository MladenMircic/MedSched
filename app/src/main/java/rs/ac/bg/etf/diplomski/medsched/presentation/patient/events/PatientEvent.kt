package rs.ac.bg.etf.diplomski.medsched.presentation.patient.events

sealed class PatientEvent {
    object GetAllServices: PatientEvent()
}
