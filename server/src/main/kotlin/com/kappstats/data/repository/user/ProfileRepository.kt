package com.kappstats.data.repository.user

import com.kappstats.data.entity.user.ProfileEntity
import com.kappstats.data.repository.Repository
import com.kappstats.model.user.Profile

interface ProfileRepository: Repository<Profile, ProfileEntity> {

}