package rs.ac.bg.etf.diplomski.medsched.domain.repository

import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Category
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.DoctorRegisterRequest

interface ClinicRepository {

    suspend fun getAllCategories(): List<Category>
    suspend fun registerDoctor(doctorRegisterRequest: DoctorRegisterRequest)
}