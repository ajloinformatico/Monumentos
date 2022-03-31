package es.sdos.formacion.monumentosandaluces.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import es.sdos.formacion.monumentosandaluces.R

@AndroidEntryPoint
class MonumentWebViewFragment : Fragment() {

    private val safeArgs : MonumentWebViewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_monument_web_view, container, false)
        val webView: WebView = view.findViewById(R.id.activity_monument_detail__webView)
        true.also { webView.settings.javaScriptEnabled = it }
        webView.webViewClient = WebViewClient()
        webView.loadUrl(safeArgs.monumentUrl)
        return view
    }
}