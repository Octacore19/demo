package com.octacore.demo.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.octacore.demo.base.BaseResponse
import com.octacore.demo.user.User
import com.octacore.demo.user.UserRepository
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
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*
import java.util.stream.Collectors

@RequestMapping("auth/v1")
@RestController
class AuthController(
    private val authManager: AuthenticationManager,
    private val userRepo: UserRepository,
    private val pEncoder: PasswordEncoder,
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
        @RequestParam email: String,
        @RequestParam password: String
    ): ResponseEntity<LoginResponse> {
        try {
            val context = SecurityContextHolder.createEmptyContext()
            val a = UsernamePasswordAuthenticationToken(email, password)
            val auth = authManager.authenticate(a)
            context.authentication = auth
            SecurityContextHolder.setContext(context)

            val scope = auth.authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "))

            val token = JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withSubject(auth.name)
                .withIssuedAt(Date())
                .withClaim("scope", scope)
                .withExpiresAt(Date(System.currentTimeMillis() + (60 * 60 * 2)))
                .sign(Algorithm.RSA256(rsaPublicKey, rsaPrivateKey))
            return ResponseEntity.ok().body(LoginResponse(access_token = token, expires_at = 0, grant_type = "Bearer"))
        } catch (e: UsernameNotFoundException) {
            return ResponseEntity.notFound().build()
        } catch (e: BadCredentialsException) {
            return ResponseEntity.notFound().build()
        } catch (e: Exception) {
            return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("/signup")
    fun createNewUser(@RequestBody user: User): ResponseEntity<BaseResponse<*>> {
        return try {
            authService.createUser(user)
            ResponseEntity.ok().body(null)
        } catch (e: Exception) {
            val res = BaseResponse<Any>(status = false, message = e.localizedMessage)
            ResponseEntity(res, HttpStatus.BAD_REQUEST)
        }
    }
}