package app.kawaishiryu.jiujitsu.ui.fragment.location

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.data.model.dojos.DojosModel
import app.kawaishiryu.jiujitsu.databinding.FragmentDetailLocationBinding
import app.kawaishiryu.jiujitsu.ui.adapter.adapterhour.HourAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions


class DetailLocationFragment : Fragment(R.layout.fragment_detail_location) {

    private lateinit var binding: FragmentDetailLocationBinding
    private val args by navArgs<DetailLocationFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDetailLocationBinding.bind(view)

        // Mostrar el uuid en el TextView
        binding.tvNameDojo.text = args.dojoDetail.nameDojo
        binding.tvNameSensei.text = "Nombre del sensei: ${args.dojoDetail.nameSensei}"
        binding.tvDescription.text = "Descripcion: ${args.dojoDetail.description}"
        binding.tvPrice.text = "Precio ${args.dojoDetail.price}"

        val horarios = args.dojoDetail.horarios
        val horariosFormateadosList = mutableListOf<String>()

        for ((index, horario) in horarios.withIndex()) {
            val horarioFormateado = StringBuilder()
            horarioFormateado.append("Horario ${index + 1}:\n")
            horarioFormateado.append("Disciplina: ${horario.disciplina}\n")
            horarioFormateado.append("${horario.dias.joinToString(", ")}\n")
            horarioFormateado.append("${horario.horarioEntrada}\n")
            horarioFormateado.append("${horario.horarioSalida}\n")
            horariosFormateadosList.add(horarioFormateado.toString())
        }


        val horariosAdapter = HourAdapter(
            horariosFormateadosList,
            null
        ) // Puedes proporcionar un listener si lo necesitas

        // Configura el RecyclerView con el adaptador
        binding.rvHour.adapter = horariosAdapter


        Glide.with(view)
            .load(args.dojoDetail.dojoUrlImage)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate()
            .into(binding.ivDojoDetail)

        // Aplica la animación personalizada al ImageView cuando la vista se carga
        val scaleUpAnimation: Animation = AnimationUtils.loadAnimation(view.context, R.anim.anim_three)
        binding.ivDojoDetail.startAnimation(scaleUpAnimation)

        binding.ivIgMap.setOnClickListener {
            openGoogleMaps(args.dojoDetail.latitud, args.dojoDetail.longitud)
        }

        binding.ivIgWpp.setOnClickListener {
            openWpp(args.dojoDetail.numberWpp)
        }
        binding.ivIgDetail.setOnClickListener {
            openInsta(args.dojoDetail.instaUrl)
        }
        binding.addTime.setOnClickListener {

            val dojo =
                DojosModel(
                    nameDojo = args.dojoDetail.nameDojo,
                    uuId = args.dojoDetail.uuId,
                    nameSensei = args.dojoDetail.nameSensei,
                    dojoUrlImage = args.dojoDetail.dojoUrlImage,
                    imagePathUrl = args.dojoDetail.imagePathUrl,
                    description = args.dojoDetail.description,
                    price = args.dojoDetail.price,
                    horarios = args.dojoDetail.horarios,
                    numberWpp = args.dojoDetail.numberWpp,
                    instaUrl = args.dojoDetail.instaUrl,
                    facebookUrl = args.dojoDetail.facebookUrl,
                    latitud = args.dojoDetail.latitud,
                    longitud = args.dojoDetail.longitud
                )

            val directions =
                DetailLocationFragmentDirections.actionDetailLocationFragmentToTimeRegisterFragment(
                    dojo
                )
            findNavController().navigate(directions)

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
        val label = "Dojo"
        val uri = "geo:$latitud,$longitud?q=$latitud,$longitud($label)"
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