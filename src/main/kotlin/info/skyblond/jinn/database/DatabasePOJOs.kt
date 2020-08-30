package info.skyblond.jinn.database

import org.bson.types.ObjectId
import java.time.LocalDate
import java.time.LocalDateTime

interface DatabasePOJO {
    @Suppress("PropertyName")
    val _id: ObjectId
}

data class QsoInfo(
    var qsoDateTime: LocalDateTime,
    var callsign: String,
    var frequency: Long,
    var qsoMode: String,
    var signalReport: SentRcvdPair,
    var otherSideInfo: OperatorInfo,
    var operatorInfo: OperatorInfo,
    var contestInfo: ContestInfo,
    var extraInfo: ExtraInfo,
    var qslInfo: QslInfo,
    override val _id: ObjectId = ObjectId.get()
) : DatabasePOJO

data class SentRcvdPair(
    val sent: String,
    val received: String,
    val comment: String = ""
)

data class OperatorInfo(
    val grid: String,
    val power: Double // TODO
)

data class ContestInfo(
    val contestName: String,
    val exchanges: SentRcvdPair
)

data class ExtraInfo(
    val comment: String // TODO
)

data class QslInfo(
    val lotw: Digital,
    val clublog: Digital,
    val qrz: Digital,
    val card: Card,
    val comment: String
) {
    data class Digital(
        val uploaded: Boolean,
        val uploadedDate: LocalDate?,
        val confirmed: Boolean,
        val confirmedDate: LocalDate?
    )

    data class Card(
        val sent: Sent,
        val received: Received
    ) {
        data class Sent(
            val sent: Boolean,
            val sentDate: LocalDate?,
            val required: Boolean,
            val requiredDate: LocalDate?,
            val via: String
        )

        data class Received(
            val received: Boolean,
            val receivedDate: LocalDate?,
            val required: Boolean,
            val requiredDate: LocalDate?,
            val via: String
        )
    }
}