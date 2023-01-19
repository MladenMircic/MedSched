package rs.ac.bg.etf.diplomski.medsched.presentation.patient.stateholders

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
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.InfoChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.PasswordChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.*
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.UpdateEmailUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.UpdateInfoUseCase
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
    private val updateInfoUseCase: UpdateInfoUseCase
) : ViewModel() {

    private val userFlow = getUserUseCase.userFlow

    private val _editProfileState = MutableStateFlow(EditProfileState())
    val editProfileState = _editProfileState.asStateFlow()

    init {
        viewModelScope.launch {
            userFlow.collect { user ->
                val patient = user as Patient?
                _editProfileState.update {
                    it.copy(
                        newEmailText = patient?.email ?: "",
                        firstNameText = patient?.firstName ?: "",
                        lastNameText = patient?.lastName ?: "",
                        phoneText = patient?.phone ?: "",
                        ssnText = patient?.ssn ?: ""
                    )
                }
            }
        }
    }

    fun onEvent(editProfileEvent: EditProfileEvent) {
        when (editProfileEvent) {
            is EditProfileEvent.ChangeEmailText -> {
                _editProfileState.update {
                    it.copy(newEmailText = editProfileEvent.text)
                }
            }
            is EditProfileEvent.ConfirmNewEmail -> {
                val emailResult = EmailValidation.validate(_editProfileState.value.newEmailText)

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
                    it.copy(oldPasswordText = editProfileEvent.text)
                }
            }
            is EditProfileEvent.ChangeNewPasswordText -> {
                _editProfileState.update {
                    it.copy(newPasswordText = editProfileEvent.text)
                }
            }
            is EditProfileEvent.ChangeConfirmNewPasswordText -> {
                _editProfileState.update {
                    it.copy(confirmNewPasswordText = editProfileEvent.text)
                }
            }
            is EditProfileEvent.ConfirmNewPassword -> {
                val editProfileState = _editProfileState.value
                val oldPasswordResult = ValidateUseCase().validate(editProfileState.oldPasswordText)
                val newPasswordResult = PasswordValidation.validate(
                    editProfileState.newPasswordText
                )
                val confirmPasswordResult = ConfirmPasswordEqualsValidation.validate(
                    editProfileState.confirmNewPasswordText, editProfileState.newPasswordText
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


            is EditProfileEvent.ChangeFirstNameText -> {
                _editProfileState.update { it.copy(firstNameText = editProfileEvent.text) }
            }
            is EditProfileEvent.ChangeLastNameText -> {
                _editProfileState.update { it.copy(lastNameText = editProfileEvent.text) }
            }
            is EditProfileEvent.ChangePhoneText -> {
                _editProfileState.update { it.copy(phoneText = editProfileEvent.text) }
            }
            is EditProfileEvent.ChangeSSNText -> {
                _editProfileState.update { it.copy(ssnText = editProfileEvent.text) }
            }
            EditProfileEvent.ConfirmNewInfo -> {
                val editProfileState = _editProfileState.value
                val validateUseCase = ValidateUseCase()
                val firstNameResult = validateUseCase.validate(editProfileState.firstNameText)
                val lastNameResult = validateUseCase.validate(editProfileState.lastNameText)
                val phoneResult = PhoneValidation.validate(editProfileState.phoneText)
                val ssnResult = SSNValidation.validate(editProfileState.ssnText)

                val hasError = listOf(
                    firstNameResult,
                    lastNameResult,
                    phoneResult,
                    ssnResult
                ).any { it.errorId != null }

                _editProfileState.update { it.copy(
                    firstNameErrorId = firstNameResult.errorId,
                    lastNameErrorId = lastNameResult.errorId,
                    phoneErrorId = phoneResult.errorId,
                    ssnErrorId = ssnResult.errorId
                ) }

                if (!hasError) {
                    updateInfo()
                }
            }
        }
    }

    private fun updateEmail() = viewModelScope.launch {
        val response = updateEmailUseCase(
            EmailChangeRequest(email = _editProfileState.value.newEmailText)
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
                        updateUserUseCase(patient.copy(email = _editProfileState.value.newEmailText))
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
                oldPassword = editProfileState.oldPasswordText,
                newPassword = editProfileState.newPasswordText
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

    private fun updateInfo() = viewModelScope.launch {
        val editProfileState = _editProfileState.value
        val response = updateInfoUseCase(
            InfoChangeRequest(
                firstName = editProfileState.firstNameText,
                lastName = editProfileState.lastNameText,
                phone = editProfileState.phoneText,
                ssn = editProfileState.ssnText
            )
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
                        updateUserUseCase(
                            patient.copy(
                                firstName = editProfileState.firstNameText,
                                lastName = editProfileState.lastNameText,
                                phone = editProfileState.phoneText,
                                ssn = editProfileState.ssnText
                            )
                        )
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
                    _editProfileState.update {
                        it.copy(isInProgress = true)
                    }
                }
            }
        }
    }
}