package com.kappstats.data.repository.user

import com.kappstats.data.data_source.entity.user.AuthTokenEntity
import com.kappstats.data.repository.Repository
import com.kappstats.model.user.AuthToken

interface AuthTokenRepository: Repository<AuthToken, AuthTokenEntity> {
    suspend fun getByAuthId(value: String): AuthToken?
}