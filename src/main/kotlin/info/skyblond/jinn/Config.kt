package info.skyblond.jinn

import com.charleskorn.kaml.Yaml
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import info.skyblond.jinn.database.QsoInfo
import kotlinx.serialization.Serializable
import org.litote.kmongo.KMongo
import java.io.Closeable
import java.io.File
import java.nio.charset.StandardCharsets

object ConfigObject : Closeable {
    var configFilePath = ""
    var databaseConfig = DatabaseConfig(
        "logbook",
        false,
        "",
        "",
        "",
        listOf(ServerAddr("host1", 27017))
    )

    val config = ConfigPOJO(databaseConfig)


    private var internalMongoClient: MongoClient? = null
    private var internalMongoDatabase: MongoDatabase? = null

    val database: MongoDatabase
        get() {
            if (internalMongoDatabase == null) {
                if (internalMongoClient == null) {
                    internalMongoClient = KMongo.createClient(
                        MongoClientSettings.builder().apply {
                            applyToClusterSettings { builder ->
                                builder.hosts(
                                    databaseConfig.address.map { ServerAddress(it.host, it.port) }
                                )
                            }
                            if (databaseConfig.needAuthorization) {
                                credential(
                                    MongoCredential.createCredential(
                                        databaseConfig.username,
                                        databaseConfig.source,
                                        databaseConfig.password.toCharArray(),
                                    )
                                )
                            }
                        }.build()
                    )
                }
                internalMongoDatabase = internalMongoClient!!.getDatabase(databaseConfig.databaseName)
            }
            return internalMongoDatabase!!
        }

    /**
     * Read file with configFilePath and parse to config.
     */
    fun readConfig() {
        val config = Yaml.default.decodeFromString(
            ConfigPOJO.serializer(),
            File(configFilePath).readText(StandardCharsets.UTF_8)
        )

        databaseConfig = config.databaseConfig
    }

    /**
     * Reset internal status
     */
    fun reset(){
        internalMongoClient = null
        internalMongoDatabase = null
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}

@Serializable
data class ConfigPOJO(
    val databaseConfig: DatabaseConfig
)

@Serializable
data class DatabaseConfig(
    val databaseName: String,
    val needAuthorization: Boolean,
    val source: String,
    val username: String,
    val password: String,
    val address: List<ServerAddr>
)

@Serializable
data class ServerAddr(
    val host: String,
    val port: Int
)