package rs.ac.bg.etf.diplomski.medsched.commons

object Constants {
    const val BASE_URL = "https://desolate-mesa-63349.herokuapp.com"
    const val AUTHENTICATION_ENDPOINTS = "authentication"
    const val PATIENT_ENDPOINTS = "patient"
    const val DOCTOR_ENDPOINTS = "doctor"
    const val SERVICE_ICONS_URL = "$BASE_URL/clinic-images"
    val SKIP_HEADER_APPEND_PATHS = listOf("login", "register")
    const val AUTO_LOGOUT_TASK_NAME = "auto_logout"
    const val PATIENT_APPOINTMENT_FETCH_TASK_NAME = "patient_appointment_fetch"
    const val DOCTOR_APPOINTMENT_FETCH_TASK_NAME = "doctor_appointment_fetch"

    const val APPOINTMENT_CANCEL_CHANNEL_ID = "CANCELLED_APPOINTMENTS"
    const val APPOINTMENT_CANCEL_CHANNEL_NAME = "Cancelled appointments"
    const val APPOINTMENT_CANCEL_CHANNEL_DESCRIPTION =
        "Channel for notifying user about cancelled appointments"

    const val APPOINTMENT_CANCEL_NOTIFICATION_ID = 1
    const val PATIENT_SCHEDULED_DEEPLINK = "https://com.scheduled.patient"
    const val DOCTOR_SCHEDULED_DEEPLINK = "https://com.scheduled.doctor"
}