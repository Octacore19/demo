package com.octacore.demo.exceptions

import org.springframework.security.core.AuthenticationException

class ExistingUserException(private val m: String, private val c: Throwable?) : AuthenticationException(m, c) {
    constructor(message: String) : this(message, null)
}