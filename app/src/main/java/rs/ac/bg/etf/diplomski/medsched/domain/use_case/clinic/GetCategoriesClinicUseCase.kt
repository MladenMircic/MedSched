package rs.ac.bg.etf.diplomski.medsched.domain.use_case.clinic

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.internal.http.HTTP_UNAUTHORIZED
import okio.IOException
import retrofit2.HttpException
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Category
import rs.ac.bg.etf.diplomski.medsched.domain.repository.ClinicRepository
import javax.inject.Inject

class GetCategoriesClinicUseCase @Inject constructor(
    private val clinicRepository: ClinicRepository
) {

    operator fun invoke(): Flow<Resource<List<Category>>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(clinicRepository.getAllCategories()))
        } catch (e: HttpException) {
            if (e.code() == HTTP_UNAUTHORIZED) {
                emit(Resource.Error(-1))
            }
        } catch (e: IOException) {
            emit(Resource.Error(R.string.no_connection))
        }
    }
}