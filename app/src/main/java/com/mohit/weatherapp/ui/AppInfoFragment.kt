package com.mohit.weatherapp.ui

import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.databinding.DataBindingUtil
import com.mohit.weatherapp.R
import com.mohit.weatherapp.databinding.FragmentAppInfoBinding
import com.mohit.weatherapp.interfaces.IFragmentCallbacks

class AppInfoFragment : BaseFragment() {

    companion object {
        const val TAG = "AppInfoFragment"
        const val MIME_TYPE_HTML = "text/html; charset=utf-8"
        const val BASE_64_ENCODING  = "base64"
        const val CONTENTS = "<p><strong>Hey User,</strong></p>\n" +
                "\n" +
                "<p>Here is how you can use the App.</p>\n" +
                "\n" +
                "<ol>\n" +
                "\t<li>You can add locations in your bookmarks, to always see the weather info.</li>\n" +
                "\t<li>Just tap the location on the map and there you go.</li>\n" +
                "\t<li>Simply delete the location when no longer needed.</li>\n" +
                "\t<li>Tap the location from the list to see more info.</li>\n" +
                "</ol>\n" +
                "\n" +
                "<p>This App Consists of concepts like <span style=\"color: rgb(34, 34, 34); font-family: Arial, Helvetica, sans-serif; font-size: small; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255); text-decoration-style: initial; text-decoration-color: initial; display: inline !important; float: none;\"><strong>MVVM, Dagger2, Motion Layout, Room, Data Binding, Provision to Enable Dark &amp; Light Theme, Robolectric, Mockio based Passing unit test cases  etc.</strong></span></p>\n" +
                "\n" +
                "<p><strong>Thanks.</strong></p>\n"
    }

    lateinit var binding: FragmentAppInfoBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IFragmentCallbacks) {
            iFragmentCallbacks = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_app_info, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iFragmentCallbacks.showBackArrow()
        iFragmentCallbacks.updateTitle(getString(R.string.app_info))
        setUpWebView(CONTENTS)
    }

    private fun setUpWebView(htmlContent: String) {
        binding.webview.clearHistory()
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.settings.loadWithOverviewMode = true
        binding.webview.settings.useWideViewPort = false
        binding.webview.settings.builtInZoomControls = true
        binding.webview.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        val encodedHtml = Base64.encodeToString(htmlContent.toByteArray(), Base64.NO_PADDING)
        binding.webview.loadData(encodedHtml, MIME_TYPE_HTML, BASE_64_ENCODING)
    }

}