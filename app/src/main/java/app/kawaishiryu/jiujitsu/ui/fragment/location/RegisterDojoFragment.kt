package app.kawaishiryu.jiujitsu.ui.fragment.location

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.dojos.DojosModel
import app.kawaishiryu.jiujitsu.databinding.FragmentRegisterDojoBinding
import app.kawaishiryu.jiujitsu.util.LocationPermission
import app.kawaishiryu.jiujitsu.util.StoragePermission
import app.kawaishiryu.jiujitsu.view.DojosViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.hbb20.CountryCodePicker.PhoneNumberValidityChangeListener
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.util.*


class RegisterDojoFragment : Fragment(R.layout.fragment_register_dojo), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener {

    private var controllerMarket = false
    private lateinit var market: Marker

    //Declaramos la api de Google Mpas
    private lateinit var map: GoogleMap
    private var latitud = 0.0
    private var longitud = 0.0

    private lateinit var binding: FragmentRegisterDojoBinding
    private val viewModel: DojosViewModel by viewModels()
    private var imageSelectedUri: Uri? = null

    //Seleccionar imagen
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Log.i("imagenes", "Llego ahì")
            if (it.resultCode == Activity.RESULT_OK) {
                imageSelectedUri = it.data?.data
                Log.i("imagenes", "$imageSelectedUri")
                binding.ivDojosRegFrg.setImageURI(imageSelectedUri)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRegisterDojoBinding.bind(view)

        initFlows()
        clickableEvent()

        checKIsValidNumber()
    }

