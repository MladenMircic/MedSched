package rs.ac.bg.etf.diplomski.medsched.domain.model.request

import rs.ac.bg.etf.diplomski.medsched.domain.model.business.WorkDay

data class DoctorRegisterRequest(
    val email: String,
    var password: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val categoryId: Int,
    val specializationId: Int,
    val workDays: List<WorkDay>
)