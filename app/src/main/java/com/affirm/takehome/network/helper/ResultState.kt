package com.affirm.takehome.network.helper

sealed class ResultState<out T> {
    data class Success<out T>(val data: T) : ResultState<T>()
    data object Loading : ResultState<Nothing>()
    data class Failure(val error: Throwable) : ResultState<Nothing>()
}
