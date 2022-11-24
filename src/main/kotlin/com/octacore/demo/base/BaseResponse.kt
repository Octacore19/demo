package com.octacore.demo.base

data class BaseResponse<T>(
    val status: Boolean,
    val data: T? = null,
    val message: String? = null,
)
