package com.sotatek.weather.base

interface Storage {
    fun setString(key: String, value: String)
    fun getString(key: String): String?
}
