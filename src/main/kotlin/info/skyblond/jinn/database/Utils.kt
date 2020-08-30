package info.skyblond.jinn.database

import com.mongodb.client.MongoCollection
import org.litote.kmongo.`in`
import org.litote.kmongo.eq

fun MongoCollection<DatabasePOJO>.insert(vararg objects: DatabasePOJO) {
    when {
        objects.isEmpty() -> return
        objects.size == 1 -> this.insertOne(objects[0])
        else -> this.insertMany(objects.toList())
    }
}

fun MongoCollection<DatabasePOJO>.delete(vararg objects: DatabasePOJO) {
    when {
        objects.isEmpty() -> return
        objects.size == 1 -> this.deleteOne(DatabasePOJO::_id eq objects[0]._id)
        else -> this.deleteMany(DatabasePOJO::_id `in` objects.map { it._id })
    }
}