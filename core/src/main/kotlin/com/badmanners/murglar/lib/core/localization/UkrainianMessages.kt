package com.badmanners.murglar.lib.core.localization

import java.util.Locale


/**
 * Ukrainian (Українська) implementation of [Messages].
 */
abstract class UkrainianMessages : Messages {

    companion object {
        val UKRAINIAN = Locale.Builder().setLanguage("uk").build()
    }

    override val loginWith = "Увійти через"
    override val web = "web"
    override val token = "токен"
    override val cookie = "cookie"
    override val email = "email"
    override val username = "логін"
    override val phone = "телефон"
    override val password = "пароль"
    override val copy = "Копіювати"
    override val invalidCredentials = "Неправильні облікові дані!"
    override val youAreLoggedIn = "Ви авторизовані"
    override val youAreNotLoggedIn = "Ви не авторизовані"
    override val sessionUpdateFailed = "Не вдалося оновити сесію, спробуйте увійти в акаунт ще раз!"
    override val anErrorHasOccurred = "Сталася помилка:"
    override val illegalResponseFormat = "Відповідь не відповідає вимогам:"
    override val sourceUrlUnavailable = "URL треку недоступне!"
    override val trackHasNoLyrics = "Не має тексту пісні"
    override val myTracks = "Мої треки"
    override val myAlbums = "Мої альбоми"
    override val myArtists = "Мої виконавці"
    override val myPlaylists = "Мої плейлісти"
    override val myPodcasts = "Мої подкасти"
    override val myAudiobooks = "Мої аудіокниги"
    override val myHistoryTracks = "Історія"
    override val recommendations = "Рекомендації"
    override val tracksSearch = "Пошук треків"
    override val albumsSearch = "Пошук альбомів"
    override val artistsSearch = "Пошук виконавців"
    override val playlistsSearch = "Пошук плейлістів"
    override val podcastsSearch = "Пошук подкастів"
    override val audiobooksSearch = "Пошук аудіокниг"
    override val newReleases = "Новинки"
    override val popular = "Популярне"
    override val popularTracks = "Популярні треки"
    override val tracks = "Треки"
    override val albums = "Альбоми"
    override val compilations = "Збірники"
    override val similarArtists = "Подібні виконавці"
    override val artists = "Виконавці"
    override val playlists = "Плейлісти"
}