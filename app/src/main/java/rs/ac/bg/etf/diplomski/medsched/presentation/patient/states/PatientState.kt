package rs.ac.bg.etf.diplomski.medsched.presentation.patient.states

import rs.ac.bg.etf.diplomski.medsched.presentation.patient.PatientHome

data class PatientState(
    val routeSelected: String = PatientHome.route
)
