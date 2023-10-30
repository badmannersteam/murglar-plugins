package com.badmanners.murglar.lib.core.localization

import com.badmanners.murglar.lib.core.service.Murglar


/**
 * Base messages for [Murglar]s.
 *
 * Implemented as interface for easy i18n on the different platforms and frameworks.
 */
interface Messages {
    val serviceName: String
    val loginWith: String
    val web: String
    val token: String
    val cookie: String
    val email: String
    val username: String
    val phone: String
    val password: String
    fun loginWith(
        web: Boolean = false,
        token: Boolean = false,
        cookie: Boolean = false,
        email: Boolean = false,
        username: Boolean = false,
        phone: Boolean = false
    ): String {
        val credentials = listOfNotNull(
            this.web.takeIf { web },
            this.token.takeIf { token },
            this.cookie.takeIf { cookie },
            this.email.takeIf { email },
            this.username.takeIf { username },
            this.phone.takeIf { phone }
        ).joinToString("/")
        return "$loginWith $credentials"
    }
    val copy: String
    val invalidCredentials: String
    val youAreLoggedIn: String
    val youAreLoggedInWithServiceName: String get() = "$youAreLoggedIn $serviceName"
    val youAreNotLoggedIn: String
    val youAreNotLoggedInWithServiceName: String get() = "$youAreNotLoggedIn $serviceName"
    val sessionUpdateFailed: String
    val sessionUpdateFailedWithServiceName: String get() = "$serviceName: $sessionUpdateFailed"
    val anErrorHasOccurred: String
    val illegalResponseFormat: String
    val sourceUrlUnavailable: String
    val trackHasNoLyrics: String
    val myTracks: String
    val myAlbums: String
    val myArtists: String
    val myPlaylists: String
    val myPodcasts: String
    val myAudiobooks: String
    val myHistoryTracks: String
    val recommendations: String
    val tracksSearch: String
    val albumsSearch: String
    val artistsSearch: String
    val playlistsSearch: String
    val podcastsSearch: String
    val audiobooksSearch: String
    val newReleases: String
    val popular: String
    val popularTracks: String
    val tracks: String
    val albums: String
    val compilations: String
    val similarArtists: String
    val artists: String
    val playlists: String
}