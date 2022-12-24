package rs.ac.bg.etf.diplomski.medsched.presentation.patient.states

import rs.ac.bg.etf.diplomski.medsched.presentation.patient.PatientMain

data class PatientState(
    val routeSelected: String = PatientMain.route
)
