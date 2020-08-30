package info.skyblond.jinn.utils

import info.skyblond.jinn.ConfigObject
import info.skyblond.jinn.DatabaseConfig
import info.skyblond.jinn.ServerAddr
import java.io.File

internal fun isDevelopEnv() = File("./config.yaml").exists()

internal fun prepareDatabaseConfig() {
    if (isDevelopEnv()) {
        // Is develop env
        ConfigObject.databaseConfig = DatabaseConfig(
            "logbook", false, "", "", "",
            listOf(ServerAddr("localhost", 27017))
        )
    } else {
        // Is test env
        println("---------- Test Env Detected ----------")
        ConfigObject.databaseConfig = DatabaseConfig(
            "logbook", true, "admin", "root", "example",
            listOf(ServerAddr("localhost", 27017))
        )
    }
}