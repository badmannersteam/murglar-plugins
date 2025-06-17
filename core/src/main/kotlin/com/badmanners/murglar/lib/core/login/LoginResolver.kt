package com.badmanners.murglar.lib.core.login

import com.badmanners.murglar.lib.core.login.CredentialsLoginVariant.Credential
import com.badmanners.murglar.lib.core.network.NetworkMiddleware
import com.badmanners.murglar.lib.core.service.Murglar
import com.badmanners.murglar.lib.core.webview.WebViewProvider
import com.badmanners.murglar.lib.core.webview.WebViewProvider.UrlLoadPolicyResolver


/**
 * Login resolver.
 *
 * Manages login status and login/logout actions for [Murglar].
 *
 * If [Murglar] doesn't support/require login - use [NoLoginResolver].
 */
interface LoginResolver {

    val isLoginSupported: Boolean
        get() = webLoginVariants.isNotEmpty() || credentialsLoginVariants.isNotEmpty()

    /**
     * True, if user logged in the service, false otherwise.
     */
    val isLogged: Boolean

    /**
     * User readable info about login status.
     */
    val loginInfo: String

    /**
     * List of web login variants. If list is empty, then web login is not supported.
     */
    val webLoginVariants: List<WebLoginVariant>

    /**
     * Performs login through a web view.
     *
     * Login process:
     * 1. Client app gets [webLoginVariants] and provides them to user
     * 2. User selects one
     * 3. Client app calls this method with selected loginVariantId
     * 4. This method calls inside [WebViewProvider.startWebView] with required params
     * 5. User performs login in the webview
     * 6. Webview detects end of login according to the [UrlLoadPolicyResolver] and sync cookies from the webview
     * to the [NetworkMiddleware]
     * 7. Control returns to this method and if login successful this method performs login:
     *    - makes login requests through [NetworkMiddleware]
     *    - or extracts cookies/tokens/keys from [NetworkMiddleware] cookies
     *    - saves neccessary data to the [PreferenceMiddleware]
     *
     * @param loginVariantId user selected [WebLoginVariant.id].
     * @param webViewProvider web-view provider.
     * @return true if successful, false if login cancelled
     * @throws UnsupportedOperationException if login is not supported.
     * @throws IllegalStateException if login failed.
     */
    suspend fun webLogin(loginVariantId: String, webViewProvider: WebViewProvider): Boolean

    /**
     * List of credentials login variants. If list is empty, then credentials login is not supported.
     */
    val credentialsLoginVariants: List<CredentialsLoginVariant>

    /**
     * Performs credentials login.
     *
     * Login process:
     * 1. Client app gets [credentialsLoginVariants] and provides them to user
     * 2. User selects one
     * 3. Client app provides to user UI with inputs, based on [CredentialsLoginVariant.credentials]
     * 4. User fills inputs with actual credentials
     * 5. Client app passes selected variant id and user input args to this method
     * 6. Method performs login related actions and returns [CredentialLoginStep]. If it is
     *    - [TwoFARequiredStep] - client app provides to user UI with label with help text and input for 2FA code
     *    - [CaptchaRequiredStep] - client app provides to user UI with image of captcha and input for captcha value
     *    - [SuccessfulLogin] - client app finishes login
     * 7. Client app passes selected variant id and user input args + callbackArgs + 2FA/captcha arg to this method
     * 8. Go to p.6
     *
     * @param loginVariantId user selected [CredentialsLoginVariant.id].
     * @param args map of [[key -> value]] args, contain ALL available data
     *             regarding of step (including data from previous steps):
     *  - [[Credential.id] -> user input value for this credential]
     *  - all [CredentialLoginStep.callbackArgs]
     *  - [[TwoFARequiredStep.TWO_F_A_VALUE]/[CaptchaRequiredStep.CAPTCHA_VALUE] -> user input value of 2FA/captcha]
     *
     * @return next login step - see [CredentialLoginStep] descendants.
     *
     * @throws UnsupportedOperationException if login is not supported.
     * @throws IllegalStateException if login failed.
     */
    suspend fun credentialsLogin(loginVariantId: String, args: Map<String, String>): CredentialLoginStep

    /**
     * Performs logout from the service.
     */
    fun logout()
}