package rs.ac.bg.etf.diplomski.medsched.presentation.patient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Patient
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.EmailChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.PasswordChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.*
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.authentication.LogoutUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.UpdateEmailUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.UpdatePasswordUseCase
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.events.EditProfileEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.states.EditProfileState
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase,
    private val updateEmailUseCase: UpdateEmailUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val userFlow = getUserUseCase.userFlow

    private val _editProfileState = MutableStateFlow(EditProfileState())
    val editProfileState = _editProfileState.asStateFlow()

    init {
        viewModelScope.launch {
            userFlow.collect { user ->
                _editProfileState.update {
                    it.copy(newEmail = user?.email ?: "")
                }
            }
        }
    }

    fun onEvent(editProfileEvent: EditProfileEvent) {
        when (editProfileEvent) {
            is EditProfileEvent.ChangeEmailText -> {
                _editProfileState.update {
                    it.copy(newEmail = editProfileEvent.text)
                }
            }
            is EditProfileEvent.ConfirmNewEmail -> {
                val emailResult = EmailValidation.validate(_editProfileState.value.newEmail)

                _editProfileState.update {
                    it.copy(
                        newEmailErrorId = emailResult.errorId
                    )
                }

                if (emailResult.errorId == null) {
                    updateEmail()
                }
            }
            is EditProfileEvent.ResetUpdateEmailMessage -> {
               _editProfileState.update { it.copy(updateMessageId = null) }
            }
            is EditProfileEvent.ChangeOldPasswordText -> {
                _editProfileState.update {
                    it.copy(oldPassword = editProfileEvent.text)
                }
            }
            is EditProfileEvent.ChangeNewPasswordText -> {
                _editProfileState.update {
                    it.copy(newPassword = editProfileEvent.text)
                }
            }
            is EditProfileEvent.ChangeConfirmNewPasswordText -> {
                _editProfileState.update {
                    it.copy(confirmNewPassword = editProfileEvent.text)
                }
            }
            is EditProfileEvent.ConfirmNewPassword -> {
                val editProfileState = _editProfileState.value
                val oldPasswordResult = FormValidation().validate(editProfileState.oldPassword)
                val newPasswordResult = PasswordValidation.validate(
                    editProfileState.newPassword
                )
                val confirmPasswordResult = ConfirmPasswordEqualsValidation.validate(
                    editProfileState.confirmNewPassword, editProfileState.newPassword
                )

                val hasError = listOf(
                    oldPasswordResult,
                    newPasswordResult,
                    confirmPasswordResult
                ).any { it.errorId != null }

                _editProfileState.update {
                    it.copy(
                        oldPasswordErrorId = oldPasswordResult.errorId,
                        newPasswordErrorId = newPasswordResult.errorId,
                        confirmNewPasswordErrorId = confirmPasswordResult.errorId
                    )
                }

                if (!hasError) {
                    updatePassword()
                }
            }
        }
    }

    private fun updateEmail() = viewModelScope.launch {
        val response = updateEmailUseCase(
            EmailChangeRequest(email = _editProfileState.value.newEmail)
        )

        response.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    _editProfileState.update {
                        it.copy(
                            updateMessageId = resource.data,
                            isInProgress = false
                        )
                    }
                    userFlow.collect { user ->
                        val patient = user!! as Patient
                        updateUserUseCase(patient.copy(email = _editProfileState.value.newEmail))
                    }
                }
                is Resource.Error -> {
                    _editProfileState.update {
                        it.copy(
                            updateMessageId = resource.message,
                            isInProgress = false
                        )
                    }
                }
                is Resource.Loading -> {
                    _editProfileState.update { it.copy(isInProgress = true) }
                }
            }
        }
    }

    private fun updatePassword() = viewModelScope.launch {
        val editProfileState = _editProfileState.value
        val response = updatePasswordUseCase(
            PasswordChangeRequest(
                oldPassword = editProfileState.oldPassword,
                newPassword = editProfileState.newPassword
            )
        )

        response.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    _editProfileState.update {
                        it.copy(
                            updateMessageId = R.string.password_update_success,
                            isInProgress = false
                        )
                    }
                }
                is Resource.Error -> {
                    if (resource.message == R.string.password_error_login) {
                        _editProfileState.update {
                            it.copy(
                                oldPasswordErrorId = resource.message,
                                isInProgress = false
                            )
                        }
                    } else {
                        _editProfileState.update {
                            it.copy(
                                updateMessageId = R.string.password_update_failure,
                                isInProgress =  false
                            )
                        }
                    }
                }
                is Resource.Loading -> {
                    _editProfileState.update { it.copy(isInProgress = true) }
                }
            }
        }
    }
}