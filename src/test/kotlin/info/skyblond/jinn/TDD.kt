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

        database.useConnection { conn ->
            val sql = """
                create table if not exists qso_infos
                (
                    id              bigserial   not null
                        constraint qso_infos_pk
                            primary key,
                    qso_date        date        not null,
                    qso_time        time        not null,
                    callsign        varchar(20) not null,
                    frequency       bigint      not null,
                    qso_mode        varchar(50) not null,
                    signal_report   jsonb       not null,
                    other_side_info jsonb       not null,
                    operator_info   jsonb       not null,
                    contest_info    jsonb       not null,
                    extra_info      jsonb       not null,
                    qsl_info        jsonb       not null
                );
                
                comment on table qso_infos is 'Basic QSO information';
                
                comment on column qso_infos.qso_date is 'qso date in UTC';
                
                comment on column qso_infos.qso_time is 'qso time in UTC';
                
                comment on column qso_infos.callsign is 'his/her callsign';
                
                comment on column qso_infos.frequency is 'working freq, band come from this automatically';
                
                comment on column qso_infos.qso_mode is 'mode, such as FT8, ssb, cw, etc.';
                
                comment on column qso_infos.signal_report is 'Singnal report: {"sent": 599, "rcvd": 599}';
                
                comment on column qso_infos.other_side_info is 'other side''s info';
                
                comment on column qso_infos.operator_info is 'operator''s info';
                
                comment on column qso_infos.contest_info is 'contest info, such as exchange records';
                
                comment on column qso_infos.extra_info is 'Such as IOTA, Satellite, repeater ';
                
                comment on column qso_infos.qsl_info is 'LOTW, QSL card';
                
                alter table qso_infos
                    owner to logbook;
                
                create unique index if not exists qso_record_uindex
                    on qso_infos (qso_date, qso_time, callsign, frequency, qso_mode);
            """.trimIndent()

            conn.prepareStatement(sql).use { statement ->
                statement.executeQuery()
            }
        }

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