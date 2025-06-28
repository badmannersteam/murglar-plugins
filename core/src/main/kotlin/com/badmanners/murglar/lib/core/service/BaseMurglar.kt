package com.badmanners.murglar.lib.core.service

import com.badmanners.murglar.lib.core.decrypt.Decryptor
import com.badmanners.murglar.lib.core.decrypt.NoopDecryptor
import com.badmanners.murglar.lib.core.localization.DelegatingMessages
import com.badmanners.murglar.lib.core.localization.Messages
import com.badmanners.murglar.lib.core.log.LoggerMiddleware
import com.badmanners.murglar.lib.core.model.node.Node
import com.badmanners.murglar.lib.core.model.tag.Lyrics
import com.badmanners.murglar.lib.core.model.tag.Tags
import com.badmanners.murglar.lib.core.model.track.BaseTrack
import com.badmanners.murglar.lib.core.network.NetworkMiddleware
import com.badmanners.murglar.lib.core.notification.NotificationMiddleware
import com.badmanners.murglar.lib.core.preference.Preference
import com.badmanners.murglar.lib.core.preference.PreferenceMiddleware
import com.badmanners.murglar.lib.core.utils.RateLimit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Proxy
import java.util.Locale


/**
 * Base abstract impl of [Murglar], that aggregates common middlewares and handles locale management.
 */
abstract class BaseMurglar<Track : BaseTrack, Msg : Messages> @JvmOverloads protected constructor(
    override val id: String,
    private val localeToMessages: Map<Locale, Msg>,
    protected val preferences: PreferenceMiddleware,
    protected val network: NetworkMiddleware,
    protected val notifications: NotificationMiddleware,
    protected val logger: LoggerMiddleware,
    override val decryptor: Decryptor<Track> = NoopDecryptor()
) : Murglar<Track> {

    protected val messages: Msg = delegatingMessages()
    private val messagesHandler = delegatingMessagesHandler(messages)

    override val name by messages::serviceName

    override val availableLocales get() = localeToMessages.keys.toList()
    override var locale by messagesHandler::currentLocale

    override val murglarPreferences = emptyList<Preference>()

    override val rateLimits = emptyList<RateLimit>()

    override suspend fun onCreate() = Unit

    override fun hasLyrics(track: Track) = false

    override suspend fun getLyrics(track: Track): Lyrics = throw UnsupportedOperationException("getLyrics")

    override suspend fun getTags(track: Track, parent: Node?): Tags = Tags.Builder().apply {
        title = track.title
        subtitle = track.subtitle
        artists = track.artistNames
        album = track.albumName
        trackNumber = track.indexInAlbum
        diskNumber = track.volumeNumber
        releaseDate = track.albumReleaseDate
        genre = track.genre
        explicit = track.explicit
        gain = track.gain
        peak = track.peak
        url = track.serviceUrl
        mediaId = track.mediaId
    }.createTags()


    @Suppress("UNCHECKED_CAST")
    private fun delegatingMessages(): Msg {
        val messagesClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<Msg>
        return DelegatingMessages.from(messagesClass, localeToMessages)
    }

    @Suppress("UNCHECKED_CAST")
    private fun delegatingMessagesHandler(messages: Msg) =
        Proxy.getInvocationHandler(messages) as DelegatingMessages<Msg>
}