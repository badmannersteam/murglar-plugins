# Document provider

Murglar provides a custom `android.provider.DocumentsProvider`, that exposes user's music library through a
Storage Access Framework in a filesystem-like manner.

Provider `AUTHORITY` is `com.badmanners.murglar.provider.nodes`.

## 1. Public contracts

### 1.1 Standard SAF methods implemented

| Provider method           | Typical client entry point                           | Notes                                                           |
|---------------------------|------------------------------------------------------|-----------------------------------------------------------------|
| `queryRoots()`            | system picker                                        | Not usually called directly by client code; used by the SAF UI. |
| `queryDocument()`         | `ContentResolver.query(documentUri, ...)`            | Returns single root/document row.                               |
| `queryChildDocuments()`   | `ContentResolver.query(childrenUri, ...)`            | Lists directory content.                                        |
| `querySearchDocuments()`  | `ContentResolver.query(searchUri, ...)`              | Searches inside selected root.                                  |
| `findDocumentPath()`      | `DocumentsContract.findDocumentPath()`               | Returns document path like `My artists/Xxx/Albums/Yyy`. API 26+ |
| `openDocument()`          | `openInputStream()` / `openFileDescriptor(..., "r")` | Opens document content byte stream.                             |
| `openDocumentThumbnail()` | `ContentResolver.loadThumbnail()`                    | Returns cover art byte content.                                 |
| `call()`                  | `ContentResolver.call(...)`                          | Calls custom provider methods, listed below.                    |

### 1.2 Custom `call()` methods

| Method                     | Purpose                                                                  |
|----------------------------|--------------------------------------------------------------------------|
| `android:resetCache`       | Requests cache reset of a subtree before the next query.                 |
| `android:getAncestors`     | Backport of `findDocumentPath()` for any API version.                    |
| `android:registerOpenMode` | Registers `PIPE` or `SEEKABLE_SOCKET` open mode for the calling package. |

### 1.3 Root cursor columns

Returned when `projection == null` in `queryRoots()`:

| Column                    | Reference | Type     | Notes                                                                  |
|---------------------------|-----------|----------|------------------------------------------------------------------------|
| `Root.COLUMN_ROOT_ID`     | framework | `String` | Root ID used by SAF.                                                   |
| `Root.COLUMN_MIME_TYPES`  | framework | `String` | Newline-separated supported descendant MIME types.                     |
| `Root.COLUMN_FLAGS`       | framework | `Int`    | `Root.FLAG_SUPPORTS_IS_CHILD`, optionally `Root.FLAG_SUPPORTS_SEARCH`. |
| `Root.COLUMN_ICON`        | framework | `Int`    | App icon resource ID.                                                  |
| `Root.COLUMN_TITLE`       | framework | `String` | Music service title.                                                   |
| `Root.COLUMN_SUMMARY`     | framework | `String` | Murglar app name.                                                      |
| `Root.COLUMN_DOCUMENT_ID` | framework | `String` | Same value as root ID.                                                 |

### 1.4 Document cursor columns

Returned when `projection == null` in `queryDocument()` / `queryChildDocuments()` / `querySearchDocuments()`:

