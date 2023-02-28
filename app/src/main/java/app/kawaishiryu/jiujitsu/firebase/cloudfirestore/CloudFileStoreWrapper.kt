package app.kawaishiryu.jiujitsu.firebase.cloudfirestore

import android.app.DownloadManager.Query
import app.kawaishiryu.jiujitsu.core.DojoViewModelState
import app.kawaishiryu.jiujitsu.data.model.DojosModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object CloudFileStoreWrapper {

    //Corutina que pide datos
    suspend fun replace(
        collectionPath: String,
        documentPath: String,
        map: MutableMap<String, Any>
    ): Void {

        return suspendCoroutine {
            Firebase.firestore.collection(collectionPath).document(documentPath).set(map)
                .addOnSuccessListener {

                }
                .addOnFailureListener {

                }
        }
    }

    suspend fun select2(
        colletionPath: String,
        orderBy: String? = null,
        offset: DocumentSnapshot? = null,
        limit: Long = 1,
        conditionMap: MutableMap<String, Any>? = null
    ): QuerySnapshot {

        return suspendCoroutine { continuation ->
            val collectionReferece = Firebase.firestore.collection(colletionPath)

            //Create query
            //the order must be 1-order by, 2-offset, 3-limit, 4-condition
            //dont change the query order
            var query: com.google.firebase.firestore.Query? = null

            //1 order by
            orderBy?.let {
                //we will order by create date , desc
                query = collectionReferece.orderBy(it, com.google.firebase.firestore.Query.Direction.DESCENDING)
            }
            //2 offset
            orderBy?.let { offset ->
                //check query is null or not
                query?.let {
                    query = it.startAfter(offset)
                } ?: kotlin.run {
                    query = collectionReferece.limit(limit)
                }
            }

            // 3 limit
            query?.let {
                query = it.limit(limit)

            } ?: kotlin.run {
                query = collectionReferece.limit(limit)
            }

            // 4 limit
            // this condition may ask to create a index
            // we will create index when we test it
            conditionMap?.let {
                it.forEach { map ->
                    //codition map example id = xxxx, password = xxxx
                    query = collectionReferece.whereEqualTo(map.key, map.value)
                }
            }

            conditionMap?.let {
                it.forEach { map ->
                    query = collectionReferece.whereEqualTo(map.key, map.value)
                }
            }

            query?.get()
                ?.addOnSuccessListener {
                    continuation.resume(it)
                }
                ?.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    suspend fun select(
        colletionPath: String,
        orderBy: String? = null,
        //conditionMap: MutableMap<String, Any>? = null
    ): QuerySnapshot {

        return suspendCoroutine { continuation ->
            val collectionReferece = Firebase.firestore.collection(colletionPath)

            //Create query
            //the order must be 1-order by, 2-offset, 3-limit, 4-condition
            //dont change the query order
            var query: com.google.firebase.firestore.Query? = null

            //1 order by
            orderBy?.let {
                //we will order by create date , desc
                query = collectionReferece.orderBy(it, com.google.firebase.firestore.Query.Direction.DESCENDING)
            }

            query?.get()
                ?.addOnSuccessListener {
                    continuation.resume(it)
                }
                ?.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }




}