package rs.ac.bg.etf.diplomski.medsched.presentation.patient.states

import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Scheduled

data class ScheduledState(
    val scheduledList: List<Scheduled> = listOf(),
    val alreadyRevealed: List<Boolean> = listOf(),
    val message: String? = null,
    val deletingIndex: Int? = null,
    val lastDeleted: Int? = null,
    val isRefreshing: Boolean = false
)