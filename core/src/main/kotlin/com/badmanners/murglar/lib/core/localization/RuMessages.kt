package com.badmanners.murglar.lib.core.localization


/**
 * Russian hardcoded implementation of [Messages].
 */
abstract class RuMessages : Messages {
    override val illegalResponseFormat = "Ответ содержит невалидный контент: "
    override val invalidCredentialsFormat = "Неверные учетные данные!"
    override val anErrorHasOccurred = "Произошла ошибка: "
    override val sessionUpdateFailed = "Не удалось обновить сессию, перезайдите в аккаунт!"
    override val youAreLoggedIn get() = "Вы залогинены в $serviceName"
    override val youAreNotLoggedIn get() = "Вы не залогинены в $serviceName"
    override val trackHasNoLyrics = "Нет текста песни"
    override val sourceUrlUnavailable = "URL трека недоступен!"
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
    override val copyToken = "Скопировать токен"
    override fun loginWith(
        web: Boolean, tokens: Boolean, cookies: Boolean, email: Boolean, username: Boolean, phone: Boolean
    ): String {
        val credentials = listOfNotNull(
            "web".takeIf { web },
            "токены".takeIf { tokens },
            "cookie".takeIf { cookies },
            "email".takeIf { email },
            "логин".takeIf { username },
            "телефон".takeIf { phone }
        ).joinToString("/")
        return "Войти через $credentials"
    }
}