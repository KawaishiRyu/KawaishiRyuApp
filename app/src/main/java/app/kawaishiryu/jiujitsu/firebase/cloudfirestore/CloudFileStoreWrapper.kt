package app.kawaishiryu.jiujitsu.firebase.cloudfirestore

import android.app.DownloadManager.Query
import android.util.Log
import app.kawaishiryu.jiujitsu.data.model.CurrentUser
import app.kawaishiryu.jiujitsu.data.model.DojosModel

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object CloudFileStoreWrapper {
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }


    //Corutina que pide datos
    suspend fun replace(
        collectionPath: String,
        documentPath: String,
        map: MutableMap<String, Any>
    ): Void{

        return suspendCoroutine { continuacion->
            Firebase.firestore.collection(collectionPath).document(documentPath).set(map)
                .addOnSuccessListener {
                    //Se guarda en la app
                    continuacion.resume(it)
                }
                .addOnFailureListener {
                    continuacion.resumeWithException(it)
                }
        }
    }


    //Esta funcion lo que hace es registrar al usuario y avisar si esta lista o no
    suspend fun registerComplete(user: CurrentUser): Boolean{
        return suspendCoroutine { continuacion ->
            firebaseAuth.createUserWithEmailAndPassword(user.email,user.password).addOnCompleteListener { task->

                if(task.isSuccessful){

                    var user = firebaseAuth.currentUser
                    Log.i("usuario registrado","${user!!.email}")
                    Log.i("usuario registrado2", "${user.uid}")
                continuacion.resume(task.isComplete)
                }

            }.addOnFailureListener {
                Log.i("registrarUsuarioError","${it}")
                continuacion.resumeWithException(it)
            }

        }
    }

    //Esta funcion nos sirve para poder ingresar de usuario

    suspend fun signInUserComplete(user: CurrentUser): Boolean{
        return suspendCoroutine { continuacion ->

            firebaseAuth.signInWithEmailAndPassword(user.email,user.password).addOnCompleteListener { task->
                if (task.isSuccessful){
                    continuacion.resume(task.isComplete)
                }
            }.addOnFailureListener { error->
                //Error de cuando logra ingresar
                continuacion.resumeWithException(error)
            }



        }
    }


    //Esta funcion suspendida nos sierve para retornar verdaddero en el caso
    //de que el usuario ya se encuentre activo

    suspend fun loggedUser(): Boolean{
        return suspendCoroutine { continuacion ->
            if(firebaseAuth.currentUser != null){
                continuacion.resume(true)
            }else{


            }
        }
    }

}
/*
 firebaseAuth.currentUser?.let {
            //Largamos lo actividad
            /*val intent = Intent(requireContext(),MainMenuHostActivity::class.java)
            startActivity(intent)*/
            val intent = Intent(requireContext(), MainMenuHostActivity::class.java)
            startActivity(intent)
            Toast.makeText(context, "Bienvenido de nuevo :)", Toast.LENGTH_SHORT).show()
        }
 */

