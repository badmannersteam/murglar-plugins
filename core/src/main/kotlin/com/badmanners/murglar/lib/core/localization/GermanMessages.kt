package com.badmanners.murglar.lib.core.localization

import java.util.Locale


/**
 * German (Deutsch) implementation of [Messages].
 */
abstract class GermanMessages : Messages {

    companion object {
        val GERMAN = Locale.Builder().setLanguage("de").build()
    }

    override val loginWith = "Einloggen mit"
    override val web = "web"
    override val token = "token"
    override val cookie = "cookie"
    override val email = "email"
    override val username = "benutzername"
    override val phone = "telefon"
    override val password = "passwort"
    override val copy = "Kopieren"
    override val invalidCredentials = "Falsche Zugangsdaten!"
    override val youAreLoggedIn = "Sie sind eingeloggt"
    override val youAreNotLoggedIn = "Sie sind nicht eingeloggt"
    override val sessionUpdateFailed = "Aktualisierung der Sitzung fehlgeschlagen, Loggen Sie sich erneut in Ihr Konto ein!"
    override val anErrorHasOccurred = "Es ist ein Fehler aufgetreten:"
    override val illegalResponseFormat = "Antwort enthält keine gültige Antwort:"
    override val sourceUrlUnavailable = "Quelle-URL nicht verfügbar!"
    override val trackHasNoLyrics = "Titel hat keinen Liedtext"
    override val myTracks = "Meine Titel"
    override val myAlbums = "Meine Alben"
    override val myArtists = "Meine Künstler"
    override val myPlaylists = "Meine Playlists"
    override val myPodcasts = "Meine Podcasts"
    override val myAudiobooks = "Meine Hörbücher"
    override val myHistoryTracks = "Meine Verlauf Titel"
    override val recommendations = "Empfehlungen"
    override val tracksSearch = "Titel-Suche"
    override val albumsSearch = "Alben-Suche"
    override val artistsSearch = "Künstler-Suchen"
    override val playlistsSearch = "Playlists-Suche"
    override val podcastsSearch = "Podcasts-Suche"
    override val audiobooksSearch = "Hörbücher-Suche"
    override val newReleases = "Neue Veröffentlichungen"
    override val popular = "Beliebt"
    override val popularTracks = "Beliebte Titel"
    override val tracks = "Titel"
    override val albums = "Alben"
    override val compilations = "Kompilationen"
    override val similarArtists = "Ähnliche Künstler"
    override val artists = "Künstler"
    override val playlists = "Playlists"
}