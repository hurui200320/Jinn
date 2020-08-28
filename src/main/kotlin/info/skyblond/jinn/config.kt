package info.skyblond.jinn

import kotlinx.serialization.*

object ConfigObject {
    var configFilePath = ""
    var config = ConfigPOJO(
        ConfigPOJO.DatabaseConfig(
            "PostgreSQL",
            "localhost",
            5432,
            "logbook",
            "logbook",
            "qOA\$&fHGOceoIqxD"
        )
    )
}

@Serializable
data class ConfigPOJO(
    val databaseConfig: DatabaseConfig
) {
    @Serializable
    data class DatabaseConfig(
        val type: String,
        val host: String,
        val port: Int,
        val database: String,
        val username: String,
        val password: String
    )
}