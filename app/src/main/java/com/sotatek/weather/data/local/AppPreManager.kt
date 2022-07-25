package com.sotatek.weather.data.local

import android.content.Context
import com.google.gson.Gson
import com.sotatek.weather.base.Storage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by khiemnt
 */

class AppPreManager @Inject constructor(@ApplicationContext context: Context) : Storage {

    companion object {
        const val SP_NAME = "weather"
    }

    private val pref = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

    override fun setString(key: String, value: String) {
        with(pref.edit()) {
            putString(key, value)
            apply()
        }
    }

    override fun getString(key: String): String? = pref.getString(key, null)

}