package com.badmanners.murglar.lib.core.model.radio

import com.badmanners.murglar.lib.core.utils.contract.Model
import java.io.Serializable


/**
 * Single radio setting, e.g. "mood"/"speed"/"language"/etc, provided from the plugin to the client.
 */
@Model
data class RadioSetting(

    /**
     * ID of the setting.
     */
    val id: String,

    /**
     * Human-readable name of the setting.
     */
    val name: String,

    /**
     * All entries of the radio setting, e.g.
     */
    val entries: List<Entry>,

    /**
     * 0-1 items if not [supportsMultipleEntries], 0-[entries].size otherwise.
     */
    val selectedEntriesIds: List<String>,

    /**
     * If multiple entries of the setting can be applied at the same time.
     */
    val supportsMultipleEntries: Boolean,

    /**
     * If setting must have at least 1 selected entry.
     */
    val mustHaveSelectedEntry: Boolean
) : Serializable {
    init {
        require(entries.isNotEmpty()) { "Setting must have at least 1 entry" }
        val from = if (mustHaveSelectedEntry) 1 else 0
        val to = if (supportsMultipleEntries) entries.size else 1
        require(selectedEntriesIds.size in from..to) {
            "selectedEntriesIds must contain from $from to $to entries ids, got ${selectedEntriesIds.size}"
        }
    }

    /**
     * Single entry of the radio setting, e.g. "calm" for the "mood" setting.
     */
    @Model
    data class Entry(

        /**
         * ID of the entry.
         */
        val id: String,

        /**
         * Human-readable name of the entry.
         */
        val name: String
    ) : Serializable
}

/**
 * Radio settings update that reflects user choice, provided from the client to the plugin.
 */
sealed class RadioSettingsUpdate : Serializable {

    /**
     * Change a single radio setting update request.
     */
    @Model
    data class Change(
        /**
         * ID of the setting.
         */
        val id: String,
        /**
         * ID's of the selected entries of the radio setting.
         */
        val selectedEntriesIds: List<String>
    ) : RadioSettingsUpdate()

    /**
     * Reset all settings update request.
     */
    data object Reset : RadioSettingsUpdate() {
        private fun readResolve(): Any = Reset
    }
}

/**
 * Returns an updated (with [RadioSettingsUpdate.Change]) copy of the [RadioSetting] list.
 */
fun List<RadioSetting>.update(update: RadioSettingsUpdate.Change) = map {
    when (it.id) {
        update.id -> it.copy(selectedEntriesIds = update.selectedEntriesIds)
        else -> it
    }
}