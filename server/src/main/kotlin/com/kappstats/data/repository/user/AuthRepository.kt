package com.kappstats.data.repository.user

import com.kappstats.data.entity.user.AuthEntity
import com.kappstats.data.repository.Repository
import com.kappstats.model.user.Auth

interface AuthRepository: Repository<Auth, AuthEntity> {

}