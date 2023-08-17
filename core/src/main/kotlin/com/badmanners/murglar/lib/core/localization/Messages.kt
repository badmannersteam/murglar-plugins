package com.badmanners.murglar.lib.core.localization

import com.badmanners.murglar.lib.core.service.Murglar


/**
 * Base messages for [Murglar]s.
 *
 * Implemented as interface for easy i18n on the different platforms and frameworks.
 */
interface Messages {
    val serviceName: String
    val illegalResponseFormat: String
    val invalidCredentialsFormat: String
    val anErrorHasOccurred: String
    val sessionUpdateFailed: String
    val sessionUpdateFailedWithServiceName: String get() = "$serviceName: $sessionUpdateFailed"
    val youAreLoggedIn: String
    val youAreNotLoggedIn: String
    val trackHasNoLyrics: String
    val sourceUrlUnavailable: String
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
    val copyToken: String
    fun loginWith(
        web: Boolean = false,
        tokens: Boolean = false,
        cookies: Boolean = false,
        email: Boolean = false,
        username: Boolean = false,
        phone: Boolean = false
    ): String
}