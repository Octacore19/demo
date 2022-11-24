package com.octacore.demo.entities

import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
)
