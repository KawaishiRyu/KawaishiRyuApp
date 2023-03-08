package app.kawaishiryu.jiujitsu.core

import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import app.kawaishiryu.jiujitsu.firebase.storage.FirebaseStorageManager
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


    /*
       suspend fun getCurrentLocation(): LatLng {
       return coroutineScope { continuacion ->

           fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
               if (location != null) { (LatLng(location.latitude,location.longitude))
               }

           }

       }


    }
     */

    /*
        private fun obtenerUbicacionActual() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Verificar si se pudo obtener la ubicación
                if (location != null) {
                    // Obtener la latitud y longitud de la ubicación actual
                    val latitud = location.latitude
                    val longitud = location.longitude
                    // Aquí puedes hacer lo que necesites con las coordenadas obtenidas
                    Toast.makeText(
                        requireContext(),
                        "Latitud: $latitud, Longitud: $longitud",
                        Toast.LENGTH_SHORT
                    ).show()
                    createMarker(latitud, longitud)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "No se pudo obtener la ubicación actual",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


     */
}