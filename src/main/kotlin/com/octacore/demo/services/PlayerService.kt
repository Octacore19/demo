package com.octacore.demo.services

import com.octacore.demo.model.Player
import com.octacore.demo.repo.PlayerRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class PlayerService(private val repo: PlayerRepository) {
    fun getAll(): List<Player> = repo.findAll()

    fun getById(id: Long): Player = repo.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    fun create(player: Player): Player = repo.save(player)

    fun remove(id: Long) {
        if (repo.existsById(id)) {
            repo.deleteById(id)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun update(id: Long, player: Player): Player {
        return if (repo.existsById(id)) {
            player.id = id
            repo.save(player)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}