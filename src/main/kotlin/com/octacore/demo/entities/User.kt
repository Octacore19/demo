package com.octacore.demo.entities

import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    val email: String,
    var password: String,
    val firstName: String?,
    val lastName: String?,
)
