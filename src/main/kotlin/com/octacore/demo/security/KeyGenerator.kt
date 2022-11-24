package com.octacore.demo.security

import org.springframework.stereotype.Component
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.RSAKeyGenParameterSpec

@Component
class KeyGenerator private constructor() {

    private val keyPair: KeyPair

    init {
        val keyGenerator = KeyPairGenerator.getInstance("RSA")
        val spec = RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4)
        keyGenerator.initialize(spec)
        keyPair = keyGenerator.generateKeyPair()
    }

    fun getPublicKey(): RSAPublicKey = keyPair.public as RSAPublicKey

    fun getPrivateKey(): RSAPrivateKey = keyPair.private as RSAPrivateKey
}