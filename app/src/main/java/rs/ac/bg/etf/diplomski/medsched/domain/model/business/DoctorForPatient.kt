package rs.ac.bg.etf.diplomski.medsched.domain.model.business

import coil.request.ImageRequest

data class DoctorForPatient(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val service: String,
    var imageRequest: ImageRequest? = null
)