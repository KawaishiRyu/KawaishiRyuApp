package app.kawaishiryu.jiujitsu.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object LocationPermission {


    private const val REQUEST_CODE_AFFIRMATION = 0
    private const val PERMISION = Manifest.permission.ACCESS_FINE_LOCATION

    //Trabajando con los permisos
    //Dame el permiso de localicacion, si es igual al activado  retornar true
    fun isLocationPermissionGranted(context: Context) = ContextCompat.checkSelfPermission(
        context,
        PERMISION
    ) == PackageManager.PERMISSION_GRANTED

    /*
       fun requestPermission(context: Context) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(PERMISION),
            PERMISSION_CODE
        )
    }
     */



    fun requestLocationPermission(activity: Activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                PERMISION
            )
        ) {
        } else {
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_AFFIRMATION
            )
        }
    }




    /*
      fun hasPermission(context: Context): Boolean {
        return (ContextCompat.checkSelfPermission(
            context,
            PERMISION
        ) == PackageManager.PERMISSION_GRANTED)

    }

     */



}