package com.sotatek.weather.data.remote.errors

import com.google.gson.Gson
import java.lang.reflect.Type

interface JsonConverter {

    fun <T> toJson(data: T): String

    fun <T> fromJson(json: String, clazz: Class<T>): T

    fun <T> fromJson(json: String, typeOfT: Type): T
}

inline fun <reified T> JsonConverter.fromJson(json: String): T {
    return fromJson(json, T::class.java)
}

class JsonConverterImpl(private val gson: Gson) : JsonConverter {

    override fun <T> toJson(data: T): String {
        return gson.toJson(data)
    }

    override fun <T> fromJson(json: String, clazz: Class<T>): T {
        return gson.fromJson(json, clazz)
    }

    override fun <T> fromJson(json: String, typeOfT: Type): T {
        return gson.fromJson(json, typeOfT)
    }
}