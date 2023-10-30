package com.badmanners.murglar.lib.core.localization

import java.util.Locale


/**
 * Portuguese (Português) implementation of [Messages].
 */
abstract class PortugueseMessages : Messages {

    companion object {
        val PORTUGUESE = Locale.Builder().setLanguage("pt").build()
    }

    override val loginWith = "Entrar com"
    override val web = "web"
    override val token = "token"
    override val cookie = "cookie"
    override val email = "email"
    override val username = "nome de usuário"
    override val phone = "telefone"
    override val password = "senha"
    override val copy = "Copiar"
    override val invalidCredentials = "Credenciais inválidas!"
    override val youAreLoggedIn = "Está logado"
    override val youAreNotLoggedIn = "Você não está logado"
    override val sessionUpdateFailed = "Falha ao atualizar a sessão, faça login novamente em sua conta!"
    override val anErrorHasOccurred = "Ocorreu um erro:"
    override val illegalResponseFormat = "A resposta não contém uma resposta válida:"
    override val sourceUrlUnavailable = "Url de origem indisponível!"
    override val trackHasNoLyrics = "Faixa não tem letras"
    override val myTracks = "Minhas faixas"
    override val myAlbums = "Meus álbuns"
    override val myArtists = "Meus artistas"
    override val myPlaylists = "Minhas playlists"
    override val myPodcasts = "Meus podcasts"
    override val myAudiobooks = "Meus audiolivros"
    override val myHistoryTracks = "Meu histórico de faixas"
    override val recommendations = "Recomendações"
    override val tracksSearch = "Pesquisa de faixas"
    override val albumsSearch = "Pesquisar Álbuns"
    override val artistsSearch = "Procurar artistas"
    override val playlistsSearch = "Pesquisar playlist"
    override val podcastsSearch = "Pesquisa de Podcasts"
    override val audiobooksSearch = "Pesquisa de audiolivro"
    override val newReleases = "Novos lançamentos"
    override val popular = "Popular"
    override val popularTracks = "Faixas populares"
    override val tracks = "Músicas"
    override val albums = "Álbuns"
    override val compilations = "Compilações"
    override val similarArtists = "Artistas similares"
    override val artists = "Artistas"
    override val playlists = "Playlists"
}