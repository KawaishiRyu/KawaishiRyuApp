package app.kawaishiryu.jiujitsu.util

import android.util.Patterns
import com.google.android.gms.maps.model.Marker
import java.util.*

/*
    Es una funcion con tipo lo que hace es recibir dos strings(email y la contraseña) y devuelve un valor booleano
    En el caso de que sea verdadero se admite el mail ingresado y el password
    Utilizamos expresiones regulares para coomparar  los dos strings
 */
fun controlEmailAndPassword(email: String, password: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email)
        .matches() && (password.matches("^[a-zA-Z0-9]*$".toRegex()) && password.isNotEmpty() && password.length > 4)
}

//Creamos la funcion para obtener un numero random
fun getRandomUUIDString():String = UUID.randomUUID().toString().replace("-", "")