| Column                                 | Reference | Type           | Root/dir | File | Notes                                                                                |
|----------------------------------------|-----------|----------------|----------|------|--------------------------------------------------------------------------------------|
| `Document.COLUMN_DOCUMENT_ID`          | framework | `String`       | ✅        | ✅    | Stable provider ID for the current document.                                         |
| `Document.COLUMN_MIME_TYPE`            | framework | `String`       | ✅        | ✅    | For directories - `DocumentsContract.Document.MIME_TYPE_DIR`, audio MIME for tracks. |
| `Document.COLUMN_DISPLAY_NAME`         | framework | `String`       | ✅        | ✅    | File name with extension.                                                            |
| `Document.COLUMN_SUMMARY`              | framework | `String?`      | ❔        | ❔    | Second line text.                                                                    |
| `Document.COLUMN_LAST_MODIFIED`        | framework | `Long?`        | ❌        | ✅    | Last track access time in cache.                                                     |
| `Document.COLUMN_FLAGS`                | framework | `Int`          | ✅        | ✅    | See flags sections below.                                                            |
| `__flags`                              | custom    | `Int`          | ✅        | ✅    | See flags sections below.                                                            |
| `MediaStore.MediaColumns.TITLE`        | framework | `String`       | ❌        | ✅    | Full track title.                                                                    |
| `MediaStore.Audio.AudioColumns.ARTIST` | framework | `String`       | ❌        | ✅    | Artists joined with `;`.                                                             |
| `MediaStore.Audio.AudioColumns.ALBUM`  | framework | `String?`      | ❌        | ❔    | Album title                                                                          |
| `MediaStore.Audio.AudioColumns.TRACK`  | framework | `Int?`         | ❌        | ❔    | Track number in album.                                                               |
| `MediaStore.Audio.AudioColumns.YEAR`   | framework | `Int?`         | ❌        | ❔    | Album release year.                                                                  |
| `MediaFormat.KEY_BIT_RATE`             | framework | `Int?`         | ❌        | ❔    | Bits/sec track bitrate.                                                              |
| `Document.COLUMN_SIZE`                 | framework | `Long?`        | ❌        | ❔    | File size in bytes.                                                                  |
| `duration`                             | custom    | `Long`         | ❌        | ✅    | Track duration in milliseconds.                                                      |
| `track_alt`                            | custom    | `Int`          | ❌        | ✅    | 1-based file order within the returned list.                                         |
| `genre`                                | custom    | `String?`      | ❌        | ❔    | Genre.                                                                               |
| `rg_gain`                              | custom    | `String?`      | ❌        | ❔    | ReplayGain gain in a string form such as `"+1.23 db"`.                               |
| `rg_peak`                              | custom    | provider value | ❌        | ❔    | ReplayGain peak in a double form such as `0.98765`.                                  |

#### Additional supported columns (only if explicitly requested)

These are not part of the default projection but are supported:

| Column          | Type      | Applies to                       | Notes                                      |
|-----------------|-----------|----------------------------------|--------------------------------------------|
| `lyrics`        | `String?` | file, single-document query only | Plain lyrics, if available.                |
| `lyrics_synced` | `String?` | file, single-document query only | Synced lyrics in LRC format, if available. |

Projection rule:

- `projection = null` is safe
- if you pass a custom projection, include every column you plan to read
- do not expect `lyrics` / `lyrics_synced` in list queries; only single-document queries can populate them

### 1.5 Flags

#### `Root.COLUMN_FLAGS`

| Flag                          | Reference | Meaning                            |
|-------------------------------|-----------|------------------------------------|
| `Root.FLAG_SUPPORTS_IS_CHILD` | framework | Provider supports child checks.    |
| `Root.FLAG_SUPPORTS_SEARCH`   | framework | Search is available for this root. |

#### `Document.COLUMN_FLAGS`

| Flag                               | Reference/value | Meaning                                            |
|------------------------------------|-----------------|----------------------------------------------------|
| `Document.FLAG_SUPPORTS_THUMBNAIL` | framework       | Cover art can be requested with `loadThumbnail()`. |

#### `COLUMN_CUSTOM_FLAGS` (`"__flags"`)

| Flag                      |    Value | Applies to     | Meaning                            |
|---------------------------|---------:|----------------|------------------------------------|
| `FLAG_NO_SUBDIRECTORIES`  | `0x0001` | root/directory | No subdirectories below this node. |
| `FLAG_HAS_SUBDIRECTORIES` | `0x0002` | root/directory | At least one subdirectory exists.  |

### 1.6 Covers

- Available only when `Document.COLUMN_FLAGS` contains `Document.FLAG_SUPPORTS_THUMBNAIL`
- To get covers use `ContentResolver.loadThumbnail(fileUri, Size(...), null)`
- Provider returns large cover variant for size hints `>= 300x300`, small variant otherwise

### 1.7 Lyrics

- There is no flag for actual lyric availability
- Available only through `queryDocument(fileUri, projection)` by requesting `lyrics` and/or `lyrics_synced` columns
  explicitly
