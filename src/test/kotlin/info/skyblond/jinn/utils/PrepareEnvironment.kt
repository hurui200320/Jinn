package info.skyblond.jinn.utils

import info.skyblond.jinn.ConfigObject
import info.skyblond.jinn.DatabaseConfig
import info.skyblond.jinn.ServerAddr

internal fun prepareDatabaseConfig() {
    ConfigObject.databaseConfig = DatabaseConfig(
        "logbook", false, "", "",
        listOf(ServerAddr("localhost", 27017))
    )
}