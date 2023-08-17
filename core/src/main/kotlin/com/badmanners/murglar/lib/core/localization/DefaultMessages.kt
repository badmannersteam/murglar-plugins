package com.badmanners.murglar.lib.core.localization


/**
 * Default hardcoded English implementation of [Messages].
 */
abstract class DefaultMessages : Messages {
    override val illegalResponseFormat = "Answer doesn't contain valid response: "
    override val invalidCredentialsFormat = "Invalid credentials!"
    override val anErrorHasOccurred = "An error has occurred: "
    override val sessionUpdateFailed = "Session update failed, re-login to your account!"
    override val youAreLoggedIn get() = "You are logged in $serviceName"
    override val youAreNotLoggedIn get() = "You are not logged in $serviceName"
    override val trackHasNoLyrics = "Track has no lyrics"
    override val sourceUrlUnavailable = "Source url unavailable!"
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
    override val copyToken = "Copy token"
    override fun loginWith(
        web: Boolean, tokens: Boolean, cookies: Boolean, email: Boolean, username: Boolean, phone: Boolean
    ): String {
        val credentials = listOfNotNull(
            "web".takeIf { web },
            "tokens".takeIf { tokens },
            "cookie".takeIf { cookies },
            "email".takeIf { email },
            "username".takeIf { username },
            "phone".takeIf { phone }
        ).joinToString("/")
        return "Login with $credentials"
    }
}