    /*Inicializa el flujo de trabajos
   Los flujos de trabajo son un patron comun en la programacion reactiva y se utiliza para controlar y
   manipular flujos de datos asincronos
     */
    private fun initFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dojosViewModelState.collect {
                    // Process item
                    when (it) {
                        is ViewModelState.Loading -> {
                            //show progresss here
                            Log.d("???", "Cargando")
                            showProgres()
                        }

                        is ViewModelState.RegisterSuccessfullyDojo -> {
                            hideProgress()
                            //Register succes
                            Log.d("???", "fue exitoso ${it.dojoModel}")
                            Toast.makeText(context, "Register succes", Toast.LENGTH_SHORT)
                                .show()
                        }

                        is ViewModelState.Empty -> {
                            //Selected is empty
                            Log.d("???", "Vacio")
                            hideProgress()
                        }

                        is ViewModelState.Error -> {
                            Log.d("???", "Error")
                            hideProgress()
                        }
                    }
                }
            }
        }

    }

    //    BitMapFromUri
    private fun getBitmapFromUri(uri: Uri, context: Context, quality: Int = 100): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            BitmapFactory.decodeStream(inputStream, null, options)?.let { bitmap ->
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                BitmapFactory.decodeByteArray(
                    outputStream.toByteArray(),
                    0,
                    outputStream.toByteArray().size
                )
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }


    private fun clickableEvent() {
        binding.cvLocation.visibility = View.GONE

        binding.btnCreate.setOnClickListener {
            getAndUploadData()
        }
        binding.ivDojosRegFrg.setOnClickListener {
            solicitarPermisos()
        }

        //Agregamos el evento de crear el mapa
        binding.btnAddLocation.setOnClickListener {
            //ACA
            binding.buttonAccept.visibility = View.VISIBLE
            createFragmentMap()
        }
    }

    private fun createFragmentMap() {

        binding.linearLayout12.visibility = View.GONE
        binding.scrollView.visibility = View.GONE

        binding.mapsId.visibility = View.VISIBLE
        binding.fragmentMap.visibility = View.VISIBLE

        val mapFragment =
            childFragmentManager.findFragmentById(binding.fragmentMap.id) as SupportMapFragment

        mapFragment.getMapAsync(this)
        mapFragment.getMapAsync { googleMap ->
            googleMap.setOnMapClickListener { latLng ->
                // Obtiene la latitud y longitud del punto donde se hizo clic
                val latitude = latLng.latitude
                val longitude = latLng.longitude
                // Aquí puedes hacer lo que necesites con las coordenadas obtenidas
                Toast.makeText(
                    requireContext(),
                    "Latitud: $latitude, Longitud: $longitude",
                    Toast.LENGTH_SHORT
                )
                    .show()
                newMarket(latitude, longitude)

            }
        }
    }

    //Obtener y actualizar Datos
    private fun getAndUploadData() {
        val modelDojo = DojosModel()
        val uuid = getRandomUUIDString()

        modelDojo.uuId = uuid
        modelDojo.nameSensei = binding.etSenseiName.text.toString().trim()
        modelDojo.nameDojo = binding.etNameDojo.text.toString().trim()
        modelDojo.description = binding.etDescription.text.toString().trim()
        modelDojo.price = binding.etPrice.text.toString().trim()

        //Agregamos facebook, instagram, wpp
        modelDojo.facebookUrl = binding.etFacebookUrl.text.toString().trim()
        modelDojo.instaUrl = binding.etInstaUrl.text.toString().trim()
        modelDojo.numberWpp = binding.ccp.fullNumber

        //Agregamos latitud y longitud
        modelDojo.latitud = latitud
        modelDojo.longitud = longitud

        //Selecciono cualquier uri
        val bitmap = getBitmapFromUri(imageSelectedUri!!, requireContext(), quality = 10)
        val uri =
            Uri.parse("https://upload.wikimedia.org/wikipedia/commons/thumb/c/c3/URI_Euler_Diagram_no_lone_URIs.svg/1200px-URI_Euler_Diagram_no_lone_URIs.svg.png")
        val bitmap2 = getBitmapFromUri(uri, requireContext(), quality = 10)

        viewModel.register(bitmap, "${modelDojo.uuId}.jpg", modelDojo)
    }

    //Se llama cuando el mapa esta listo
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMyLocationButtonClickListener(this)
        enableLocation()
    }

    @SuppressLint("MissingPermission")
    private fun enableLocation() {
        //Si el mapoa no fue inicializado
        if (!::map.isInitialized) return
        if (LocationPermission.isLocationPermissionGranted(requireContext())) {
            //Si acepto los permisos
            //Obtenemos la ubicacion actual
            obtenerUbicacionActual()
            map.isMyLocationEnabled = true
        } else {
            //si no se activaron los permisos
            LocationPermission.requestLocationPermission(requireActivity())
        }
    }

    // Método para obtener la ubicación actual
    private fun obtenerUbicacionActual() {
        viewModel.getUserLocation(requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.locationUser.collect() { latLong ->
                    locationUserZoom(latLong.latitude, latLong.longitude)
                    //Toast.makeText(requireContext(), "latitud:${latLong.latitude} y longitud: ${latLong.longitude}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    //Se llama cuando el mapa esta listo
    private fun locationUserZoom(lat: Double, long: Double) {
        val favoritePlace = LatLng(lat, long)
        //  map.addMarker(MarkerOptions().position(favoritePlace).title("Mi ubicacion Actual"))
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(favoritePlace, 18f),
            4000,
            null
        )
    }


    //Creamos esta funcion para crear el marcador para que cuando hagamos click se vea
    private fun newMarket(lat: Double, log: Double) {
        if (!controllerMarket) {
            Log.i("aqui llego", "latitud: ${lat}, longitud: ${log}")

            binding.tvLat.text = "Latitud: $lat"
            binding.tvLong.text = "Longitud: $log"

            val locationDojo = LatLng(lat, log)
            market = map.addMarker(MarkerOptions().position(locationDojo).title("dojo"))!!
            controllerMarket = true
            binding.buttonAccept.isEnabled = true
            binding.cvLocation.visibility = View.VISIBLE

            binding.buttonAccept.setOnClickListener {
                latitud = lat
                longitud = log
                dissapearViews()
            }
        } else {
            market.remove()
            controllerMarket = false
        }
    }

    private fun dissapearViews() {

        binding.fragmentMap.visibility = View.GONE
        binding.buttonAccept.visibility = View.GONE
        binding.mapsId.visibility = View.GONE
        binding.scrollView.visibility = View.VISIBLE
        binding.linearLayout12.visibility = View.VISIBLE
    }

    //Boton q te lleva a tu ubicacion
    override fun onMyLocationButtonClick(): Boolean {
        //Toast.makeText(requireContext(), "Hizo click", Toast.LENGTH_LONG).show()
        return false
    }

    //Metodo que verifica si el numero es valido
    private fun checKIsValidNumber() {
        binding.ccp.registerCarrierNumberEditText(binding.etNumber)

        binding.ccp.setPhoneNumberValidityChangeListener(PhoneNumberValidityChangeListener {
            if (it) {
                binding.checkBox.setImageResource(R.drawable.check1)
            } else {
                binding.checkBox.setImageResource(R.drawable.error)
            }
        })
    }


    //Crear un Id random
    private fun getRandomUUIDString(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }

    private fun showProgres() {
        binding.animationFrame.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.animationFrame.visibility = View.GONE
    }

    //Permisos de imagen
    private fun solicitarPermisos() {
        if (StoragePermission.hasPermission(requireContext())) {
            Toast.makeText(context, "Tiene permisos", Toast.LENGTH_SHORT).show()
            val intent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            resultLauncher.launch(intent)

        } else {
            StoragePermission.requestPermission(requireContext())
            if (!StoragePermission.shouldShowRequestPermissionRationale(requireContext())) {
                StoragePermission.explainPermission(requireContext())
            }
        }
    }

}


