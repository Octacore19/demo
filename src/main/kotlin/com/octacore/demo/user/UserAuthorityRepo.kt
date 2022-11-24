package com.octacore.demo.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserAuthorityRepo: JpaRepository<UserGrantedAuthority, Long> {
}