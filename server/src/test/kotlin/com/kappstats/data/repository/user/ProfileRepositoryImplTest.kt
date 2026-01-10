package com.kappstats.data.repository.user

import com.kappstats.custom_object.username.Username
import com.kappstats.data.entity.user.ProfileEntity
import com.kappstats.data.remote.api.database.mongo.MongoApi
import com.kappstats.test_util.container.MongoTestContainer
import com.kappstats.model.user.Profile
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ProfileRepositoryImplTest {

    companion object {
        val profileTest = Profile(
            id = ObjectId().toHexString(),
            name = "Test User",
            username = Username("test123"),
            bio = "Test description"
        )
    }

    lateinit var profileRepository: ProfileRepository

    @BeforeEach
    fun setUp() {
        val mongoApi = MongoApi(
            MongoTestContainer.connectionString,
            "KAppStatsTests"
        )
        profileRepository = ProfileRepositoryImpl(mongoApi)
    }

    @Test
    fun `Check index in collection`() = runTest {
        val indexNames = profileRepository.generic.database.collection.listIndexes().toList()
            .map { it.getString("name") }
        assertContains(indexNames, "${ProfileEntity::name.name}_1")
        assertContains(indexNames, "${ProfileEntity::username.name}_1")
    }

    @Test
    fun `Add profile in database`() = runTest {
        val add = profileRepository.generic.add(profileTest)
        assert(add != null)
        assertEquals(profileTest.name, add?.name)
        val getByName = profileRepository.generic.getListByProperty(
            Profile::name, profileTest.name
        )
        assertEquals(1, getByName.size)
        assertEquals(profileTest.username, getByName.first().username)
        val getById = add?.id?.let {
            profileRepository.generic.getById(it)
        }
        assert(getById != null)
        assertEquals(profileTest.name, getById?.name)
    }
}