- Lyrics fetch should be deferred until the user actually needs it - don't fetch it on a list level to prevent N+1
  network calls

## 2. Integration flow

### 2.1 Pick a tree and persist access

External clients should start with the standard SAF picker. Do not attempt to access provider documents without a
user-granted tree URI.

```kotlin
val pickTree = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { treeUri ->
    if (treeUri == null)
        return

    contentResolver.takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

    // persist treeUri and proceed with import/browse
}

pickTree.launch(null)
```

Important:

- remember that user can pick any folder in the tree, not just the root of provider.

### 2.2 Build URIs only through `DocumentsContract`

Treat `documentId` as opaque; persist the tree URI and IDs you receive, but do not parse semantics out of them.

```kotlin
data class NodeUris(
    val treeUri: Uri,
) {
    val rootDocumentId: String get() = DocumentsContract.getTreeDocumentId(treeUri)

    val rootDocumentUri: Uri
        get() = DocumentsContract.buildDocumentUriUsingTree(
            treeUri,
            DocumentsContract.getTreeDocumentId(treeUri)
        )

    fun documentUri(documentId: String): Uri = DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId)

    fun childrenUri(parentDocumentId: String): Uri =
        DocumentsContract.buildChildDocumentsUriUsingTree(treeUri, parentDocumentId)

    fun searchUri(query: String): Uri =
        DocumentsContract.buildSearchDocumentsUri(AUTHORITY, rootDocumentId, query)
}

fun buildNodeUris(treeUri: Uri) = NodeUris(treeUri)
```

### 2.3 Browse/import using list queries

For import, one metadata query per directory is enough.

Use `track_alt` as a default provider order inside a listing; it is not album track number

**Avoid opening files or requesting lyrics during library scan!**

