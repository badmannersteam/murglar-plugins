package com.badmanners.murglar.lib.core.localization

import java.util.Locale


/**
 * Default (English) implementation of [Messages].
 */
abstract class DefaultMessages : Messages {

    companion object {
        val DEFAULT = Locale.Builder().setLanguage("en").build()
    }

    override val loginWith = "Login with"
    override val web = "web"
    override val token = "token"
    override val cookie = "cookie"
    override val email = "email"
    override val username = "username"
    override val phone = "phone"
    override val password = "password"
    override val copy = "Copy"
    override val invalidCredentials = "Invalid credentials!"
    override val youAreLoggedIn = "You are logged in"
    override val youAreNotLoggedIn = "You are not logged in"
    override val sessionUpdateFailed = "Session update failed, re-login to your account!"
    override val anErrorHasOccurred = "An error has occurred:"
    override val illegalResponseFormat = "Answer doesn't contain valid response:"
    override val sourceUrlUnavailable = "Source url unavailable!"
    override val trackHasNoLyrics = "Track has no lyrics"
    override val myTracks = "My tracks"
    override val myAlbums = "My albums"
    override val myArtists = "My artists"
    override val myPlaylists = "My playlists"
    override val myPodcasts = "My podcasts"
    override val myAudiobooks = "My audiobooks"
    override val myHistoryTracks = "My history tracks"
    override val recommendations = "Recommendations"
    override val tracksSearch = "Tracks search"
    override val albumsSearch = "Albums search"
    override val artistsSearch = "Artists search"
    override val playlistsSearch = "Playlists search"
    override val podcastsSearch = "Podcasts search"
    override val audiobooksSearch = "Audiobooks search"
    override val newReleases = "New releases"
    override val popular = "Popular"
    override val popularTracks = "Popular tracks"
    override val tracks = "Tracks"
    override val albums = "Albums"
    override val compilations = "Compilations"
    override val similarArtists = "Similar artists"
    override val artists = "Artists"
    override val playlists = "Playlists"
}