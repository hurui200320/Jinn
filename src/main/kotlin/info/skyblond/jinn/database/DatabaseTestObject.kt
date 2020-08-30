package info.skyblond.jinn.database

import org.apache.commons.lang3.RandomStringUtils
import org.bson.types.ObjectId

data class DatabaseTestObject (
    val data: String = RandomStringUtils.randomAlphanumeric(100, 200),
    val _id: ObjectId = ObjectId.get()
)