package com.sotatek.weather.data.remote.errors

sealed class ViewError {
    class ResourceError(val resId: Int, val code: String?) : ViewError()
    class StringError(val error: String) : ViewError()
}