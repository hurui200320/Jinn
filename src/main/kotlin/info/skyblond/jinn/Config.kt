package info.skyblond.jinn

import com.impossibl.postgres.jdbc.PGDataSource
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import info.skyblond.jinn.extension.MyPostgreSqlDialect
import kotlinx.serialization.Serializable
import me.liuwj.ktorm.database.Database

object ConfigObject {
    var configFilePath = ""
    var config = ConfigPOJO(
        ConfigPOJO.DatabaseConfig(
            "localhost",
            5432,
            "logbook",
            "logbook",
            "qOA\$&fHGOceoIqxD"
        )
    )

    // No need to change database on the fly
    private var internalDatabase: Database? = null
    val database: Database
        get() {
            if (internalDatabase == null) {
                val pgDataSource = PGDataSource()
                pgDataSource.serverName = config.databaseConfig.host
                pgDataSource.portNumber = config.databaseConfig.port
                pgDataSource.user = config.databaseConfig.username
                pgDataSource.password = config.databaseConfig.password
                pgDataSource.databaseName = config.databaseConfig.database

                val config = HikariConfig()
                config.driverClassName = "com.impossibl.postgres.jdbc.PGDataSource"
                config.dataSource = pgDataSource
                config.addDataSourceProperty("cachePrepStmts", "true")
                config.addDataSourceProperty("prepStmtCacheSize", "250")
                config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")

                val hikariDataSource = HikariDataSource(config)

                internalDatabase = Database.connect(hikariDataSource, dialect = MyPostgreSqlDialect())
            }
            return internalDatabase!!
        }
}

@Serializable
data class ConfigPOJO(
    val databaseConfig: DatabaseConfig
) {
    @Serializable
    data class DatabaseConfig(
        val host: String,
        val port: Int,
        val database: String,
        val username: String,
        val password: String
    )
}