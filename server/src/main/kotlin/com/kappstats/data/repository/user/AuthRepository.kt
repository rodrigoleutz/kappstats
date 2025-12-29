package com.kappstats.data.repository.user

import com.kappstats.custom_object.email.Email
import com.kappstats.data.entity.user.AuthEntity
import com.kappstats.data.repository.Repository
import com.kappstats.domain.core.security.hashing.SaltedHash
import com.kappstats.model.user.Auth

interface AuthRepository : Repository<Auth, AuthEntity> {
    suspend fun add(value: Auth, saltedHash: SaltedHash): Auth?
    suspend fun deleteById(value: String): Boolean
    suspend fun getSaltedHash(email: Email): Pair<Auth, SaltedHash>?
}