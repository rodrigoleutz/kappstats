package com.kappstats.data.remote.api.database.mongo

import com.kappstats.contracts.Model
import com.kappstats.data.entity.Entity
import com.kappstats.data.remote.api.database.Database
import com.kappstats.data.remote.api.database.TableIndex
import com.mongodb.client.model.Filters
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import org.koin.core.component.inject
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.jvmErasure

class MongoDatabaseImpl<M: Model, T: Entity<M>>(
    private val api: MongoApi,
    private val clazz: KClass<T>,
    private vararg val indexes: TableIndex<M, T> = arrayOf(),
) : Database<T> {

    override val collection: MongoCollection<T> =
        api.database.getCollection(clazz.java.simpleName, clazz.java)

    init {
        runBlocking {
            indexes.forEach { index ->
                val options =
                    if (index.isAscending) Indexes.ascending(index.property.name)
                    else Indexes.descending(index.property.name)
                val unique = IndexOptions().unique(index.isUnique)
                collection.createIndex(options, unique)
            }
        }
    }

    override suspend fun add(item: T): T? {
        val result = collection.insertOne(item)
        return if (result.wasAcknowledged()) {
            item
        } else {
            null
        }
    }

    override suspend fun addMany(items: List<T>): List<T>? {
        if (items.isEmpty()) {
            return emptyList()
        }
        val result = collection.insertMany(items)
        return if (result.wasAcknowledged() && result.insertedIds.size == items.size) {
            items
        } else {
            null
        }
    }

    override suspend fun delete(id: String): Boolean {
        return try {
            collection.deleteOne("_id" eq ObjectId(id)).deletedCount > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun deleteByProperty(
        property: KProperty1<T, Any?>,
        value: Any?,
    ): Boolean {
        return try {
            collection.deleteOne(property.name eq value).deletedCount > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun getByProperty(
        property: KProperty1<T, Any?>,
        value: Any?,
    ): T? {
        return try {
            collection.find(property eq value).firstOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getListByProperty(
        property: KProperty1<T, Any?>,
        value: Any?,
        limit: Int,
        skip: Int,
    ): List<T> {
        return try {
            collection.find(property eq value).skip(skip).limit(limit).toList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun <R> getListByPropertyList(
        property: KProperty1<T, R>,
        value: List<R>,
        limit: Int,
        skip: Int,
    ): List<T> {
        return try {
            val filterList = mutableListOf<Bson>()
            value.forEach {
                filterList.add(
                    property eq if (property.returnType.jvmErasure == ObjectId::class)
                        ObjectId(it.toString())
                    else it
                )
            }
            val filters = Filters.or(filterList)
            collection.find(filters).skip(skip).limit(limit).toList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getListByPropertyContains(
        property: KProperty1<T, Any?>,
        value: Any?,
        limit: Int,
        skip: Int,
    ): List<T> {
        return try {
            collection.find(property containsIgnoreCase value).skip(skip).limit(limit).toList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getById(id: String, limit: Int, skip: Int): T? {
        val objectId = try {
            ObjectId(id)
        } catch (e: IllegalArgumentException) {
            return null
        }
        return collection.find(Filters.eq("_id", objectId)).firstOrNull()
    }

    override suspend fun update(id: String, item: T): T? {
        val objectId = try {
            ObjectId(id)
        } catch (e: IllegalArgumentException) {
            return null
        }
        val result = collection.replaceOne(Filters.eq("_id", objectId), item)
        return if (result.modifiedCount > 0) item else null
    }

    override suspend fun getAllWithLimitAndSkip(limit: Int, skip: Int): List<T> {
        return collection.find().toList()
    }

    override suspend fun <V : Any> setField(
        id: String,
        fieldProperty: KProperty1<T, V>,
        value: V,
    ): Boolean {
        val objectId = try {
            ObjectId(id)
        } catch (e: IllegalArgumentException) {
            return false
        }
        val fieldName = fieldProperty.name
        val updateResult =
            collection.updateOne(Filters.eq("_id", objectId), Updates.set(fieldName, value))
        return updateResult.modifiedCount > 0
    }

    override suspend fun <E : Any> pushToList(
        id: String,
        listProperty: KProperty1<T, List<E>>,
        element: E,
    ): Boolean {
        val objectId = try {
            ObjectId(id)
        } catch (e: IllegalArgumentException) {
            return false
        }
        val fieldName = listProperty.name
        val updateResult =
            collection.updateOne(Filters.eq("_id", objectId), Updates.push(fieldName, element))
        return updateResult.modifiedCount > 0
    }

    override suspend fun <E : Any> pullFromList(
        id: String,
        listProperty: KProperty1<T, List<E>>,
        element: E,
    ): Boolean {
        val objectId = try {
            ObjectId(id)
        } catch (e: IllegalArgumentException) {
            return false
        }
        val fieldName = listProperty.name
        val updateResult =
            collection.updateOne(Filters.eq("_id", objectId), Updates.pull(fieldName, element))
        return updateResult.modifiedCount > 0
    }

    override suspend fun <E : Any> updateListItemByIndex(
        id: String,
        listProperty: KProperty1<T, List<E>>,
        index: Int,
        newElement: E,
    ): Boolean {
        val objectId = try {
            ObjectId(id)
        } catch (e: IllegalArgumentException) {
            return false
        }
        val fieldName = listProperty.name
        val indexedFieldName = "$fieldName.$index"
        val updateResult = collection.updateOne(
            Filters.eq("_id", objectId),
            Updates.set(indexedFieldName, newElement)
        )
        return updateResult.modifiedCount > 0
    }

    private infix fun <T, R> KProperty1<T, R>.eq(value: R): Bson = Filters.eq(this.name, value)
    private infix fun <R> String.eq(value: R): Bson = Filters.eq(this, value)
    private infix fun <T, R> KProperty1<T, R>.containsIgnoreCase(value: R): Bson =
        Filters.regex(this.name, ".*$value.*", "i")

    private infix fun <T, R> KProperty1<T, R>.contains(value: R): Bson =
        Filters.regex(this.name, ".*$value.*")

    private infix fun <T, R> KProperty1<T, R>.set(value: R): Bson = Updates.set(this.name, value)
}