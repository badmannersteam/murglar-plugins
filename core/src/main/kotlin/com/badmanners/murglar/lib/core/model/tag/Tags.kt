package com.badmanners.murglar.lib.core.model.tag

import org.threeten.bp.LocalDate


/**
 * Tags abstraction, that can be used for downloaded track tagging - ID3/vorbis comments/etc.
 *
 * See [wiki.hydrogenaud.io](https://wiki.hydrogenaud.io/index.php?title=Tag_Mapping),
 * [picard-docs.musicbrainz.org](https://picard-docs.musicbrainz.org/en/appendices/tag_mapping.html)
 */
data class Tags(
    val title: String,
    val subtitle: String? = null,
    val artists: List<String>,
    val comment: String? = null,
    val album: String? = null,
    val albumArtist: String? = null,
    val trackNumber: Int? = null,
    val totalTracks: Int? = null,
    val diskNumber: Int? = null,
    val totalDisks: Int? = null,
    val releaseDate: LocalDate? = null,
    val genre: String? = null,
    val isExplicit: Boolean = false,
    val composer: String? = null,
    val performer: String? = null,
    val producer: String? = null,
    val mixer: String? = null,
    val engineer: String? = null,
    val author: String? = null,
    val writer: String? = null,
    val recordLabel: String? = null,
    val copyright: String? = null,
    val isrc: String? = null,
    val barcode: String? = null,
    val gain: String? = null,
    val peak: String? = null,
    val bpm: String? = null,
    val url: String? = null,
    val mediaId: String
) {

    val hasSubtitle: Boolean
        get() = !subtitle.isNullOrEmpty()

    val hasComment: Boolean
        get() = !comment.isNullOrEmpty()

    val hasAlbum: Boolean
        get() = !album.isNullOrEmpty()

    val hasAlbumArtist: Boolean
        get() = !albumArtist.isNullOrEmpty()

    val hasTrackNumber: Boolean
        get() = trackNumber != null

    val hasTotalTracks: Boolean
        get() = totalTracks != null

    val hasDiskNumber: Boolean
        get() = diskNumber != null

    val hasTotalDisks: Boolean
        get() = totalDisks != null

    val hasReleaseDate: Boolean
        get() = releaseDate != null

    val hasGenre: Boolean
        get() = !genre.isNullOrEmpty()

    val hasGain: Boolean
        get() = !gain.isNullOrEmpty()

    val hasPeak: Boolean
        get() = !peak.isNullOrEmpty()

    val hasBpm: Boolean
        get() = !bpm.isNullOrEmpty()

    val hasUrl: Boolean
        get() = !url.isNullOrEmpty()

    fun toBuilder(): Builder = Builder(
        title, subtitle, artists, comment, album, albumArtist, trackNumber, totalTracks,
        diskNumber, totalDisks, releaseDate, genre, isExplicit, gain, peak, bpm, url, mediaId
    )

    data class Builder(
        var title: String? = null,
        var subtitle: String? = null,
        var artists: List<String>? = null,
        var comment: String? = null,
        var album: String? = null,
        var albumArtist: String? = null,
        var trackNumber: Int? = null,
        var totalTracks: Int? = null,
        var diskNumber: Int? = null,
        var totalDisks: Int? = null,
        var releaseDate: LocalDate? = null,
        var genre: String? = null,
        var explicit: Boolean = false,
        var composer: String? = null,
        var performer: String? = null,
        var producer: String? = null,
        var mixer: String? = null,
        var engineer: String? = null,
        var author: String? = null,
        var writer: String? = null,
        var recordLabel: String? = null,
        var copyright: String? = null,
        var isrc: String? = null,
        var barcode: String? = null,
        var gain: String? = null,
        var peak: String? = null,
        var bpm: String? = null,
        var url: String? = null,
        var mediaId: String? = null
    ) {
        fun title(title: String) = apply { this.title = title }
        fun subtitle(subtitle: String?) = apply { this.subtitle = subtitle }
        fun artists(artists: List<String>) = apply { this.artists = artists }
        fun comment(comment: String?) = apply { this.comment = comment }
        fun album(album: String?) = apply { this.album = album }
        fun albumArtist(albumArtist: String?) = apply { this.albumArtist = albumArtist }
        fun trackNumber(trackNumber: Int?) = apply { this.trackNumber = trackNumber }
        fun totalTracks(totalTracks: Int?) = apply { this.totalTracks = totalTracks }
        fun diskNumber(diskNumber: Int?) = apply { this.diskNumber = diskNumber }
        fun totalDisks(totalDisks: Int?) = apply { this.totalDisks = totalDisks }
        fun releaseDate(releaseDate: LocalDate?) = apply { this.releaseDate = releaseDate }
        fun genre(genre: String?) = apply { this.genre = genre }
        fun explicit(explicit: Boolean) = apply { this.explicit = explicit }
        fun composer(composer: String) = apply { this.composer = composer }
        fun performer(performer: String) = apply { this.performer = performer }
        fun producer(producer: String) = apply { this.producer = producer }
        fun mixer(mixer: String) = apply { this.mixer = mixer }
        fun engineer(engineer: String) = apply { this.engineer = engineer }
        fun author(author: String) = apply { this.author = author }
        fun writer(writer: String) = apply { this.writer = writer }
        fun recordLabel(recordLabel: String) = apply { this.recordLabel = recordLabel }
        fun copyright(copyright: String) = apply { this.copyright = copyright }
        fun isrc(isrc: String) = apply { this.isrc = isrc }
        fun barcode(barcode: String) = apply { this.barcode = barcode }
        fun gain(gain: String?) = apply { this.gain = gain }
        fun peak(peak: String?) = apply { this.peak = peak }
        fun bpm(bpm: String?) = apply { this.bpm = bpm }
        fun url(url: String?) = apply { this.url = url }
        fun mediaId(mediaId: String) = apply { this.mediaId = mediaId }
        fun createTags() = Tags(
            requireNotNull(title), subtitle, requireNotNull(artists), comment,
            album, albumArtist, trackNumber, totalTracks, diskNumber, totalDisks, releaseDate,
            genre, explicit, composer, performer, producer, mixer, engineer, author, writer, recordLabel, copyright,
            isrc, barcode, gain, peak, bpm, url, requireNotNull(mediaId)
        )
    }
}