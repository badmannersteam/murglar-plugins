package com.badmanners.murglar.lib.core.preference


/**
 * Metadata of service preference, provided to user.
 */
sealed interface Preference {
    val id: String
    val title: String
}


/**
 * Preference for run some [action] when clicked.
 */
data class ActionPreference(
    override val id: String,
    override val title: String,
    val summary: String?,
    val action: () -> Unit,
    val needConfirmation: Boolean,
    /**
     * If [needConfirmation] is true and this field is null, then client default confirmation text must be used.
     */
    val confirmationText: String? = null,
    val refreshAllAfterInvoke: Boolean = false
) : Preference


/**
 * Preference that shows to user value from [displayGetter] and copies to buffer content from [getter] when clicked.
 */
data class CopyPreference(
    override val id: String,
    override val title: String,
    val getter: () -> String,
    val displayGetter: () -> String,
) : Preference


/**
 * Edit text preference.
 */
data class EditPreference(
    override val id: String,
    override val title: String,
    val message: String?,
    val getter: () -> String,
    val setter: (String) -> Unit,
    val refreshAllAfterChange: Boolean = false
) : Preference


/**
 * Select from list preference.
 */
data class ListPreference(
    override val id: String,
    override val title: String,
    val message: String?,
    val nameGetter: (() -> String)?,
    val valueGetter: () -> String,
    val valueSetter: (String) -> Unit,
    val names: List<String>,
    val values: List<String>,
    val refreshAllAfterChange: Boolean = false
) : Preference


/**
 * Switch preference.
 */
data class SwitchPreference(
    override val id: String,
    override val title: String,
    val summary: String?,
    val getter: () -> Boolean,
    val setter: (Boolean) -> Unit,
    val refreshAllAfterChange: Boolean = false
) : Preference
