package com.badmanners.murglar.lib.core.decrypt

import com.badmanners.murglar.lib.core.model.track.BaseTrack
import com.badmanners.murglar.lib.core.model.track.source.Source

/**
 * Decryptor.
 *
 * Used for track content decryption (after extraction from container, if any).
 *
 * If no decryption needed - use [NoopDecryptor].
 */
interface Decryptor<Track : BaseTrack> {

    /**
     * Is decryption required for the track.
     * If false - just don't call [decrypt].
     */
    fun isEncrypted(track: Track, source: Source): Boolean

    /**
     * Size of the chunk (in bytes), that must be used with [decrypt]
     * as chunk length.
     *
     * @throws UnsupportedOperationException if content is not encrypted.
     */
    val decryptionChunkSize: Int

    /**
     * Performs decrypting of track content.
     *
     * @param content track content buffer, implementation must doesn't modify its content.
     * @param offset  start offset (in bytes) in the content buffer.
     * @param length  size of chunk (in bytes), that must be decrypted.
     * @param track   track, which content must be decrypted.
     * @param source  **resolved** source of track, which content must be decrypted.
     * @return new byte array with size from **0** (if no chunks decrypted due to errors)
     * to **length** (if all chunks decrypted successfully).
     * @throws UnsupportedOperationException if content is not encrypted.
     */
    fun decrypt(content: ByteArray, offset: Int, length: Int, track: Track, source: Source): ByteArray
}