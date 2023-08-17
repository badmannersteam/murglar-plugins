package com.badmanners.murglar.lib.core.decrypt

import com.badmanners.murglar.lib.core.model.track.BaseTrack
import com.badmanners.murglar.lib.core.model.track.source.Source

/**
 * Noop [Decryptor].
 */
class NoopDecryptor<Track : BaseTrack> : Decryptor<Track> {

    override fun isEncrypted(track: Track, source: Source) = false

    override val decryptionChunkSize get() = throw UnsupportedOperationException("decryptionChunkSize")

    override fun decrypt(content: ByteArray, offset: Int, length: Int, track: Track, source: Source) =
        throw UnsupportedOperationException("decrypt")
}