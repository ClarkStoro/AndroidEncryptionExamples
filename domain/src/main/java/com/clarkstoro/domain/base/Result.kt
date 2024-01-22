package com.clarkstoro.domain.base

sealed class Result<out T> {
    data class Success<T>(val value: T) : Result<T>()
    data class Failure(val ex: Exception) : Result<Nothing>()
}