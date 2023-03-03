package rs.ac.bg.etf.diplomski.medsched.domain.model.business

import coil.request.ImageRequest

data class DoctorForPatient(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val categoryId: Int,
    val specializationId: Int,
    var imageRequest: ImageRequest? = null
)