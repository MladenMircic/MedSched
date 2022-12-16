package rs.ac.bg.etf.diplomski.medsched.domain.use_case

data class ValidationResult(
    val success: Boolean,
    val errorMessage: String? = null
)
