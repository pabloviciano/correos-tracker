package net.kelmer.correostracker.data.prefs

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import javax.inject.Inject

class SharedPrefsManagerImpl @Inject constructor(
        private val sharedPreferences: SharedPreferences
) : SharedPrefsManager {
    companion object {
        private const val FEATURE_SEEN = "C_FEATURE_SEEN"
        private const val PREFERRED_THEME_MODE = "C_PREFERRED_THEME"
    }

    override fun hasSeenFeatureBlurb(): Boolean {
        return get(FEATURE_SEEN, false)
    }

    override fun setSeenFeatureBlurb() {
        set(FEATURE_SEEN, true)
    }

    override fun getPreferredTheme(): ThemeMode {
        return ThemeMode.fromIdx(get(PREFERRED_THEME_MODE, 0))
    }

    override fun setPreferredTheme(mode: ThemeMode) {
        set(PREFERRED_THEME_MODE, mode.idx)
    }

    /**
     * Put boolean in shared preference
     * @param key Key for boolean value
     * @param value value of boolean
     */
    operator fun set(key: String, value: Boolean) {
        sharedPreferences.edit()?.putBoolean(key, value)?.apply()
    }

    /**
     * Put String in shared preference
     * @param key Key for String value
     * @param value value of String
     */
    operator fun set(key: String, value: String) {
        sharedPreferences.edit()?.putString(key, value)?.apply()
    }

    /**
     * Put Int in shared preference
     * @param key Key for Int value
     * @param value value of Int
     */
    operator fun set(key: String, value: Int) {
        sharedPreferences.edit()?.putInt(key, value)?.apply()
    }

    /**
     * Get value for key in boolean format
     * @param key Key for boolean value
     * @param defaultValue If no value found for given key then return default value
     */
    operator fun get(key: String, defaultValue: Boolean): Boolean =
            sharedPreferences.getBoolean(key, defaultValue)

    /**
     * Get value for key in String format
     * @param key Key for String value
     * @param defaultValue If no value found for given key then return default value
     */
    operator fun get(key: String, defaultValue: String): String =
            sharedPreferences.getString(key, defaultValue)!!

    /**
     * Put long in shared preference
     * @param key Key for long value
     * @param value value of long
     */
    operator fun set(key: String, value: Long) {
        sharedPreferences.edit()?.putLong(key, value)?.apply()
    }

    /**
     * Get value for key in long format
     * @param key Key for long value
     * @param defaultValue If no value found for given key then return default value
     */
    operator fun get(key: String, defaultValue: Long): Long =
            sharedPreferences.getLong(key, defaultValue)


    /**
     * Get value for key in Int format
     * @param key Key for Int value
     * @param defaultValue If no value found for given key then return default value
     */
    operator fun get(key: String, defaultValue: Int): Int =
            sharedPreferences.getInt(key, defaultValue)

    /**
     * Clear SharedPreference
     */
    override fun clear() {
        sharedPreferences.edit()?.clear()?.apply()
    }
}