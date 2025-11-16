package com.badmanners.murglar.lib.core.webview

import com.badmanners.murglar.lib.core.network.NetworkMiddleware


/**
 * WebView provider, with help of which WebView login can be performed.
 */
interface WebViewProvider {

    /**
     * Performs web login through some kind of WebView.
     * This method is blocking!
     *
     * @param enableJS              whether JavaScript should be enabled or disabled
     * @param userAgent             required useragent or null for default
     * @param startUrl              initial login url
     * @param domainsForCookiesSync list of domains for which cookies must be synced after login
     * from webview cookies store to the [NetworkMiddleware] impl cookies store
     * @param resolver              url load policy resolver
     * @return true if successful, false if cancelled
     */
    suspend fun startWebView(
        enableJS: Boolean,
        userAgent: String?,
        startUrl: String,
        domainsForCookiesSync: List<String>,
        resolver: UrlLoadPolicyResolver
    ): Boolean


    /**
     * Resolver, that decides to load passed URL or not.
     */
    interface UrlLoadPolicyResolver {

        /**
         * Must be called by a webview before loading the requested url.
         *
         * @param url the url that is planned to be loaded
         * @return resolved load policy
         */
        fun resolveUrlLoadPolicy(url: String): UrlLoadPolicy

        /**
         * Must be called by a webview before loading the resource/xhr.
         *
         * @param url url of the resource/xhr that is planned to be loaded
         * @return resolved load policy
         */
        fun resolveResourceLoadPolicy(url: String): UrlLoadPolicy
    }


    /**
     * Url loading decision variants.
     */
    enum class UrlLoadPolicy {

        /**
         * Load url.
         */
        ALLOW_LOAD,

        /**
         * Discard requested url loading and load startUrl instead.
         */
        DISCARD_LOAD,

        /**
         * Load url, sync cookies and close webview.
         */
        ALLOW_LOAD_AND_FINISH
    }
}
