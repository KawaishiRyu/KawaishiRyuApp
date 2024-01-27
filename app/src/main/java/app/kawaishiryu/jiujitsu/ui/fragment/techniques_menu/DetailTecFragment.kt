package app.kawaishiryu.jiujitsu.ui.fragment.techniques_menu

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.data.model.movimientos.MoviemientosModel
import app.kawaishiryu.jiujitsu.data.model.tecnicas.MainModelTec
import app.kawaishiryu.jiujitsu.databinding.FragmentDetailTecBinding
import app.kawaishiryu.jiujitsu.util.ColorUtils
import app.kawaishiryu.jiujitsu.util.YoutubeVideoLoader
import app.kawaishiryu.jiujitsu.viewmodel.tec.TecnicasViewModel

class DetailTecFragment : Fragment(R.layout.fragment_detail_tec) {

    private lateinit var binding: FragmentDetailTecBinding

    private val viewModel: TecnicasViewModel by viewModels()
    private val args by navArgs<DetailTecFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDetailTecBinding.bind(view)

        //Item de menu principal
        val mainModelTec = MainModelTec(
            args.mainModelTecArg!!.title,
            args.mainModelTecArg!!.translate,
            args.mainModelTecArg!!.kanji
        )
        binding.tvTitleMain.text = args.mainModelTecArg?.title
        binding.tvTranslteMain.text = args.mainModelTecArg?.translate
        binding.tvKanjiMain.text = args.mainModelTecArg?.kanji

        //Item de la tecnica seleccionada
        binding.tvTitleTec.text = args.movModelArg?.nameTec
        binding.tvTranslateTec.text = args.movModelArg?.transalteTec
        binding.tvDescriptionTec.text = args.movModelArg?.description
        binding.tvDifTec.text = args.movModelArg?.grado
        args.movModelArg?.uuId

        val movModel = MoviemientosModel(
            uuId = args.movModelArg!!.uuId,
            nameTec = args.movModelArg!!.nameTec,
            transalteTec = args.movModelArg!!.transalteTec,
            urlYoutube = args.movModelArg!!.urlYoutube,
            description = args.movModelArg!!.description,
            grado = args.movModelArg!!.grado
        )

        args.movModelArg?.grado?.let {
            ColorUtils.setColorForCardView(binding, it)
        }

        binding.webView.setBackgroundColor(Color.TRANSPARENT)
        binding.webView.visibility = View.GONE

        val youtubeVideoLoader = YoutubeVideoLoader(binding.webView, args)
        youtubeVideoLoader.loadYoutubeVideo()

        binding.btnEditTec.setOnClickListener {
            val navController = Navigation.findNavController(requireView())

            val action = DetailTecFragmentDirections
                .actionDetailTecFragmentToCreateTecFragment(true,movModel,mainModelTec)

            navController.navigate(action)
        }

        binding.btnEliminar.setOnClickListener {
            viewModel.deleteDojoFirebase(movModel, args.mainModelTecArg!!.title)
            findNavController().popBackStack()
        }
    }
}