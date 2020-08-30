package info.skyblond.jinn.cli

import com.charleskorn.kaml.Yaml
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import info.skyblond.jinn.ConfigObject
import info.skyblond.jinn.ConfigPOJO
import info.skyblond.jinn.database.*
import org.apache.commons.lang3.exception.ExceptionUtils
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection
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

object Application : CliktCommand(), RootCommand<Application> {
    private val logger = LoggerFactory.getLogger(Application::class.java)

    private val configPath by option(
        "-c",
        "--config",
        metavar = "path",
        help = "path to configuration file"
    ).default("./config.yaml")

    // parse the config to object
    override fun run() {
        ConfigObject.configFilePath = configPath
        val configFile = File(configPath)
        if (!configFile.exists()) {
            echo("Config file not found with given path: $configPath", err = true)
            echo("Using default configuration instead, might be failed.", err = true)
            return
        } else if (configFile.isDirectory) {
            echo("$configPath is a directory.", err = true)
            exitProcess(21)
        } else {
            try {
                ConfigObject.readConfig()
                echo("Reading config from: $configPath")
            } catch (e: Exception) {
                logger.error(ExceptionUtils.getStackTrace(e))
                echo("Error when reading config file. Please refer to 'logs/app.log' for more information.")
                exitProcess(1)
            }
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
        QSO.getAllSubcommands(),
        Init.getAllSubcommands(),
        TestConfig
    )
}

object QSO : CliktCommand(name = "qso", help = "Manage QSO things"), RootCommand<QSO> {
    override fun run() = Unit

    override fun getAllSubcommands() = this.subcommands(
        Import, List, Filter
    )

    object Import : CliktCommand(help = "Import QSO(s) from files") {
        override fun run() {
            echo("Hello World!")
            TODO()
        }
    }

    object List : CliktCommand(help = "Print all QSO(s) to stdout") {
        override fun run() {
            echo("Hello World!")
            TODO()
        }
    }

    object Filter : CliktCommand(help = "Filter QSO(s) and print to stdout") {
        override fun run() {
            echo("Hello World!")
            TODO()
        }
    }
}

object Init : CliktCommand(name = "init", help = "Initialize things"), RootCommand<Init> {
    override fun run() = Unit

    override fun getAllSubcommands() = this.subcommands(
        GenerateConfigFile
    )

    object GenerateConfigFile : CliktCommand(name = "config", help = "Generate default config.yaml with given path") {
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
                        configFile.writeText(
                            Yaml.default.encodeToString(ConfigPOJO.serializer(), ConfigObject.config),
                            StandardCharsets.UTF_8
                        )
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
}

object TestConfig : CliktCommand(name = "test", help = "Test config file") {
    private val logger = LoggerFactory.getLogger(TestConfig::class.java)
    override fun run() {
        try {
            val database = ConfigObject.database //normal java driver usage
            val col = database.getCollection<DatabaseTestObject>() //KMongo extension method
            // test with writing a object
            val obj = DatabaseTestObject()
            col.insertOne(obj)
            require(col.findOne { QsoInfo::_id eq obj._id } != null){ "Cannot read the object that just write in." }
            col.deleteOne(QsoInfo::_id eq obj._id)
            col.drop()
            echo("Test ok!")
        } catch (e: Exception) {
            logger.error(ExceptionUtils.getStackTrace(e))
            echo("Error when testing configuration. Please refer to logs/app.log for more information.")
            exitProcess(5)
        }
    }
}


