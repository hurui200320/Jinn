package info.skyblond.jinn

import info.skyblond.jinn.database.QslInfo
import info.skyblond.jinn.database.QsoInfo
import info.skyblond.jinn.utils.prepareDatabaseConfig
import org.junit.jupiter.api.Test
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.getCollection

/**
 * The class used as Main class, to testing ideas
 */
internal class TDD {
    @Test
    fun tddTest() {
        prepareDatabaseConfig()
        val database = ConfigObject.database
        val col = database.getCollection<QsoInfo>()

        col.find(QsoInfo::qslInfo / QslInfo::lotw / QslInfo.Digital::uploaded eq true)
            .forEach { println(it) }
    }
}