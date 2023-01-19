package rs.ac.bg.etf.diplomski.medsched.domain.use_case

import rs.ac.bg.etf.diplomski.medsched.R
import javax.inject.Inject

class ClinicIdToNameMapUseCase @Inject constructor() {

    private val categoriesMap = mapOf(
        0 to R.string.service_all,
        1 to R.string.category_brain,
        2 to R.string.category_glands,
        3 to R.string.category_heart,
        4 to R.string.category_kidneys,
        5 to R.string.category_stomach
    )

    private val specializationMap = mapOf(
        1 to R.string.spec_gastroenterologist
    )

    private val servicesMap = mapOf(
        1 to R.string.service_specialist,
        2 to R.string.service_specialist,
        3 to R.string.service_specialist,
        4 to R.string.service_specialist,
        5 to R.string.service_abdominal_ultrasound,
        6 to R.string.service_gastroscopy,
        7 to R.string.service_colonoscopy,
        8 to R.string.service_heart_ultrasound
    )

    fun categoryIdToNameId(categoryId: Int): Int? = categoriesMap[categoryId]
    fun specializationIdToNameId(specializationId: Int): Int = specializationMap[specializationId]!!
    fun serviceIdToNameId(serviceId: Int): Int = servicesMap[serviceId]!!
}