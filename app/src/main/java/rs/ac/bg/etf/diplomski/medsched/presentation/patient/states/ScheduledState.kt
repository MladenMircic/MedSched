package rs.ac.bg.etf.diplomski.medsched.presentation.patient.states

import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Scheduled

data class ScheduledState(
    val scheduledList: List<Scheduled> = listOf(),
    val alreadyRevealed: List<Boolean> = listOf(),
    val deletedList: List<Scheduled> = listOf(),
    val revealNew: Boolean = false,
    val message: String? = null,
    val scheduleIndexToDelete: Int? = null,
    val lastDeleted: Int? = null,
    val isRefreshing: Boolean = false
)