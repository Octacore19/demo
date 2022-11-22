package com.octacore.demo.model

import javax.persistence.*

@Entity
@Table(name = "tb_users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
)
