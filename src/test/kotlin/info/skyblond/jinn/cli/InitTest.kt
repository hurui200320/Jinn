package info.skyblond.jinn.cli

import com.charleskorn.kaml.Yaml
import info.skyblond.jinn.ConfigObject
import info.skyblond.jinn.ConfigPOJO
import info.skyblond.jinn.database.*
import info.skyblond.jinn.utils.assertExit
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.sequenceOf
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalTime


internal class InitTest {
    private val logger = LoggerFactory.getLogger(InitTest::class.java)

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
        val init = Init().getAllSubcommands()

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

        val init = Init().getAllSubcommands()

        assertExit(17) {
            init.main(listOf("config"))
        }

        cleanConfigEnvironment()
    }

    @Test
    fun initDatabaseTest() {
        // Require a real PostgreSQL
        val database = ConfigObject.database

        val init = Init().getAllSubcommands()
        init.main(listOf("database"))

        // test qso_infos
        val qsoInfo = QsoInfo {
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
        }
        val qsoInfoSequence = database.sequenceOf(QsoInfos)
        qsoInfoSequence.add(qsoInfo)
        logger.debug("Assigned Id: ${qsoInfo.id}")
        assertTrue(qsoInfoSequence.find { it.id eq qsoInfo.id } != null)
        qsoInfo.delete()


    }
}