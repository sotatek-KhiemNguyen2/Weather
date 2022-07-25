package com.sotatek.weather.base

import android.app.Application
import com.sotatek.weather.data.remote.errors.BackendTypedError
import com.sotatek.weather.data.remote.errors.StandardError
import com.sotatek.weather.data.remote.errors.JsonConverter
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseService {

    @Inject
    lateinit var jsonConverter: JsonConverter

    @Inject
    lateinit var application: Application

    protected inline fun <V> eitherNetwork(
        errorClass: KClass<out BackendTypedError> = StandardError::class,
        action: () -> V
    ) = eitherNetwork(jsonConverter, errorClass, action)

}