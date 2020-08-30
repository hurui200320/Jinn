package info.skyblond.jinn.cli

import info.skyblond.jinn.ConfigObject
import info.skyblond.jinn.DatabaseConfig
import info.skyblond.jinn.ServerAddr
import info.skyblond.jinn.utils.assertExit
import info.skyblond.jinn.utils.assertNotExit
import info.skyblond.jinn.utils.prepareDatabaseConfig
import org.junit.jupiter.api.Test

internal class TestConfigTest {
    @Test
    fun testDatabaseSuccessTest(){
        prepareDatabaseConfig()
        ConfigObject.reset()

        val test = TestConfig
        assertNotExit {
            test.main(listOf())
        }
    }

    @Test
    fun testDatabaseFailedTest(){
        // 2000 shouldn't be MongoDB port
        ConfigObject.databaseConfig = DatabaseConfig(
            "logbook", false, "", "",
            listOf(ServerAddr("localhost", 2000))
        )
        ConfigObject.reset()

        val test = TestConfig
        assertExit(5) {
            test.main(listOf())
        }
    }
}