```kotlin
private val PROJECTION = arrayOf(
    DocumentsContract.Document.COLUMN_DOCUMENT_ID,
    DocumentsContract.Document.COLUMN_MIME_TYPE,
    DocumentsContract.Document.COLUMN_DISPLAY_NAME,
    DocumentsContract.Document.COLUMN_SUMMARY,
    DocumentsContract.Document.COLUMN_LAST_MODIFIED,
    DocumentsContract.Document.COLUMN_FLAGS,
    DocumentsContract.Document.COLUMN_SIZE,
    COLUMN_CUSTOM_FLAGS,
    COLUMN_TRACK_ALT,
    MediaStore.MediaColumns.TITLE,
    MediaStore.Audio.AudioColumns.ARTIST,
    COLUMN_DURATION,
    MediaStore.Audio.AudioColumns.ALBUM,
    MediaStore.Audio.AudioColumns.TRACK,
    MediaStore.Audio.AudioColumns.YEAR,
    COLUMN_GENRE,
    MediaFormat.KEY_BIT_RATE,
    COLUMN_REPLAY_GAIN_GAIN,
    COLUMN_REPLAY_GAIN_PEAK
)

sealed class ImportedNode {
    abstract val documentId: String
    abstract val mimeType: String
    abstract val displayName: String
    abstract val summary: String?
    abstract val flags: Int
    abstract val customFlags: Int

    data class ImportedDirectory(
        override val documentId: String,
        override val mimeType: String,
        override val displayName: String,
        override val summary: String?,
        override val flags: Int,
        override val customFlags: Int
    ) : ImportedNode()

    data class ImportedTrack(
        override val documentId: String,
        override val mimeType: String,
        override val displayName: String,
        override val summary: String?,
        override val flags: Int,
        override val customFlags: Int,
        val lastModified: Long,
        val size: Long?,
        val trackAlt: Int,
        val title: String,
        val artist: String,
        val durationMs: Long,
        val album: String?,
        val trackNumber: Int?,
        val year: Int?,
        val genre: String?,
        val bitrate: Int?,
        val replayGainGain: String?,
        val replayGainPeak: Double?
    ) : ImportedNode()
}

fun listChildren(contentResolver: ContentResolver, childrenUri: Uri): List<ImportedNode> = buildList {
    contentResolver.query(childrenUri, PROJECTION, null, null, null)?.use { c ->
        val idCol = c.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_DOCUMENT_ID)
        val mimeCol = c.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_MIME_TYPE)
        val nameCol = c.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_DISPLAY_NAME)
        val summaryCol = c.getColumnIndex(DocumentsContract.Document.COLUMN_SUMMARY)
        val lastModifiedCol = c.getColumnIndex(DocumentsContract.Document.COLUMN_LAST_MODIFIED)
        val documentFlagsCol = c.getColumnIndex(DocumentsContract.Document.COLUMN_FLAGS)
        val sizeCol = c.getColumnIndex(DocumentsContract.Document.COLUMN_SIZE)
        val customFlagsCol = c.getColumnIndex(COLUMN_CUSTOM_FLAGS)
        val trackAltCol = c.getColumnIndex(COLUMN_TRACK_ALT)
        val titleCol = c.getColumnIndex(MediaStore.MediaColumns.TITLE)
        val artistCol = c.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST)
        val durationCol = c.getColumnIndex(COLUMN_DURATION)
        val albumCol = c.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM)
        val trackNumberCol = c.getColumnIndex(MediaStore.Audio.AudioColumns.TRACK)
        val yearCol = c.getColumnIndex(MediaStore.Audio.AudioColumns.YEAR)
        val genreCol = c.getColumnIndex(COLUMN_GENRE)
        val bitrateCol = c.getColumnIndex(MediaFormat.KEY_BIT_RATE)
        val replayGainGainCol = c.getColumnIndex(COLUMN_REPLAY_GAIN_GAIN)
        val replayGainPeakCol = c.getColumnIndex(COLUMN_REPLAY_GAIN_PEAK)

        while (c.moveToNext()) {
            val documentId = c.getString(idCol)
            val mimeType = c.getString(mimeCol)
            val displayName = c.getString(nameCol)
            val summary = c.getNullableString(summaryCol)
            val flags = c.getInt(documentFlagsCol)
            val customFlags = c.getInt(customFlagsCol)

            val row = when (mimeType) {
                DocumentsContract.Document.MIME_TYPE_DIR -> ImportedDirectory(
                    documentId = documentId,
                    mimeType = mimeType,
                    displayName = displayName,
                    summary = summary,
                    flags = flags,
                    customFlags = customFlags
                )

                else -> ImportedTrack(
                    documentId = documentId,
                    mimeType = mimeType,
                    displayName = displayName,
                    summary = summary,
                    flags = flags,
                    customFlags = customFlags,
                    lastModified = c.getLong(lastModifiedCol),
                    size = c.getNullableLong(sizeCol),
                    trackAlt = c.getInt(trackAltCol),
                    title = c.getString(titleCol),
                    artist = c.getString(artistCol),
                    durationMs = c.getLong(durationCol),
                    album = c.getNullableString(albumCol),
                    trackNumber = c.getNullableInt(trackNumberCol),
                    year = c.getNullableInt(yearCol),
                    genre = c.getNullableString(genreCol),
                    bitrate = c.getNullableInt(bitrateCol),
                    replayGainGain = c.getNullableString(replayGainGainCol),
                    replayGainPeak = c.getNullableDouble(replayGainPeakCol)
                )
            }

            add(row)
        }
    }
}

private fun Cursor.getNullableString(index: Int): String? =
    if (index >= 0 && !isNull(index)) getString(index) else null

private fun Cursor.getNullableLong(index: Int): Long? =
    if (index >= 0 && !isNull(index)) getLong(index) else null

private fun Cursor.getNullableInt(index: Int): Int? =
    if (index >= 0 && !isNull(index)) getInt(index) else null

private fun Cursor.getNullableDouble(index: Int): Double? =
    if (index >= 0 && !isNull(index)) getDouble(index) else null
```

### 2.4 Request cache reset before a listing query (optional)

Murglar caches remote directory lists.
Reset is explicit and should be requested only when you really need to reload remote state.

