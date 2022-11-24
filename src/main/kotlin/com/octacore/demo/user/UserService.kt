package com.octacore.demo.user

import com.octacore.demo.exceptions.ExistingUserException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserService(
    private val repo: UserRepository,
    private val authRepo: UserAuthorityRepo,
    private val encoder: PasswordEncoder,
) : UserManager {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails {
        return username?.let { repo.findByUsername(it) } ?: throw UsernameNotFoundException("User not found with username: $username")
    }

    @Throws(ExistingUserException::class)
    override fun createUser(user: User) {
        val eUser = repo.findByUsername(user.username)
        if (eUser != null) {
            throw ExistingUserException("User with username, ${user.username}, already exists")
        }
        val nUser = user.copy(password = encoder.encode(user.password))
        repo.save(nUser)
        authRepo.save(UserGrantedAuthority(role = "USER", user = nUser))
    }

    @Throws(UsernameNotFoundException::class)
    override fun updateUser(user: User) {
        if (repo.existsById(user.id)) {
            repo.delete(user)
            repo.saveAndFlush(user)
        }
        throw UsernameNotFoundException("User not found with username: ${user.username}")
    }

    override fun deleteUser(username: String) {
        val user = repo.findByUsername(username) ?: throw UsernameNotFoundException("User not found with username: $username")
        repo.delete(user)
    }

    override fun changePassword(oldPassword: String, newPassword: String) {
        TODO("Not yet implemented")
    }

    override fun usernameExists(username: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun userEmailExists(email: String): Boolean {
        TODO("Not yet implemented")
    }
}