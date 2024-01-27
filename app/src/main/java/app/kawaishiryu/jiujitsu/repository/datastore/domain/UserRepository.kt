package app.kawaishiryu.jiujitsu.repository.datastore.domain

import app.kawaishiryu.jiujitsu.core.ViewModelState
import kotlinx.coroutines.flow.Flow

//Capa de abastraccion
//Es una interfaz que define las operaciones que pueden realizarse con respecto al usuario,
//como guardar y recuperar datos. También define el flujo de estados de la vista (UserViewState).

interface UserRepository {
    suspend fun saveUser(name: String, rol: String)
    fun getUserProfile(): Flow<ViewModelState>
}
