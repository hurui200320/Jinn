package info.skyblond.jinn.cli

import com.charleskorn.kaml.Yaml
import info.skyblond.jinn.ConfigObject
import info.skyblond.jinn.ConfigPOJO
import info.skyblond.jinn.utils.assertExit
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.charset.StandardCharsets


internal class InitTest {
    private fun initConfigEnvironment(): File {
        ConfigObject.configFilePath = "./${RandomStringUtils.randomAlphabetic(20)}.yaml"
        println("Test config file path: ${ConfigObject.configFilePath}")
        val configFile = File(ConfigObject.configFilePath)
        configFile.deleteRecursively()
        // Ensure no exists config file
        assertFalse(configFile.exists())
        return configFile
    }

    private fun cleanConfigEnvironment() = File(ConfigObject.configFilePath).deleteRecursively()

    @Test
    fun createConfigTest() {
        val configFile = initConfigEnvironment()
        val init = Init.getAllSubcommands()

        // ensure init config is functional
        init.main(listOf("config"))
        assertTrue(configFile.exists())
        assertTrue(configFile.isFile)
        assertEquals(
            Yaml.default.decodeFromString(ConfigPOJO.serializer(), configFile.readText(StandardCharsets.UTF_8)),
            ConfigObject.config
        )

        cleanConfigEnvironment()
    }

    @Test
    fun existsConfigTest() {
        val configFile = initConfigEnvironment()
        configFile.createNewFile()
        assertTrue(configFile.exists())

        val init = Init.getAllSubcommands()

        assertExit(17) {
            init.main(listOf("config"))
        }

        cleanConfigEnvironment()
    }
}