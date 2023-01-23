package rs.ac.bg.etf.diplomski.medsched.di.json_adapters

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

inline fun <reified T> Moshi.toList(jsonString: String): List<T>? {
    return adapter<List<T>>(Types.newParameterizedType(List::class.java, T::class.java))
        .fromJson(jsonString)
}

inline fun <reified T> Moshi.fromList(list: List<T>): String {
    return adapter<List<T>>(Types.newParameterizedType(List::class.java, T::class.java))
        .toJson(list)
}