package com.octacore.demo.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.*
import org.springframework.security.web.SecurityFilterChain
import java.security.interfaces.RSAPublicKey
import java.util.*

@EnableWebSecurity
class WebSecurityConfig(
    private val rsaPublicKey: RSAPublicKey,

    @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private val issuerUri: String,
) {
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors().and().csrf().disable()
            .httpBasic(Customizer.withDefaults())
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().authorizeRequests()
            .antMatchers(HttpMethod.POST, "/auth/v1/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .oauth2ResourceServer().jwt()
        return http.build()
    }

    @Bean
    fun jwtDecoderBean(audValidator: AudienceValidator): JwtDecoder {
        return NimbusJwtDecoder
            .withPublicKey(rsaPublicKey)
            .build().apply {
                val withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri)
                val withAud = DelegatingOAuth2TokenValidator(withIssuer, audValidator)
                setJwtValidator(withAud)
            }
    }

    @Bean
    fun authenticationManager(http: HttpSecurity, userDetailsService: UserDetailsService): AuthenticationManager {
        val authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
        authenticationManagerBuilder.userDetailsService(userDetailsService)
        return authenticationManagerBuilder.build()
    }

    @Bean
    fun passwordEncoderBean(): PasswordEncoder = BCryptPasswordEncoder()
}