```kotlin
fun resetCache(contentResolver: ContentResolver, documentUri: Uri) {
    contentResolver.call(
        Uri.parse("content://$AUTHORITY"),
        "android:resetCache",
        documentUri.toString(),
        null
    )
}
```

Important:

- `arg` must be a tree-scoped document URI string, not a raw `documentId`
- refresh registration is short-lived; call it immediately before the relevant `query()`
- do not call it on every scan/import pass - provider-side caching is intentional
- the best way is to provide to user a special "refresh" button that calls `resetCache()` + `query()` for the relevant
  directory

### 2.5 Get ancestors of a document (optional)

Returns a `path` of the document for showing in the UI - from tree root to direct parent of the target document:
`[My artists, Xxx, Albums, Yyy]`.

`findDocumentPath()` is available on API 26+, but for maximum compatibility use your own stored ancestry or
`android:getAncestors`

```kotlin
fun getAncestors(contentResolver: ContentResolver, documentUri: Uri): List<String> {
    val out = contentResolver.call(
        Uri.parse("content://$AUTHORITY"),
        "android:getAncestors",
        documentUri.toString(),
        null
    )
    return out?.getStringArray("ancestors")?.toList().orEmpty()
}
```

Important:

- `arg` must be a tree-scoped document URI string, not a raw `documentId`
- if target document is the tree root, result is `Bundle.EMPTY`

### 2.6 Open tracks, covers, metadata, lyrics

Don't trust file MIMEs/extensions - they are coarse and may not match exact container/codec,
so try to read stream magic/signature on open.

```kotlin
fun openTrackInputStream(contentResolver: ContentResolver, fileUri: Uri): InputStream {
    return checkNotNull(contentResolver.openInputStream(fileUri))
}

fun openTrackFd(contentResolver: ContentResolver, fileUri: Uri): ParcelFileDescriptor {
    return checkNotNull(contentResolver.openFileDescriptor(fileUri, "r"))
}

fun loadCover(contentResolver: ContentResolver, fileUri: Uri): Bitmap {
    return contentResolver.loadThumbnail(fileUri, Size(300, 300), null)
}

fun readLyrics(contentResolver: ContentResolver, fileUri: Uri): Pair<String?, String?> {
    contentResolver.query(fileUri, arrayOf(COLUMN_LYRICS, COLUMN_LYRICS_SYNCED), null, null, null)?.use { c ->
        if (c.moveToFirst()) {
            val plain = c.getNullableString(c.getColumnIndex(COLUMN_LYRICS))
            val synced = c.getNullableString(c.getColumnIndex(COLUMN_LYRICS_SYNCED))
            return plain to synced
        }
    }
    return null to null
}
```

### 2.7 Register seekable open mode (optional)

Default open mode for tracks content is `PIPE`.
If your playback stack supports `SEEKABLE_SOCKET` mode, register it before opening tracks.

#### `PIPE` (if plain byte stream is enough)

- Backed by `ParcelFileDescriptor.createPipe()`
- Client sees a forward-only stream
- No seek support from the transport itself
- File size may be unavailable
- Best default for generic players ()

#### `SEEKABLE_SOCKET` (if track seeking is required)

- Backed by `ParcelFileDescriptor.createSocketPair()`
- Uses a custom duplex protocol for file length reporting and seek requests
- For more info about this transport, see
  [Poweramp docs](https://github.com/maxmpz/powerampapi/blob/master/poweramp_provider_example/readme.md#seekable-sockets-pipes-file-descriptors-opendocument-cancellationsignal).
- Use only if your playback code understands this transport

```kotlin
fun registerSeekableMode(contentResolver: ContentResolver) {
    contentResolver.call(
        Uri.parse("content://$AUTHORITY"),
        "android:registerOpenMode",
        "SEEKABLE_SOCKET", // or "PIPE"
        null
    )
}
```

Important:

- registration is per calling package
- registration lives only in provider memory
- OS can restart the provider process, so the safest approach is to register before each `openFileDescriptor()`,
  that expects seek support

## Contacts and support

If you have any questions about integration or suggestions for improving this doc, feel free to contact us:

- Telegram - @badmannersteam
- E-mail - badmannersteam@gmail.com
