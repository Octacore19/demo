package com.octacore.demo.user

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.Hibernate
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    private val username: String,
    private val password: String,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private val authorities: MutableCollection<UserGrantedAuthority>? = null
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? = authorities

    override fun getPassword(): String = password

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as User

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , email = $email , firstName = $firstName , lastName = $lastName , username = $username , password = $password, authorities = $authorities )"
    }
}
