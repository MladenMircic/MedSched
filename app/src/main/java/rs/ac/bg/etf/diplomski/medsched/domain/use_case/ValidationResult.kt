package rs.ac.bg.etf.diplomski.medsched.domain.use_case

data class ValidationResult(
    val success: Boolean,
    val errorId: Int? = null
)
