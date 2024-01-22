package app.kawaishiryu.jiujitsu.ui.fragment.techniques_menu

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.databinding.FragmentDetailTecBinding
import app.kawaishiryu.jiujitsu.util.ColorUtils

class DetailTecFragment : Fragment(R.layout.fragment_detail_tec) {

    private lateinit var binding: FragmentDetailTecBinding
    private val args by navArgs<DetailTecFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDetailTecBinding.bind(view)

        binding.tvMain.text = args.nameMovArg
        binding.tvDetailName.text = "${args.modelMovArg?.nameTec}"
        binding.tvTranslate.text = " ${args.modelMovArg?.transalteTec}"
        binding.tvDescription.text = "${args.modelMovArg?.description}"
        binding.tvDif.text = "${args.modelMovArg?.grado}"

        args.modelMovArg?.grado?.let {
            ColorUtils.setColorForCardView(binding, it)
        }

        binding.webView.setBackgroundColor(Color.TRANSPARENT)
        binding.webView.visibility = View.GONE
        if (args.modelMovArg != null && args.modelMovArg.toString().isNotEmpty()) {
            binding.webView.visibility = View.VISIBLE

            // Reemplazar los valores fijos por "100%"
            val nuevoCodigo = args.modelMovArg?.urlYoutube?.replace("width=\"560\"", "width=\"100%\"")
                ?.replace("height=\"315\"", "height=\"100%\"")

            binding.webView.settings.javaScriptEnabled = true
            binding.webView.webChromeClient = WebChromeClient()

            binding.webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    if (view?.title?.contains("YouTube") == true) {
                        // El video se cargó exitosamente
                    } else {
                        binding.webView.visibility = View.GONE
                    }
                }
            }

            nuevoCodigo?.let {
                binding.webView.loadData(it, "text/html", "utf-8")
            } ?: run {
                // En caso de que nuevoCodigo sea nulo, ocultar el WebView
                binding.webView.visibility = View.GONE
            }
        }
    }

}