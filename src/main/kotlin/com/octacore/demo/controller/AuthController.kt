package com.octacore.demo.controller

import com.octacore.demo.entities.User
import com.octacore.demo.model.LoginResponse
import com.octacore.demo.repo.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RequestMapping("auth/v1")
@RestController
class AuthController(
    private val userRepo: UserRepository,
    private val pEncoder: PasswordEncoder
) {

    @PostMapping("/login")
    fun login(@RequestParam email: String, @RequestParam password: String): ResponseEntity<LoginResponse> {
        return ResponseEntity.ok().body(null)
    }

    @PostMapping("/signup")
    fun createNewUser(@RequestBody user: User): ResponseEntity<*> {
        user.password = pEncoder.encode(user.password)
        userRepo.save(user)
        return ResponseEntity.ok().body(null)
    }
}