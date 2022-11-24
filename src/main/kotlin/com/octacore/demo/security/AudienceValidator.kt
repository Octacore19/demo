package com.octacore.demo.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component

@Component
class AudienceValidator(
    @Value("\${auth0.audience}")
    private val audience: String,
) : OAuth2TokenValidator<Jwt> {
    override fun validate(token: Jwt?): OAuth2TokenValidatorResult {
        val error = OAuth2Error("invalid_token", "The required audience is missing", null)
        return if (token?.audience?.contains(audience) == true) {
            OAuth2TokenValidatorResult.success()
        } else OAuth2TokenValidatorResult.failure(error)
    }
}