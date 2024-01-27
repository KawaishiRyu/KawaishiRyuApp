package app.kawaishiryu.jiujitsu.util
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import app.kawaishiryu.jiujitsu.ui.fragment.techniques_menu.DetailTecFragmentArgs
import io.grpc.NameResolver

class YoutubeVideoLoader(private val webView: WebView, private val args: DetailTecFragmentArgs) {

    fun loadYoutubeVideo() {
        if (args.movModelArg != null && args.movModelArg.toString().isNotEmpty()) {
            webView.visibility = View.VISIBLE

            val nuevoCodigo = args.movModelArg?.urlYoutube?.replace("width=\"560\"", "width=\"100%\"")
                ?.replace("height=\"315\"", "height=\"100%\"")

            webView.settings.javaScriptEnabled = true
            webView.webChromeClient = WebChromeClient()

            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    if (view?.title?.contains("YouTube") == true) {
                        // El video se cargó exitosamente
                    } else {
                        webView.visibility = View.GONE
                    }
                }
            }

            nuevoCodigo?.let {
                webView.loadData(it, "text/html", "utf-8")
            } ?: run {
                webView.visibility = View.GONE
            }
        }
    }
}
