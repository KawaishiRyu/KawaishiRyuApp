package app.kawaishiryu.jiujitsu

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.kawaishiryu.jiujitsu.data.model.Dojos
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class DojosViewModel :ViewModel(){

    private var db = Firebase.firestore
    private val dojosFirebaseRef = "Dojos"
    private var storageRef= Firebase.storage

    fun initStorageRef(){
        storageRef = FirebaseStorage.getInstance()
    }

    private val createLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    private val updateLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    private val getListLiveData: MutableLiveData<List<Dojos>> by lazy {
        MutableLiveData<List<Dojos>>()
    }

    private val deleteLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun create(dojos: Dojos) {
        val docRef = db.collection(dojosFirebaseRef)
        docRef.add(dojos.toMap()).addOnSuccessListener {
            createLiveData.postValue(true)
        }.addOnFailureListener {
            Log.d("create", it.localizedMessage!!)
            createLiveData.postValue(false)
        }
    }

    fun update(dojos: Dojos) {
        val docRef = db.collection(dojosFirebaseRef)
        docRef.document(dojos.id!!).update(dojos.toMap()).addOnSuccessListener {
            updateLiveData.postValue(true)
        }.addOnFailureListener {
            Log.d("update", it.localizedMessage!!)
            updateLiveData.postValue(false)
        }
    }

    fun delete(id: String) {
        val docRef = db.collection(dojosFirebaseRef)
        docRef.document(id).delete().addOnSuccessListener {
            deleteLiveData.postValue(true)
        }.addOnFailureListener {
            Log.d("delete", it.localizedMessage!!)
            deleteLiveData.postValue(false)
        }
    }

    fun getList() {
        val docRef = db.collection(dojosFirebaseRef)
        docRef.get().addOnSuccessListener {
            val dojos = ArrayList<Dojos>()
            for (item in it.documents) {

                val dojos = Dojos()
                //Falta algo
            }

            getListLiveData.postValue(dojos)
        }.addOnFailureListener {
            Log.d("get", it.localizedMessage!!)
            getListLiveData.postValue(null)
        }
    }

    //Metodo subir imagen
    fun uploadImageToFirebase(dojos: Dojos) {
        initStorageRef()
        storageRef.getReference("images_dojos").child(System.currentTimeMillis().toString())
            .putFile(Uri.parse(dojos.urlImageDojo)).addOnSuccessListener { task ->
                task.metadata!!.reference!!.downloadUrl
                    .addOnFailureListener {
                        Log.d("Error", "Error al subir una imagen")
                    }
                    .addOnCompleteListener { task2 ->
                        if (task2.isSuccessful) {
                            if (dojos.id != null) {
                                dojos.urlImageDojo = task2.result.toString()
                                update(dojos)
                            } else {
                                dojos.urlImageDojo = task2.result.toString()
                                create(dojos)
                            }
                            //Cuando se complete la subida
                        } else {
                            //Cuando se produce un error
                        }
                    }
            }
    }

}