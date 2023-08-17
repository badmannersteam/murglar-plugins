package com.badmanners.murglar.lib.core.login


/**
 * Single web login variant.
 */
data class WebLoginVariant(

    /**
     * Unique per [LoginResolver] login variant id.
     */
    val id: String,

    /**
     * Label, visible to the user.
     */
    val label: () -> String
)


/**
 * Single credentials login variant.
 */
data class CredentialsLoginVariant(

    /**
     * Unique per [LoginResolver] login variant id.
     */
    val id: String,

    /**
     * Label, visible to the user.
     */
    val label: () -> String,

    /**
     * List of credentials for this variant, required for credentials login.
     */
    val credentials: List<Credential>
) {

    /**
     * Single credential entry (login/email/password/token/cookie/etc).
     */
    data class Credential(

        /**
         * Unique per [CredentialsLoginVariant] credential id.
         */
        val id: String,

        /**
         * Label, visible to the user.
         */
        val label: () -> String,

        /**
         * Flag, indicating that visible value of this credential should be obscured by client (like "*****").
         */
        val secure: Boolean = false
    )
}


