package com.badmanners.murglar.lib.core.login

import com.badmanners.murglar.lib.core.utils.contract.WorkerThread
import com.badmanners.murglar.lib.core.webview.WebViewProvider


/**
 * [LoginResolver] impl for cases when no login support required.
 */
class NoLoginResolver : LoginResolver {

    override val isLoginSupported = false

    override val isLogged = false

    override val loginInfo = "Login not supported"

    override val webLoginVariants = emptyList<WebLoginVariant>()

    @WorkerThread
    override fun webLogin(loginVariantId: String, webViewProvider: WebViewProvider): Boolean =
        throw UnsupportedOperationException("webLogin")

    override val credentialsLoginVariants = emptyList<CredentialsLoginVariant>()

    @WorkerThread
    override fun credentialsLogin(loginVariantId: String, args: Map<String, String>) =
        throw UnsupportedOperationException("credentialsLogin")

    override fun logout() {}
}