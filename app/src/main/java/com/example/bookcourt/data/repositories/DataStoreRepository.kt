package com.example.bookcourt.data.repositories

import android.content.Context
import android.util.Log
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
        val isRemembered: Preferences.Key<Boolean> = booleanPreferencesKey("is_remembered")
        val savedName: Preferences.Key<String> = stringPreferencesKey("user_name")
        val savedSurname: Preferences.Key<String> = stringPreferencesKey("user_surname")
        val savedPhoneNumber: Preferences.Key<String> = stringPreferencesKey("user_phone")
        val uuid: Preferences.Key<String> = stringPreferencesKey("uuid")
        val startSessionTime: Preferences.Key<Int> = intPreferencesKey("startSessionTime")
        val endSessionTime: Preferences.Key<Int> = intPreferencesKey("endSessionTime")
        val savedCity: Preferences.Key<String> = stringPreferencesKey("user_city")
        val savedLikedBooksCnt: Preferences.Key<Int> = intPreferencesKey("liked_books_cnt")
        val savedDislikedBooksCnt: Preferences.Key<Int> = intPreferencesKey("disliked_books_cnt")
        val savedFavoriteGenres: Preferences.Key<String> = stringPreferencesKey("favorite_genres")
        val savedWantToReadList: Preferences.Key<String> = stringPreferencesKey("want_to_read_genres")
        val dislikedGenresList: Preferences.Key<String> = stringPreferencesKey("disliked_genres")
        val readBooksList: Preferences.Key<String> = stringPreferencesKey("read_books")
    }

    suspend fun setPref(prefValue: Boolean, prefKey: Preferences.Key<Boolean>) {
        dataStore.edit { pref->
            pref[prefKey] = prefValue
        }
        Log.d("DATASTORE","setPref отработал Bool")
    }

    suspend fun setPref(prefValue: String, prefKey: Preferences.Key<String>) {
        dataStore.edit { pref->
            pref[prefKey] = prefValue
        }
        Log.d("DATASTORE","setPref отработал String ${prefValue}")
    }

    suspend fun setPref(prefValue: Int, prefKey: Preferences.Key<Int>) {
        dataStore.edit { pref->
            pref[prefKey] = prefValue
        }
    }

    fun getBoolPref(prefKey: Preferences.Key<Boolean>) : Flow<Boolean> {
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

    fun getPref(prefKey: Preferences.Key<String>) : Flow<String> {
        return dataStore.data
            .catch { exception->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { pref->
                val prefMode = pref[prefKey] ?: ""
                prefMode
            }

    }

    fun getIntPref(prefKey: Preferences.Key<Int>) : Flow<Int> {
        return dataStore.data
            .catch { exception->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { pref->
                val prefMode = pref[prefKey] ?: 0
                prefMode
            }

    }

}