package info.skyblond.jinn.cli

import com.charleskorn.kaml.Yaml
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import info.skyblond.jinn.ConfigObject
import info.skyblond.jinn.ConfigPOJO
import org.apache.commons.lang3.exception.ExceptionUtils
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess

interface RootCommand<T : CliktCommand> {
    /**
     * Root command should have some sub commands, this function
     * should return a it self with nesting subcommands
     */
    fun getAllSubcommands(): T
}

class Application : CliktCommand(), RootCommand<Application> {
    private val logger = LoggerFactory.getLogger(Application::class.java)

    private val configPath by option("-c", "--config", metavar = "path", help = "path to configuration file").default("./config.yaml")

    // parse the config to object
    override fun run() {
        ConfigObject.configFilePath = configPath
        val configFile = File(configPath)
        if (!configFile.exists()) {
            echo("Config file not found with given path: $configPath", err = true)
            exitProcess(2)
        }
        if (!configFile.isFile) {
            echo("$configPath is a directory.", err = true)
            exitProcess(21)
        }
        try {
            ConfigObject.config = Yaml.default.decodeFromString(ConfigPOJO.serializer(), configFile.readText(StandardCharsets.UTF_8))
            echo("Reading config from: $configPath")
        } catch (e: Exception) {
            logger.error(ExceptionUtils.getStackTrace(e))
            echo("Error when reading config file. Please refer to 'logs/app.log' for more information.")
            exitProcess(1)
        }
    }

    // define some shortcuts
    override fun aliases(): Map<String, List<String>> = mapOf(
            "QSO" to listOf("qso"),
            "import" to listOf("qso", "import"),
            "filter" to listOf("qso", "filter"),
            "list" to listOf("qso", "list")
    )

    override fun getAllSubcommands() = this.subcommands(
            QSO().getAllSubcommands(),
            Init().getAllSubcommands()
    )
}

class QSO : CliktCommand(name = "qso", help = "Manage QSO things"), RootCommand<QSO> {
    override fun run() = Unit

    override fun getAllSubcommands() = this.subcommands(
            Import(), List(), Filter()
    )

    class Import : CliktCommand(help = "Import QSO(s) from files") {
        override fun run() {
            echo("Hello World!")
            TODO()
        }
    }

    class List : CliktCommand(help = "Print all QSO(s) to stdout") {
        override fun run() {
            echo("Hello World!")
            TODO()
        }
    }

    class Filter : CliktCommand(help = "Filter QSO(s) and print to stdout") {
        override fun run() {
            echo("Hello World!")
            TODO()
        }
    }
}

class Init : CliktCommand(name = "init", help = "Initialize things"), RootCommand<Init> {
    override fun run() = Unit

    override fun getAllSubcommands() = this.subcommands(
            GenerateConfigFile(), InitDatabase()
    )

    class GenerateConfigFile : CliktCommand(name = "config", help = "Generate default config.yaml with given path") {
        private val logger = LoggerFactory.getLogger(GenerateConfigFile::class.java)

        override fun run() {
            val configFile = File(ConfigObject.configFilePath)
            when {
                configFile.exists() -> {
                    echo("File already exists: ${ConfigObject.configFilePath}", err = true)
                    exitProcess(17)
                }
                configFile.createNewFile() -> {
                    try {
                        configFile.writeText(Yaml.default.encodeToString(ConfigPOJO.serializer(), ConfigObject.config), StandardCharsets.UTF_8)
                        echo("Config file created: ${ConfigObject.configFilePath}")
                    } catch (e: Exception) {
                        logger.error(ExceptionUtils.getStackTrace(e))
                        echo("Error when creating config file. Please refer to logs/app.log for more information.")
                    }
                }
                else -> {
                    echo("Cannot create new file with given path: ${ConfigObject.configFilePath}", err = true)
                    exitProcess(1)
                }
            }
        }
    }

    class InitDatabase : CliktCommand(name = "database", help = "Initialize database for first use") {
        override fun run() {
            val database = ConfigObject.database
            database.useTransaction {
                // Create table qso_infos
                database.useConnection { conn ->
                    val sql = """
                        create table if not exists qso_infos (
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
                        
                        create unique index if not exists qso_record_uindex
                            on qso_infos (qso_date, qso_time, callsign, frequency, qso_mode);
                    """.trimIndent()
                    conn.createStatement().execute(sql)
                }
                // TODO creat other tables
            }
        }
    }
}
