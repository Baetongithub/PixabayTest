package com.example.pixabaytest.presentation.utils

import android.content.Context
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefsManager @Inject constructor(@ApplicationContext context: Context) {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun getPrefsTag(): Int {
        return prefs.getInt(Constants.PREF_TAG, 0)
    }

    fun setPrefsBoolean(i: Int) {
        prefs.edit().putInt(Constants.PREF_TAG, i).apply()
    }
}