package com.badmanners.murglar.lib.core.login


/**
 * Next credential login step.
 *
 * @see TwoFARequiredStep
 * @see CaptchaRequiredStep
 * @see SuccessfulLogin
 */
sealed interface CredentialLoginStep {

    /**
     * Args, that will be returned back to [LoginResolver.credentialsLogin] from client on the next step.
     *
     * Can be used for storing state data - last processed step/captcha sid/intermediate data/etc.
     */
    val callbackArgs: Map<String, String>
}


/**
 * Login step that requires 2FA code from user.
 */
data class TwoFARequiredStep(

    /**
     * Help text about 2FA (where to get code/etc).
     */
    val label: String,

    override val callbackArgs: Map<String, String>

) : CredentialLoginStep {
    companion object {
        /**
         * Args key for 2FA code value.
         */
        const val TWO_F_A_VALUE = "2fa_value"
    }
}


/**
 * Login step that requires captcha from user.
 */
data class CaptchaRequiredStep(

    /**
     * URL of captcha image.
     */
    val captchaImageUrl: String,

    override val callbackArgs: Map<String, String>

) : CredentialLoginStep {
    companion object {
        /**
         * Args key for captcha value.
         */
        const val CAPTCHA_VALUE = "captcha_value"
    }
}


/**
 * Successful login result.
 */
object SuccessfulLogin : CredentialLoginStep {
    override val callbackArgs = emptyMap<String, String>()
}
