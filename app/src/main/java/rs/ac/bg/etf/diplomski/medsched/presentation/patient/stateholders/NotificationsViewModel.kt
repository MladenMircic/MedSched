package rs.ac.bg.etf.diplomski.medsched.presentation.patient.stateholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.NotificationsUseCase
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val notificationsUseCase: NotificationsUseCase
): ViewModel() {

    val notificationsFlow = notificationsUseCase.notificationsFlow

    fun updateNotifications(readIndices: List<Int>) = viewModelScope.launch {
        val notifications = notificationsFlow.first().filterIndexed { index, notification ->
            readIndices.contains(index) && !notification.read
        }
        notifications.forEach { notification ->
            notification.read = true
        }
        notificationsUseCase.updateNotifications(notifications)
    }
}