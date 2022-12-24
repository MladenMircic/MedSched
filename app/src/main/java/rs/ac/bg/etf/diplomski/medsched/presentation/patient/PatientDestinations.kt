package rs.ac.bg.etf.diplomski.medsched.presentation.patient

sealed class PatientDestinations(val route: String) {
    object PatientMainScreen: PatientDestinations("patient_main_screen")
}