package rs.ac.bg.etf.diplomski.medsched.presentation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import rs.ac.bg.etf.diplomski.medsched.commons.PreferenceKeys
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    dataStore: DataStore<Preferences>
): ViewModel() {

    val tokenFlow = dataStore.data.map { preferences ->
        preferences[PreferenceKeys.USER_TOKEN_KEY]
    }
}