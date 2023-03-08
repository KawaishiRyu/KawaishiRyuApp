package app.kawaishiryu.jiujitsu.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.kawaishiryu.jiujitsu.databinding.FragmentDetailLocationDojoBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.squareup.picasso.Picasso


class DetailLocationDojoFragment :
    Fragment(app.kawaishiryu.jiujitsu.R.layout.fragment_detail_location_dojo) {

    private lateinit var binding: FragmentDetailLocationDojoBinding
    val args: DetailLocationDojoFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDetailLocationDojoBinding.bind(view)

        args.argumentos.let {
            binding.tvNameDojo.text = it.nameDojo
            binding.tvNameSensei.text = it.nameSensei
            binding.tvDescription.text = it.description
            binding.tvPrice.text = "El precio es: ${it.price}"
//            binding.tvFacebookUrl.text = it.facebookUrl
//            binding.tvInstaUrl.text = it.instaUrl
//            binding.tvNumeberWpp.text = it.numberWpp

            Picasso.get().load(it.dojoUrlImage).into(binding.ivDojoDetail)

            Glide.with(view)
                .load(it.dojoUrlImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.ivDojoDetail)

            binding.ivIgDetail.setOnClickListener { viewIt ->
                openInsta(it.instaUrl)
            }

            binding.ivIgMap.setOnClickListener { viewIt ->
                //Al hacer click aqui abrimos la ruta del mapa
                openGoogleMaps(it.latitud,it.longitud)


            }

            binding.ivIgWpp.setOnClickListener { viewIt ->
                openWpp(it.numberWpp)
            }

//            binding.button.setOnClickListener {
//                val directions = DetailLocationDojoFragmentDirections.actionLocationDojoFragmentToTimeRegisterFragment()
//                findNavController().navigate(directions)
//            }
        }

    }

    private fun openWpp(numero: String) {
        val url = "https://api.whatsapp.com/send?phone=+$numero"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)

        try {
            startActivity(intent)

        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://api.whatsapp.com/send?phone=+$numero")
                )
            )
        }
    }


    //Creamos la funcion que abríra el google maps
    private fun openGoogleMaps(latitud: Double, longitud: Double) {

        var label = "Dojo"
        var uri = "geo:$latitud,$longitud?q=$latitud,$longitud($label)"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(intent)
    }

    private fun openInsta(user: String) {

        val uri = Uri.parse("http://instagram.com/_u/$user")
        val intent = Intent(Intent.ACTION_VIEW, uri)

        intent.setPackage("com.instagram.android")

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/$user")
                )
            )
        }
    }

}