package app.kawaishiryu.jiujitsu.ui.fragment.techniques_menu

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.databinding.FragmentDetailTecBinding

class DetailTecFragment : Fragment(R.layout.fragment_detail_tec) {

    private lateinit var binding: FragmentDetailTecBinding
    private val args by navArgs<DetailTecFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDetailTecBinding.bind(view)

        binding.tvMain.text = args.nameMovArg
        binding.tvDetailName.text = "Nombre de tecnica: ${args.modelMovArg?.nameTec}"
        binding.tvTranslate.text = "Traduccion: ${args.modelMovArg?.transalteTec}"
        binding.tvDescription.text = "Descripcion: ${args.modelMovArg?.description}"

        binding.webView.visibility = View.GONE

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
                val baseUrl = "https://www.youtube.com" // Establecer el valor base según sea necesario
                binding.webView.loadDataWithBaseURL(baseUrl, it, "text/html", "utf-8", null)
            }
        }

    }

}