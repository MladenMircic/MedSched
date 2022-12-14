package rs.ac.bg.etf.diplomski.medsched.presentation.login_register.states

data class RegisterState(
    val email: String = "",
    val firstName: String = "",
    val firstNameError: Int? = null,
    val lastName: String = "",
    val lastNameError: Int? = null,
    val emailError: Int? = null,
    val password: String = "",
    val passwordError: Int? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: Int? = null,
    val phone: String = "",
    val phoneError: Int? = null,
    val ssn: String = "",
    val ssnError: Int? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val snackBarMessage: Int? = null
)