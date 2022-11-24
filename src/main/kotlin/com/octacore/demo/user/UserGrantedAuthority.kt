package com.octacore.demo.user

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.Hibernate
import org.springframework.security.core.GrantedAuthority
import javax.persistence.*

@Entity(name = "authority")
@Table(name = "authorities")
data class UserGrantedAuthority(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    val user: User,
    val role: String,
) : GrantedAuthority {
    override fun getAuthority(): String = role
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as UserGrantedAuthority

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , role = $role )"
    }

}
