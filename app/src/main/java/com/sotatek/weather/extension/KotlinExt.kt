package com.sotatek.weather.extension

import com.sotatek.weather.base.Either
import com.sotatek.weather.data.remote.errors.DomainError
import com.sotatek.weather.util.Constant

inline fun <reified T> Either<DomainError, T>.doIfFailure(callback: (error: DomainError) -> Unit) {
    if (this is Either.Failure) {
        callback(error)
    }
}

inline fun <reified T> Either<DomainError, T>.doIfSuccess(callback: (value: T) -> Unit) {
    if (this is Either.Value) {
        callback(value)
    }
}

fun getIconUrl(name: String): String {
    return "${Constant.ICON_URL}$name@2x.png"
}

fun Int.toFahrenheit(): Int {
    return (this * 9) / 5 + 32
}

