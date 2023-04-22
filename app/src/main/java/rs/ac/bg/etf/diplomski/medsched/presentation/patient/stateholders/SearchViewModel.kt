package rs.ac.bg.etf.diplomski.medsched.presentation.patient.stateholders

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.DoctorForPatient
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.ImageRequestUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient.GetDoctorsUseCase
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.events.SearchEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.patient.states.SearchState
import rs.ac.bg.etf.diplomski.medsched.presentation.utils.animated
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getDoctorsUseCase: GetDoctorsUseCase,
    private val imageRequestUseCase: ImageRequestUseCase
): ViewModel() {

    private var searchJob: Job? = null

    private val _searchState = MutableStateFlow(SearchState())
    val searchState = _searchState.asStateFlow()

    fun onEvent(searchEvent: SearchEvent) {
        when (searchEvent) {
            is SearchEvent.UpdateSearchTextAndTriggerSearch -> {
                _searchState.update {
                    it.copy(
                        searchText = searchEvent.text,
                        dataLoading = true
                    )
                }
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(400)
                    getDoctorsForPatient()
                    searchJob = null
                }
            }
            is SearchEvent.SetCategoryFilter -> {
                _searchState.update { state ->
                    state.copy(
                        filterCategories = if (state.filterCategories.contains(searchEvent.categoryId))
                            state.filterCategories.toMutableList().also {
                                if (!it.contains(searchEvent.categoryId)
                                    || searchEvent.categoryId != 0
                                ) {
                                    it.remove(searchEvent.categoryId)
                                }
                            }
                        else
                            state.filterCategories.toMutableList().also {
                                if (searchEvent.categoryId == 0 || it.contains(0)) {
                                    it.clear()
                                }
                                it.add(searchEvent.categoryId)
                            }
                    )
                }
            }
            is SearchEvent.SetSelectingMode -> {
                _searchState.update { state ->
                    state.copy(
                        isSelectingMode = searchEvent.value,
                        multipleDoctorsIndexList = if (!searchEvent.value)
                            listOf()
                        else
                            state.multipleDoctorsIndexList
                    )
                }
            }
            is SearchEvent.ToggleMultipleDoctorSelected -> {
                _searchState.update { state ->
                    val multipleDoctorIndices = state.multipleDoctorsIndexList
                    state.copy(
                        multipleDoctorsIndexList = if (multipleDoctorIndices.contains(searchEvent.index)) {
                            multipleDoctorIndices
                                .toMutableList().also {
                                    it.remove(searchEvent.index)
                                }
                        }
                        else {
                            multipleDoctorIndices
                                .toMutableList().also {
                                    it.add(searchEvent.index)
                                }
                        }
                    )
                }
            }
//            SearchEvent.ClearCategoryFilter -> {
//                _searchState.update { state ->
//                    state.copy(
//                        filterCategories = state.filterCategories.toMutableList().also {
//                            it.clear()
//                        }
//                    )
//                }
//            }
            SearchEvent.ApplyFilter -> {
                _searchState.update { state ->
                    state.copy(
                        filterApplied = true,
                        filteredDoctorList = state.fetchedDoctorList.filter { doctor ->
                            state.filterCategories.contains(0) ||
                                    state.filterCategories.contains(doctor.categoryId)
                        }.toMutableStateList().animated,
                    )
                }
            }
            SearchEvent.ClearFilter -> {
                _searchState.update { state ->
                    state.copy(
                        filterCategories = listOf(0),
                        filterApplied = false
                    )
                }
            }
            is SearchEvent.SetSelectedPage -> {
                _searchState.update {
                    it.copy(selectedPage = searchEvent.page)
                }
            }
            is SearchEvent.SelectDoctorForSchedule -> {
                _searchState.update {
                    it.copy(selectedDoctor = searchEvent.index)
                }
            }
            SearchEvent.ClearSelection -> {
                _searchState.update {
                    it.copy(
                        isSelectingMode = false,
                        selectedDoctor = null,
                        multipleDoctorsIndexList = listOf()
                    )
                }
            }
        }
    }

    fun getSelectedDoctor(): DoctorForPatient {
        val searchStateValue = _searchState.value
        return searchStateValue.fetchedDoctorList[searchStateValue.selectedDoctor!!]
    }

    fun getSelectedDoctors(): List<DoctorForPatient> {
        val searchStateValue = _searchState.value
        return if (!searchStateValue.isSelectingMode) {
            listOf(searchStateValue.fetchedDoctorList[searchStateValue.selectedDoctor!!])
        } else {
            searchStateValue.filteredDoctorList.list.filterIndexed { index, _ ->
                searchStateValue.multipleDoctorsIndexList.contains(index)
            }.map { it.data }
        }
    }

    private fun getDoctorsForPatient() = viewModelScope.launch {
        delay(300L)
        val searchState = _searchState.value
        val response = getDoctorsUseCase(
            searchState.searchText,
            searchState.filterCategories.joinToString(separator = ",")
        )

        response.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    for (doctor in resource.data!!) {
                        doctor.imageRequest = withContext(Dispatchers.IO) {
                            imageRequestUseCase("/doctors/Doctor.png")
                        }
                    }
                    _searchState.update { state ->
                        state.copy(
                            fetchedDoctorList = resource.data,
                            filteredDoctorList = resource.data.filter { doctor ->
                                state.filterCategories.contains(0) ||
                                        state.filterCategories.contains(doctor.categoryId)
                            }.toMutableStateList().animated,
                            dataLoading = false
                        )
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {
                    _searchState.update { it.copy(dataLoading = true) }
                }
            }
        }
    }

    fun isCategorySelected(categoryId: Int): Boolean =
        _searchState.value.filterCategories.contains(categoryId)
}