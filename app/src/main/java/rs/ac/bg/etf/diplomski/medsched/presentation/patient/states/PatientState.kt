package rs.ac.bg.etf.diplomski.medsched.presentation.patient.states

import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Service

data class PatientState(
    val serviceList: List<Service> = listOf()
)
