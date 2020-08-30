package info.skyblond.jinn.database

import info.skyblond.jinn.extension.jsonb
import kotlinx.serialization.Serializable
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.*
import java.lang.Appendable
import java.time.LocalDate
import java.time.LocalTime

interface QsoInfo : Entity<QsoInfo> {
    companion object : Entity.Factory<QsoInfo>()

    val id: Long
    var qsoDate: LocalDate
    var qsoTime: LocalTime
    var callsign: String
    var frequency: Long
    var qsoMode: String
    var signalReport: SignalReport
    var otherSideInfo: OperatorInfo
    var operatorInfo: OperatorInfo
    var contestInfo: ContestInfo
    var extraInfo: ExtraInfo
    var qslInfo: QslInfo
}

data class SignalReport(
    val sent: String,
    val received: String
)

data class OperatorInfo(
    val grid: String,
    val power: Double // TODO
)

data class ContestInfo(
    val name: String // TODO
)

data class ExtraInfo(
    val comment: String // TODO
)

data class QslInfo(
    val ok: Boolean // TODO
)

object QsoInfos : Table<QsoInfo>("qso_infos") {
    val id = long("id").primaryKey().bindTo { it.id }
    val qsoDate = date("qso_date").bindTo { it.qsoDate }
    val qsoTime = time("qso_time").bindTo { it.qsoTime }
    val callsign = varchar("callsign").bindTo { it.callsign }
    val frequency = long("frequency").bindTo { it.frequency }
    val qsoMode = varchar("qso_mode").bindTo { it.qsoMode }
    val signalReport = jsonb("signal_report", typeRef()).bindTo { it.signalReport }
    val otherSideInfo = jsonb("other_side_info", typeRef()).bindTo { it.otherSideInfo }
    val operatorInfo = jsonb("operator_info", typeRef()).bindTo { it.operatorInfo }
    val contestInfo = jsonb("contest_info", typeRef()).bindTo { it.contestInfo }
    val extraInfo = jsonb("extra_info", typeRef()).bindTo { it.extraInfo }
    val qslInfo = jsonb("qsl_info", typeRef()).bindTo { it.qslInfo }
}