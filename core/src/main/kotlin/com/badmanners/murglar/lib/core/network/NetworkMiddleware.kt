package com.badmanners.murglar.lib.core.network

import com.badmanners.murglar.lib.core.service.Murglar
import com.badmanners.murglar.lib.core.webview.WebViewProvider
import java.net.HttpCookie


/**
 * Network middleware.
 * Used in [Murglar]s for network HTTP IO and cookie management.
 */
interface NetworkMiddleware {

    /**
     * Returns all cookies for passed domain.
     *
     * @param domain without schema and trailing dot
     */
    fun getCookies(domain: String): List<HttpCookie>

    /**
     * Returns cookie with passed name for passed domain if exists.
     *
     * @param domain without schema and trailing dot
     */
    fun getCookie(domain: String, name: String): HttpCookie?

    /**
     * Adds new cookie to cookie store.
     *
     * Should be used only for manual cookie creation (e.g. for adding known token to cookies).
     *
     * For cookie sync from the webview after login - see [WebViewProvider.startWebView]
     * and its `domainsForCookiesSync` parameter
     *
     * @param cookie [HttpCookie.getDomain] may contains trailing dot
     */
    fun addCookie(cookie: HttpCookie)

    /**
     * Clears all cookies from cookie store for passed domain.
     *
     * @param domains without trailing dot
     */
    fun clearCookiesForDomains(vararg domains: String)

    /**
     * Clears **all** cookies from cookie store.
     */
    fun clearAllCookies()

    /**
     * Executes network HTTP IO.
     */
    fun <T> execute(request: NetworkRequest, converter: ResponseConverter<T>): NetworkResponse<T>
}