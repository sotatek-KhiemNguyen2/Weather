package com.sotatek.weather.base

import com.google.gson.JsonSyntaxException
import com.sotatek.weather.data.remote.errors.BackendTypedError
import com.sotatek.weather.data.remote.errors.DomainError
import com.sotatek.weather.data.remote.errors.JsonConverter
import com.sotatek.weather.data.remote.errors.StandardError
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLPeerUnverifiedException
import kotlin.reflect.KClass

sealed class Either<out E : DomainError, out V> {
    data class Failure<E : DomainError>(val error: E) : Either<E, Nothing>()
    data class Value<out V>(val value: V) : Either<Nothing, V>()
}

fun <V> value(value: V): Either<Nothing, V> = Either.Value(value)

fun <E : DomainError> error(value: E): Either<E, Nothing> = Either.Failure(value)

inline fun <V> eitherNetwork(jsonConverter: JsonConverter, errorClass: KClass<out BackendTypedError> = StandardError::class, action: () -> V): Either<DomainError, V> =
        try {
            value(action())
        } catch (httpException: HttpException) {
            val httpCode = httpException.code()
            val httpMessage = httpException.message ?: ""
            val httpBody = httpException.response()?.errorBody()?.string()
            try {
                error(when (httpBody) {
                    null -> DomainError.ApiError(httpCode, httpMessage)
                    else -> DomainError.ApiError(httpCode, httpMessage, jsonConverter.fromJson(httpBody, errorClass.java))
                })
            } catch (e: Exception) {
                error(when {
                    (e is JsonSyntaxException && httpCode >= 500) -> DomainError.ApiError(httpCode, httpMessage, null)
                    else -> DomainError.SystemException(e)
                })
            }
        } catch (e: Exception) {
            error(when (e) {
                is UnknownHostException -> DomainError.NetworkException(e) //Unable to locate the server. Please check your network connection.
                is ConnectException -> DomainError.NetworkException(e) //Unable to connect to the server. Please check your network connection.
                is SocketTimeoutException -> DomainError.NetworkException(e) //The connection has timed out. Please try again.
                is SocketException -> DomainError.NetworkException(e) //There are some problems with the connection. Please try again.
                is SSLHandshakeException -> DomainError.NetworkException(e) // Your connection is not private.
                is SSLPeerUnverifiedException -> DomainError.NetworkException(e) // Your connection is not private.
                else -> DomainError.SystemException(e)
            })
        }
