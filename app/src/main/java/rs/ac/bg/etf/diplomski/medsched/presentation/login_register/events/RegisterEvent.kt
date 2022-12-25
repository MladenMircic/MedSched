package rs.ac.bg.etf.diplomski.medsched.presentation.login_register.events

import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.stateholders.RegisterViewModel

sealed class RegisterEvent {
    data class FieldChange(
        val registerField: RegisterViewModel.RegisterField,
        val value: String): RegisterEvent()
    object Submit: RegisterEvent()
}