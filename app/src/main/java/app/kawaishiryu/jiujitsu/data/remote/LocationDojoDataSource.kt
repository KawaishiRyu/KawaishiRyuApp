package app.kawaishiryu.jiujitsu.data.remote

import app.kawaishiryu.jiujitsu.core.Resource
import app.kawaishiryu.jiujitsu.data.model.Dojos
import app.kawaishiryu.jiujitsu.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class LocationDojoDataSource {

    /*
    //Peticiones
    suspend fun getLocationDojos(): Resource<List<Dojos>> {
        val querSnapshot = FirebaseFirestore.getInstance().collection("posts").get().await()

        for (post in querSnapshot.documents) {
            post.toObject(User::class.java)?.let { fbPost ->

            }
        }
        return Resource.Succes(postList)
    }
    */
}