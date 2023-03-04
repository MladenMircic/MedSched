package rs.ac.bg.etf.diplomski.medsched.domain.use_case

import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.NotificationPatientEntity
import rs.ac.bg.etf.diplomski.medsched.domain.repository.PatientRepository
import javax.inject.Inject

class NotificationsUseCase @Inject constructor(
    private val patientRepository: PatientRepository
) {
    val notificationsFlow = patientRepository.notifications

    suspend fun sendNotification(notificationPatientEntity: NotificationPatientEntity) {
        patientRepository.insertNotification(notificationPatientEntity)
    }

    suspend fun updateNotifications(notifications: List<NotificationPatientEntity>) {
        for (notification in notifications) {
            patientRepository.updateNotification(notification)
        }
    }
}