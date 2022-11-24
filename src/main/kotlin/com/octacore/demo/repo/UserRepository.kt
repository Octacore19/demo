package com.octacore.demo.repo

import com.octacore.demo.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findByEmail(email: String?): User?
}