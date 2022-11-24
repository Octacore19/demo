package com.octacore.demo.user

import org.springframework.security.core.userdetails.UserDetailsService

interface UserManager: UserDetailsService {
    /**
     * Create a new user with the supplied details.
     */
    fun createUser(user: User)

    /**
     * Update the specified user.
     */
    fun updateUser(user: User)

    /**
     * Remove the user with the given login name from the system.
     */
    fun deleteUser(username: String)

    /**
     * Modify the current user's password. This should change the user's password in the
     * persistent user repository (datbase, LDAP etc).
     * @param oldPassword current password (for re-authentication if required)
     * @param newPassword the password to change to
     */
    fun changePassword(oldPassword: String, newPassword: String)

    /**
     * Check if a user with the supplied login name exists in the system.
     */
    fun usernameExists(username: String): Boolean

    /**
     * Check if a user with the supplied email exists in the system.
     */
    fun userEmailExists(email: String): Boolean
}