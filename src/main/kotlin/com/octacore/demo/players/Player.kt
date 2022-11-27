package com.octacore.demo.players

import org.hibernate.Hibernate
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "players")
data class Player(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long,
    val name: String,
    val age: Int,
    val nationality: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Player

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , name = $name , age = $age , nationality = $nationality )"
    }
}
