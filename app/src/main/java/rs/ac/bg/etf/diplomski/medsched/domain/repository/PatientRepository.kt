package rs.ac.bg.etf.diplomski.medsched.domain.repository

import kotlinx.coroutines.flow.Flow
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.User

interface PatientRepository {

    val user: Flow<User?>
}