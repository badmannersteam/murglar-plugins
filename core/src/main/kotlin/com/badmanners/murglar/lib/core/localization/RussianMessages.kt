package com.badmanners.murglar.lib.core.localization

import java.util.Locale


/**
 * Russian (Русский) implementation of [Messages].
 */
abstract class RussianMessages : Messages {

    companion object {
        val RUSSIAN = Locale.Builder().setLanguage("ru").build()
    }

    override val loginWith = "Войти через"
    override val web = "web"
    override val token = "токен"
    override val cookie = "cookie"
    override val email = "email"
    override val username = "логин"
    override val phone = "телефон"
    override val password = "пароль"
    override val copy = "Скопировать"
    override val invalidCredentials = "Неверные учетные данные!"
    override val youAreLoggedIn = "Вы авторизованы"
    override val youAreNotLoggedIn = "Вы не авторизованы"
    override val sessionUpdateFailed = "Не удалось обновить сессию, перезайдите в аккаунт!"
    override val anErrorHasOccurred = "Произошла ошибка:"
    override val illegalResponseFormat = "Ответ содержит невалидный контент:"
    override val sourceUrlUnavailable = "URL трека недоступен!"
    override val trackHasNoLyrics = "Нет текста песни"
    override val myTracks = "Мои треки"
    override val myAlbums = "Мои альбомы"
    override val myArtists = "Мои исполнители"
    override val myPlaylists = "Мои плейлисты"
    override val myPodcasts = "Мои подкасты"
    override val myAudiobooks = "Мои аудиокниги"
    override val myHistoryTracks = "История"
    override val recommendations = "Рекомендации"
    override val tracksSearch = "Поиск треков"
    override val albumsSearch = "Поиск альбомов"
    override val artistsSearch = "Поиск исполнителей"
    override val playlistsSearch = "Поиск плейлистов"
    override val podcastsSearch = "Поиск подкастов"
    override val audiobooksSearch = "Поиск аудиокниг"
    override val newReleases = "Новинки"
    override val popular = "Популярное"
    override val popularTracks = "Популярные треки"
    override val tracks = "Треки"
    override val albums = "Альбомы"
    override val compilations = "Сборники"
    override val similarArtists = "Похожие исполнители"
    override val artists = "Исполнители"
    override val playlists = "Плейлисты"
}