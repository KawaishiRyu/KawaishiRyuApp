package app.kawaishiryu.jiujitsu.core

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import app.kawaishiryu.jiujitsu.repository.firebase.storage.FirebaseStorageManager
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import java.io.ByteArrayOutputStream
import java.util.concurrent.Flow
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object MapsRepository {

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(context: Context): LatLng {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        return suspendCoroutine { continuation ->
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    continuation.resume(LatLng(location.latitude, location.longitude))
                }
            }

        }
    }
}