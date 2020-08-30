package info.skyblond.jinn

import com.impossibl.postgres.jdbc.PGDataSource
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import info.skyblond.jinn.database.*
import info.skyblond.jinn.extension.*
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.notEq
import me.liuwj.ktorm.entity.*
import org.junit.jupiter.api.*
import java.time.LocalDate
import java.time.LocalTime

/**
 * The class used as Main class, to testing ideas
 */
internal class TDD {
    @Test
    fun tddTest() {
        val pgDataSource = PGDataSource()
        pgDataSource.serverName = ConfigObject.config.databaseConfig.host
        pgDataSource.portNumber = ConfigObject.config.databaseConfig.port
        pgDataSource.user = ConfigObject.config.databaseConfig.username
        pgDataSource.password = ConfigObject.config.databaseConfig.password
        pgDataSource.databaseName = ConfigObject.config.databaseConfig.database

        val config = HikariConfig()
        config.driverClassName = "com.impossibl.postgres.jdbc.PGDataSource"
        config.dataSource = pgDataSource
        config.addDataSourceProperty("cachePrepStmts", "true")
        config.addDataSourceProperty("prepStmtCacheSize", "250")
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")

        val hikariDataSource = HikariDataSource(config)

        val database = Database.connect(hikariDataSource, dialect = MyPostgreSqlDialect())

        database.sequenceOf(QsoInfos).forEach { it.delete() }

        database.sequenceOf(QsoInfos).add(QsoInfo {
            qsoDate = LocalDate.now()
            qsoTime = LocalTime.now()
            callsign = "BI1ETG"
            frequency = 14074000
            qsoMode = "FT-8"
            signalReport = SignalReport("-23db", "-12db")
            otherSideInfo = OperatorInfo("OM89cw", 100.0)
            operatorInfo = OperatorInfo("OM89cw", 100.0)
            contestInfo = ContestInfo("CQWW")
            extraInfo = ExtraInfo("?")
            qslInfo = QslInfo(false)
        })

        Assertions.assertEquals(database.sequenceOf(QsoInfos).count(), 1)
    }
}