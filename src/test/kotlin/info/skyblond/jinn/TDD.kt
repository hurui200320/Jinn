package info.skyblond.jinn

import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import info.skyblond.jinn.database.*
import info.skyblond.jinn.utils.prepareDatabaseConfig
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.litote.kmongo.KMongo
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.getCollection
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * The class used as Main class, to testing ideas
 */
internal class TDD {
    @Test
    fun tddTest() {
        prepareDatabaseConfig()
        val database = ConfigObject.database //normal java driver usage
        val col = database.getCollection<QsoInfo>() //KMongo extension method

    }
}