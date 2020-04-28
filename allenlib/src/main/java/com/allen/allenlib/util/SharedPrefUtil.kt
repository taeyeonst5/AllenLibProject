package com.allen.allenlib.util

import android.content.Context

abstract class SharedPrefUtil(private val context: Context) {

    abstract val fileName: String

    open val sharedPreferences by lazy {
        context.applicationContext.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    }

    fun getPrefString(key: String): String {
        return sharedPreferences.getString(key, "").toString()
    }

    fun setPrefString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getPrefInt(key: String, default: Int): Int {
        return sharedPreferences.getInt(key, default)
    }

    fun setPrefInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun getPrefBoolean(key: String, default: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, default)
    }

    fun setPrefBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    //sample
//    var lastSelectBubble: String
//        get() {
//            return sharedPreferences.getString("last_select_bubble", "")!!
//        }
//        set(value) {
//            sharedPreferences.edit().putString("last_select_bubble", value).apply()
//        }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}