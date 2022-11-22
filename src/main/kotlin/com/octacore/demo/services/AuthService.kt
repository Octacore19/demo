package com.octacore.demo.services

import com.octacore.demo.repo.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class AuthService(private val repo: UserRepository) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails {
        val user = repo.findByUsername(username)
        return if (user != null) User(user.username, "", arrayListOf())
        else throw UsernameNotFoundException("User not found with username: $username")
    }
}