package com.badmanners.murglar.lib.core.localization

import java.util.Locale


/**
 * Spanish (Espanol) implementation of [Messages].
 */
abstract class SpanishMessages : Messages {

    companion object {
        val SPANISH = Locale.Builder().setLanguage("es").build()
    }

    override val loginWith = "Inicia sesión con"
    override val web = "web"
    override val token = "token"
    override val cookie = "cookie"
    override val email = "correo electrónico"
    override val username = "nombre de usuario"
    override val phone = "teléfono"
    override val password = "contraseña"
    override val copy = "Copiar"
    override val invalidCredentials = "¡Credenciales no válidas!"
    override val youAreLoggedIn = "Has iniciado sesión"
    override val youAreNotLoggedIn = "No has iniciado sesión"
    override val sessionUpdateFailed = "Error al actualizar la sesión, ¡vuelva a iniciar sesión en su cuenta!"
    override val anErrorHasOccurred = "Ha ocurrido un error:"
    override val illegalResponseFormat = "La respuesta no contiene una respuesta válida:"
    override val sourceUrlUnavailable = "¡Url de origen no disponible!"
    override val trackHasNoLyrics = "La canción no tiene letras"
    override val myTracks = "Mis canciones"
    override val myAlbums = "Mis álbumes"
    override val myArtists = "Mis artistas"
    override val myPlaylists = "Mis listas de reproducción"
    override val myPodcasts = "Mis podcasts"
    override val myAudiobooks = "Mis audiolibros"
    override val myHistoryTracks = "Mi historial de canciones"
    override val recommendations = "Recomendaciones"
    override val tracksSearch = "Buscar canciones"
    override val albumsSearch = "Buscar álbumes"
    override val artistsSearch = "Buscar artistas"
    override val playlistsSearch = "Buscar listas de reproducción"
    override val podcastsSearch = "Buscar Podcasts"
    override val audiobooksSearch = "Buscar audiolibros"
    override val newReleases = "Nuevos lanzamientos"
    override val popular = "Popular"
    override val popularTracks = "Canciones populares"
    override val tracks = "Canciones"
    override val albums = "Álbumes"
    override val compilations = "Compilaciones"
    override val similarArtists = "Artistas similares"
    override val artists = "Artistas"
    override val playlists = "Listas de reproducción"
}