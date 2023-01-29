package com.example.bookcourt.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


const val PREFERENCE_NAME = "App Preferences"

class DataStoreRepository(val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_NAME)
    private val dataStore = context.dataStore

    companion object PreferenceKeys {
        val isTutorChecked: Preferences.Key<Boolean> = booleanPreferencesKey("is_tutor_checked")
    }

    suspend fun setPref(prefValue: Boolean, prefKey: Preferences.Key<Boolean>) {
        dataStore.edit { pref->
            pref[prefKey] = prefValue
        }
    }

    fun getBoolState(prefKey: Preferences.Key<Boolean>) : Flow<Boolean> {
        return dataStore.data
            .catch { exception->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { pref->
                val prefMode = pref[prefKey] ?: false
                prefMode
            }
    }

}