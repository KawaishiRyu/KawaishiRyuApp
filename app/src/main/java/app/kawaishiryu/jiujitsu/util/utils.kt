package app.kawaishiryu.jiujitsu.util

import android.util.Patterns

/*
    Es una funcion con tipo lo que hace es recibir dos strings(email y la contraseña) y devuelve un valor booleano
    En el caso de que sea verdadero se admite el mail ingresado y el password
    Utilizamos expresiones regulares para coomparar  los dos strings
 */
fun controlEmailAndPassword(email: String, password: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email)
        .matches() && (password.matches("^[a-zA-Z0-9]*$".toRegex()) && password.isNotEmpty() && password.length > 4)
}


/* private fun valideteEmail(): Boolean {


       return if (email.isEmpty()) {
           binding.editTextEmailId.error = "No puede estar vacio"
           false
       } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
           binding.editTextEmailId.error = "Por favor ingrese un email valido"
           false
       } else {
           binding.txtInpEmail.error = null
           Toast.makeText(context, "Good", Toast.LENGTH_SHORT).show()
           true
       }
   }

   private fun validePassword(): Boolean {


       val passwordRegex = Pattern.compile(
           ".{4,}"// Minimo cuatro caracteres
       )

       return if (password.isEmpty()) {
           binding.txtInpPassword.error = "El archivo no puede estar vacio"
           false
       } else if (!passwordRegex.matcher(password).matches()) {
           binding.txtInpPassword.error = "La contraseña es muy corta"
           false
       } else {
           binding.editTextEmailId.error = null
           binding.txtInpPassword.clearError()
           true
       }
   }*/