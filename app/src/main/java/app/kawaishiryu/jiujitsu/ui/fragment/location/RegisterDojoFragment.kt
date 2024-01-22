package app.kawaishiryu.jiujitsu.ui.fragment.location

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.core.ViewModelState
import app.kawaishiryu.jiujitsu.data.model.dojos.DojosModel
import app.kawaishiryu.jiujitsu.databinding.FragmentRegisterDojoBinding
import app.kawaishiryu.jiujitsu.util.ImageManipulationUtil
import app.kawaishiryu.jiujitsu.util.permission.LocationPermission
import app.kawaishiryu.jiujitsu.util.permission.StoragePermission
import app.kawaishiryu.jiujitsu.viewmodel.dojos.LocationViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.hbb20.CountryCodePicker.PhoneNumberValidityChangeListener
import kotlinx.coroutines.launch
import java.util.*


class RegisterDojoFragment : Fragment(R.layout.fragment_register_dojo), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener {

    private var controllerMarket = false
    private lateinit var market: Marker

    private lateinit var map: GoogleMap
    private var latitud = 0.0
    private var longitud = 0.0

    private lateinit var binding: FragmentRegisterDojoBinding
    private val viewModel: LocationViewModel by viewModels()

    private var imageSelectedUri: Uri? = null

    private val args by navArgs<RegisterDojoFragmentArgs>()
    private var modify: Boolean = false

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

        updateAndCreate()
        startFlows()
        clickableEvent()
        checKIsValidNumber()
    }

    //Funcion q observa si se hace un update o create
    private fun updateAndCreate() {
        if (args.dojoModel != null) {
            modify = true
            binding.btnCreate.text = "Actualizar"

            context?.let {
                Glide.with(it)
                    .load(args.dojoModel!!.dojoUrlImage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(binding.ivDojosRegFrg)
            }
            initUpdate()

        } else {
            binding.btnCreate.text = "Crear"
            modify = false
            binding.btnCreate.setOnClickListener {
                registerData()
            }
        }
    }

    private fun initUpdate() {
        if (args.dojoModel != null) {
            binding.apply {
                cvLocation.visibility = View.VISIBLE

                etSenseiName.setText(args.dojoModel!!.nameSensei)
                etNameDojo.setText(args.dojoModel!!.nameDojo)
                etDescription.setText(args.dojoModel!!.description)
                etPrice.setText(args.dojoModel!!.price)
                etNumber.setText(args.dojoModel!!.numberWpp.substring(2))
                etInstaUrl.setText(args.dojoModel!!.instaUrl)
                etFacebookUrl.setText(args.dojoModel!!.facebookUrl)
            }
        }
    }

    private fun startFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.locationViewModelState.collect {
                    when (it) {
                        is ViewModelState.Loading2 -> {
                            Log.d("???","Loading... : ")
                            showProgres()
                        }

                        is ViewModelState.Success2 -> {
                            Log.d("???", "Register Succes: ")
                            hideProgress()

                            val directions =
                                RegisterDojoFragmentDirections.actionRegisterDojoFragmentToLocationFragment()
                            findNavController().navigate(directions)
                        }

                        is ViewModelState.Empty -> {
                            Log.d("???", "ViewModelState Empty")
                            hideProgress()
                        }

                        is ViewModelState.Error2 -> {
                            Log.d("???", "ViewModelState Error")
                            hideProgress()
                        }

                        else -> {}
                    }
                }
            }
        }

    }


    //Obtener y registrar datos
    private fun registerData() {
        val modelDojo = getEditText()
        val uuid = getRandomUUIDString()
        modelDojo.uuId = uuid

        //Tengo q asegurar que el Uri no sea nulo
        val compressedBitmap = imageSelectedUri?.let { ImageManipulationUtil.compressBitmap(it, requireContext(), quality = 10) }

        viewModel.registerOrUpdate(compressedBitmap, "${modelDojo.uuId}.jpg", modelDojo, modify)
    }

    private fun clickableEvent() {
        binding.cvLocation.visibility = View.GONE

        binding.btnCreate.setOnClickListener {
            if (modify) {
                updateData()
            } else {
                registerData()
            }
        }
        binding.ivDojosRegFrg.setOnClickListener {
            solicitarPermisos()
        }

        //Agregamos el evento de crear el mapa
        binding.btnAddLocation.setOnClickListener {
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
                newMarket(latitude, longitude)

            }
        }
    }

    //Obtener los datos de las casillas
    private fun getEditText(): DojosModel {
        val modelDojo = DojosModel()
        modelDojo.nameSensei = binding.etSenseiName.text.toString().trim()
        modelDojo.nameDojo = binding.etNameDojo.text.toString().trim()
        modelDojo.description = binding.etDescription.text.toString().trim()
        modelDojo.price = binding.etPrice.text.toString().trim()
        modelDojo.facebookUrl = binding.etFacebookUrl.text.toString().trim()
        modelDojo.instaUrl = binding.etInstaUrl.text.toString().trim()
        modelDojo.numberWpp = binding.ccp.fullNumber
        modelDojo.latitud = latitud
        modelDojo.longitud = longitud
        modelDojo.imagePathUrl = args.dojoModel?.imagePathUrl ?: ""
        modelDojo.dojoUrlImage = args.dojoModel?.dojoUrlImage ?: ""

        return modelDojo
    }

    //Obtener y actualizar datos
    private fun updateData() {
        val modelDojo = getEditText()
        val uuid = args.dojoModel!!.uuId
        modelDojo.uuId = uuid

        // Verificar si se seleccionó una nueva imagen
        if (imageSelectedUri != null) {

            val compressedBitmap = imageSelectedUri?.let { ImageManipulationUtil.compressBitmap(it, requireContext(), quality = 10) }

            if (compressedBitmap != null) {
                viewModel.registerOrUpdate(compressedBitmap, "$uuid.jpg", modelDojo, modify)
            } else {
                modelDojo.imagePathUrl = args.dojoModel!!.imagePathUrl
                modelDojo.dojoUrlImage = args.dojoModel!!.dojoUrlImage
                viewModel.registerOrUpdate(null,"$uuid.jpg", modelDojo, modify)
            }
        } else {
            viewModel.registerOrUpdate(null,"$uuid.jpg", modelDojo, modify)
        }
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


