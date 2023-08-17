package com.badmanners.murglar.lib.core.model.node


/**
 * Node types list.
 * Not enum because must be easily extendable.
 */
object NodeType {
    const val TRACK = "track"
    const val ALBUM = "album"
    const val ARTIST = "artist"
    const val PLAYLIST = "playlist"
    const val PODCAST = "podcast"
    const val PODCAST_EPISODE = "podcast_episode"
    const val AUDIOBOOK = "audiobook"
    const val AUDIOBOOK_PART = "audiobook_part"
    const val RADIO = "radio"
    const val NODE = "node"
}