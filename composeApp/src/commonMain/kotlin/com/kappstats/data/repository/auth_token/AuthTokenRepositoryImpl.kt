package com.kappstats.data.repository.auth_token

import com.kappstats.data.entity.AuthTokenEntity
import com.kappstats.data.local.api.getKStore
import com.kappstats.data.local.security.CryptManager

class AuthTokenRepositoryImpl(
    private val cryptManager: CryptManager
): AuthTokenRepository {

    private val store = getKStore<AuthTokenEntity>().store

    override suspend fun getToken(): String? {
        val tokenEncrypted = store.get()?.token ?: return null
        val token = cryptManager.decrypt(tokenEncrypted)
        return token
    }

    override suspend fun setToken(token: String) {
        val encryptToken = cryptManager.encrypt(token)
        val authTokenEntity = AuthTokenEntity(encryptToken)
        store.set(authTokenEntity)
    }
}