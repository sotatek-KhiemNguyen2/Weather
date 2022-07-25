package com.sotatek.weather.base

import androidx.lifecycle.ViewModel
import com.sotatek.weather.R
import com.sotatek.weather.data.remote.errors.DomainError
import com.sotatek.weather.data.remote.errors.ViewError
import kotlinx.coroutines.flow.*

/**
 * Created by khiemnt
 */

abstract class BaseViewModel : ViewModel() {
    private val _progressStateFlow = MutableStateFlow(false)
    val progressFlow = _progressStateFlow.asStateFlow()

    private val _errorsFlow = MutableSharedFlow<DomainError>(0, 1)

    val errorsFlow: Flow<ViewError> = _errorsFlow.asSharedFlow().map { domainError ->
        when {
            domainError is DomainError.NetworkException -> {
                ViewError.ResourceError(domainError.errorResource, domainError.errorCode)
            }
            domainError.errorMessage.isEmpty() -> {
                ViewError.ResourceError(R.string.error_something_went_wrong, domainError.errorCode)
            }
            else -> {
                ViewError.StringError(domainError.errorMessage)
            }
        }
    }

    suspend fun emitError(error: DomainError) {
        _errorsFlow.emit(error)
    }

    fun emitLoadingState(isLoading: Boolean) {
        _progressStateFlow.value = isLoading
    }

}