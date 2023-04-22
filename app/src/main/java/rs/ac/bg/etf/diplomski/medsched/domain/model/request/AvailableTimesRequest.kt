package rs.ac.bg.etf.diplomski.medsched.domain.model.request


data class AvailableTimesRequest(
    val doctorIds: List<String>,
    val patientId: String
)