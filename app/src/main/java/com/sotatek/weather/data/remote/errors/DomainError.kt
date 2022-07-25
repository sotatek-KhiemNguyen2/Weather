package com.sotatek.weather.data.remote.errors

import com.google.gson.annotations.SerializedName
import com.sotatek.weather.R
import java.io.IOException

const val ERROR_CODE_SYSTEM = "-100"
const val ERROR_CODE_NETWORK_DNS = "-101"
const val ERROR_CODE_NETWORK_CONNECTION_TIME_OUT = "-102"
const val ERROR_CODE_NETWORK_SOCKET_TIME_OUT = "-103"
const val ERROR_CODE_NETWORK_SOCKET = "-104"
const val ERROR_CODE_NETWORK_SSL_HAND_SHAKE = "-105"
const val ERROR_CODE_NETWORK_SSL_PEER_UNVERIFIED = "-106"
const val ERROR_CODE_NETWORK_NO_NETWORK = "-107"

interface DomainErrorInterface {
    val errorCode: String
    val errorMessage: String
}

data class StandardError(
    @SerializedName("message") val message: String? = null,
    @SerializedName("cod") val code: String? = null,
) : BackendTypedError


sealed class DomainError : DomainErrorInterface {
    data class ApiError<T : BackendTypedError>(
        val httpCode: Int,
        val httpMessage: String,
        val error: T? = null
    ) : DomainError() {

        override val errorCode
            get() = when (error) {
                is StandardError -> when {
                    error.code != null -> error.code
                    else -> httpCode.toString()
                }
                else -> httpCode.toString()
            }

        override val errorMessage
            get() = when (error) {
                is StandardError -> error.message ?: ""
                else -> ""
            }
    }

    data class NetworkException(val throwable: Throwable) : DomainError() {
        override val errorCode: String
            get() = when (throwable) {
                is NoNetworkException -> ERROR_CODE_NETWORK_NO_NETWORK
                is java.net.UnknownHostException -> ERROR_CODE_NETWORK_DNS
                is java.net.ConnectException -> ERROR_CODE_NETWORK_CONNECTION_TIME_OUT
                is java.net.SocketTimeoutException -> ERROR_CODE_NETWORK_SOCKET_TIME_OUT
                is java.net.SocketException -> ERROR_CODE_NETWORK_SOCKET
                is javax.net.ssl.SSLHandshakeException -> ERROR_CODE_NETWORK_SSL_HAND_SHAKE
                is javax.net.ssl.SSLPeerUnverifiedException -> ERROR_CODE_NETWORK_SSL_PEER_UNVERIFIED
                else -> error("unexpected: $throwable")
            }

        override val errorMessage
            get() = throwable.javaClass.canonicalName ?: ""

        val errorResource
            get() = when (throwable) {
                is NoNetworkException -> R.string.exception_no_network
                is java.net.UnknownHostException -> R.string.exception_unknownhost
                is java.net.ConnectException -> R.string.exception_connect_timeout
                is java.net.SocketTimeoutException -> R.string.exception_socket_timeout
                is java.net.SocketException -> R.string.exception_socket
                is javax.net.ssl.SSLHandshakeException -> R.string.exception_ssl_handshake
                is javax.net.ssl.SSLPeerUnverifiedException -> R.string.exception_ssl_handshake
                else -> error("unexpected: $throwable")
            }
    }

    data class SystemException(val throwable: Throwable) : DomainError() {
        override val errorCode: String
            get() = ERROR_CODE_SYSTEM
        override val errorMessage: String
            get() = throwable.message.toString()
    }
}

class NoNetworkException(message: String = "No internet connection") : IOException(message)

interface BackendTypedError
