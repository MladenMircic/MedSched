package rs.ac.bg.etf.diplomski.medsched.domain.use_case

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import coil.request.ImageRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import rs.ac.bg.etf.diplomski.medsched.commons.Constants.SERVICE_ICONS_URL
import rs.ac.bg.etf.diplomski.medsched.commons.PreferenceKeys
import javax.inject.Inject

class ImageRequestUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val dataStore: DataStore<Preferences>
) {

    suspend operator fun invoke(relativePath: String): ImageRequest =
        ImageRequest.Builder(context)
            .data("${SERVICE_ICONS_URL}/$relativePath")
            .addHeader(
                "Authorization",
                "Bearer ${dataStore.data.first()[PreferenceKeys.USER_TOKEN_KEY]}"
            )
            .build()
}