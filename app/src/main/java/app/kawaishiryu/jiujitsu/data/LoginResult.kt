package app.kawaishiryu.jiujitsu.data

import java.lang.Exception

sealed class LoginResult<out T>{
    class Loading(): LoginResult<Nothing>()
    data class Success<out T>(val data: T): LoginResult<T>()
    data class Failure(val exception: Exception): LoginResult<Nothing>()
}
