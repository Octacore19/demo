package com.octacore.demo.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.octacore.demo.base.BaseResponse
import com.octacore.demo.user.User
import com.octacore.demo.user.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.Instant
import java.util.*
import java.util.stream.Collectors

@RequestMapping("auth/v1")
@RestController
class AuthController(
    private val authManager: AuthenticationManager,
    private val rsaPublicKey: RSAPublicKey,
    private val rsaPrivateKey: RSAPrivateKey,
    @Value("\${auth0.audience}")
    private val audience: String,
    @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private val issuer: String,
    private val authService: UserService
) {

    @PostMapping("/login", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun login(
        @RequestParam username: String,
        @RequestParam password: String
    ): ResponseEntity<*> {
        try {
            val context = SecurityContextHolder.createEmptyContext()
            val a = UsernamePasswordAuthenticationToken(username, password)
            val auth = authManager.authenticate(a)
            context.authentication = auth
            SecurityContextHolder.setContext(context)

            val currentTime = Instant.now()
            val exp = currentTime.plusSeconds(60 * 60 * 2)
            val timeDiff = exp.epochSecond - currentTime.epochSecond

            val scope = auth.authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "))

            val token = JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withSubject(auth.name)
                .withIssuedAt(currentTime)
                .withClaim("scp", scope)
                .withExpiresAt(exp)
                .sign(Algorithm.RSA256(rsaPublicKey, rsaPrivateKey))
            return ResponseEntity.ok().body(
                LoginResponse(
                    access_token = token,
                    expires_at = timeDiff,
                    grant_type = "Bearer"
                )
            )
        } catch (e: UsernameNotFoundException) {
            return ResponseEntity<Any>(
                BaseResponse<Any>(
                    status = false,
                    message = e.localizedMessage,
                    code = HttpStatus.NOT_FOUND.value()
                ), HttpStatus.NOT_FOUND
            )
        } catch (e: BadCredentialsException) {
            return ResponseEntity<Any>(
                BaseResponse<Any>(
                    status = false,
                    message = e.localizedMessage,
                    code = HttpStatus.NOT_FOUND.value()
                ), HttpStatus.NOT_FOUND
            )
        } catch (e: Exception) {
            return ResponseEntity(
                BaseResponse<Any>(
                    status = false,
                    message = e.localizedMessage,
                    code = HttpStatus.BAD_REQUEST.value()
                ), HttpStatus.BAD_REQUEST
            )
        }
    }

    @PostMapping("/signup", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun createNewUser(
        @RequestParam username: String,
        @RequestParam password: String,
    ): ResponseEntity<BaseResponse<Any>> {
        return try {
            authService.createUser(User(username = username, password = password))
            val res = BaseResponse<Any>(
                status = true,
                message = "User created successfully",
                code = HttpStatus.CREATED.value()
            )
            ResponseEntity(res, HttpStatus.CREATED)
        } catch (e: Exception) {
            val res = BaseResponse<Any>(
                status = false,
                message = e.localizedMessage,
                code = HttpStatus.BAD_REQUEST.value()
            )
            ResponseEntity(res, HttpStatus.BAD_REQUEST)
        }
    }
}