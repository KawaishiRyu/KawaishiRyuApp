package app.kawaishiryu.jiujitsu.repository.datastore.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.datastore.UserModelDataStore
import app.kawaishiryu.jiujitsu.repository.datastore.domain.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class DefaultUserRepository(private val context: Context) : UserRepository {

    override suspend fun saveUser(name: String, rol: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(UserModelDataStore.KEY_USER)] = name
            preferences[stringPreferencesKey(UserModelDataStore.KEY_CHECKED)] = rol
        }
    }

    override fun getUserProfile(): Flow<ViewModelState> {
        return context.dataStore.data.map { preferences ->
            try {
                val userModel = UserModelDataStore(
                    name = preferences[stringPreferencesKey(UserModelDataStore.KEY_USER)].orEmpty(),
                    rol = preferences[stringPreferencesKey(UserModelDataStore.KEY_CHECKED)].orEmpty()
                )
                ViewModelState.UserDataStoreSuccesfully(userModel)
            } catch (e: Exception) {
                ViewModelState.Error(e.message ?: "An error occurred")
            }
        }.catch { e ->
            emit(ViewModelState.Error(e.message ?: "An error occurred"))
        }.onStart {
            emit(ViewModelState.Loading)
        }.flowOn(Dispatchers.IO)
    }
}