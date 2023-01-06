package rs.ac.bg.etf.diplomski.medsched.commons

object Constants {
    const val BASE_URL = "https://desolate-mesa-63349.herokuapp.com"
    const val AUTHENTICATION_ENDPOINTS = "authentication"
    const val PATIENT_ENDPOINTS = "patient"
    const val SERVICE_ICONS_URL = "$BASE_URL/clinic-images"
    val SKIP_HEADER_APPEND_PATHS = listOf("login", "register")
    const val AUTO_LOGOUT_TASK_NAME = "auto_logout"
}