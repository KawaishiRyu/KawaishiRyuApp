package app.kawaishiryu.jiujitsu.ui.fragment.location

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.navArgs
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.databinding.FragmentDetailLocationBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


class DetailLocationFragment : Fragment(R.layout.fragment_detail_location) {

    private lateinit var binding: FragmentDetailLocationBinding
    private val args by navArgs<DetailLocationFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDetailLocationBinding.bind(view)

        binding.tvNameDojo.text = args.dojoDetail.nameDojo
        binding.tvNameSensei.text = "Nombre del sensei ${args.dojoDetail.nameSensei}"
        binding.tvDescription.text = "Descripcion: ${args.dojoDetail.description}"
        binding.tvPrice.text = "Precio ${args.dojoDetail.price}"

        Glide.with(view)
            .load(args.dojoDetail.dojoUrlImage)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(binding.ivDojoDetail)

        binding.ivIgMap.setOnClickListener {
            openGoogleMaps(args.dojoDetail.latitud,args.dojoDetail.longitud)
        }

        binding.ivIgWpp.setOnClickListener {
            openWpp(args.dojoDetail.numberWpp)
        }
        binding.ivIgDetail.setOnClickListener {
            openInsta(args.dojoDetail.instaUrl)
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