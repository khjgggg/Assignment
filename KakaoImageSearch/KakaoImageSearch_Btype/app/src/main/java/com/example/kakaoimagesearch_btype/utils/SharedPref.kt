package com.example.kakaoimagesearch_btype.utils

import android.content.Context

/**
 * Preference Util class
 * 자주 사용하는 타입의 기능을 구현해서 모아둠.
 */
object SharedPref {
    const val PREF_KEY = "KAKAO_IMAGE_SEARCH"

    fun setBoolean(context: Context?, key: String?, value: Boolean) {
        context ?: return

        val sp = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        sp.edit().putBoolean(key, value).apply()
    }

    fun setInt(context: Context?, key: String?, value: Int) {
        context ?: return

        val sp = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        sp.edit().putInt(key, value).apply()
    }

    fun setLong(context: Context?, key: String?, value: Long) {
        context ?: return

        val sp = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        sp.edit().putLong(key, value).apply()
    }

    fun setString(context: Context?, key: String?, value: String?) {
        context ?: return

        val sp = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        sp.edit().putString(key, value).apply()
    }

    fun getBoolean(context: Context?, key: String?, defaultVal: Boolean): Boolean {
        context ?: return defaultVal

        val sp = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        return sp.getBoolean(key, defaultVal)
    }

    fun getInt(context: Context?, key: String?, defaultVal: Int): Int {
        context ?: return defaultVal

        val sp = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        return sp.getInt(key, defaultVal)
    }

    fun getLong(context: Context?, key: String?, defaultVal: Long): Long {
        context ?: return defaultVal

        val sp = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        return sp.getLong(key, defaultVal)
    }

    fun getString(context: Context?, key: String?, defaultVal: String): String {
        context ?: return defaultVal

        val sp = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        return sp.getString(key, defaultVal) ?: ""
    }
}