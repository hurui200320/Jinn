package info.skyblond.jinn.adif

/**
 * Reference: https://adif.org/310/ADIF_310.htm#Enumerations
 */

enum class AntPaths(val abbreviation: String) {
    GrayLine("G"),
    Other("O"),
    ShortPath("S"),
    LongPath("L");

    companion object {
        fun findByAbbreviation(abbreviation: String): AntPaths? =
                values().groupBy { it.abbreviation.toUpperCase() }[abbreviation.toUpperCase()]?.get(0)
    }
}

enum class Modes {
    AM, ARDOP, ATV, C4FM, V4, VOI, WINMOR, WSPR, AMTORFEC, ASCI, CHIP64, CHIP128, DOMINOF, FMHELL, FSK31, GTOR,
    HELL80, HFSK, JT4A, JT4B, JT4C, JT4D, JT4E, JT4F, JT4G, JT65A, JT65B, JT65C, MFSK8, MFSK16, PAC2, PAC3,
    PAX2, PCW, PSK10, PSK31, PSK63, PSK63F, PSK125, PSKAM10, PSKAM31, PSKAM50, PSKFEC31, PSKHELL, QPSK31,
    QPSK63, QPSK125, CLO, CONTESTI, DIGITALVOICE, DSTAR, FAX, FM, FSK441, FT8, JT6M, MSK144, MT63, DOMINOEX,
    PKT, PSK2K, Q15, SSTV, T10, THOR, THRBX, FSKHELL, ISCAT_A, ISCAT_B, JT9_1, JT9_2, JT9_5, JT9_10, JT9_30,
    JT9A, JT9B, JT9C, JT9D, JT9E, JT9E_FAST, JT9F, JT9F_FAST, JT9G, JT9G_FAST, JT9H, JT9H_FAST, JT44, JT65B2,
    JT65C2, FSQCALL, FT4, JS8, MFSK4, MFSK11, MFSK22, MFSK31, MFSK32, MFSK64, MFSK128, OLIVIA_4_125, OLIVIA_4_250,
    OLIVIA_8_250, OLIVIA_8_500, OLIVIA_16_500, OLIVIA_16_1000, OLIVIA_32_1000, OPERA_BEACON, OPERA_QSO, PAC4,
    PSK250, PSK500, PSK1000, QPSK250, QPSK500, SIM31, QRA64A, QRA64B, QRA64C, QRA64D, QRA64E, ROS_EME, ROS_HF,
    ROS_MF, LSB, USB,
    CHIP {
        override val subModes = listOf(CHIP64, CHIP128)
    },
    CW {
        override val subModes = listOf(PCW)
    },
    DOMINO {
        override val subModes = listOf(DOMINOEX, DOMINOF)
    },
    HELL {
        override val subModes = listOf(FMHELL, FSKHELL, HELL80, HFSK, PSKHELL)
    },
    ISCAT {
        override val subModes = listOf(ISCAT_A, ISCAT_B)
    },
    JT4 {
        override val subModes = listOf(JT4A, JT4B, JT4C, JT4D, JT4E, JT4F, JT4G)
    },
    JT9 {
        override val subModes = listOf(JT9_1, JT9_2, JT9_5, JT9_10, JT9_30, JT9A, JT9B, JT9C, JT9D, JT9E, JT9E_FAST, JT9F, JT9F_FAST, JT9G, JT9G_FAST, JT9H, JT9H_FAST)
    },
    JT65 {
        override val subModes = listOf(JT65A, JT65B, JT65B2, JT65C, JT65C2)
    },
    MFSK {
        override val subModes = listOf(FSQCALL, FT4, JS8, MFSK4, MFSK8, MFSK11, MFSK16, MFSK22, MFSK31, MFSK32, MFSK64, MFSK128)
    },
    OLIVIA {
        override val subModes = listOf(OLIVIA_4_125, OLIVIA_4_250, OLIVIA_8_250, OLIVIA_8_500, OLIVIA_16_500, OLIVIA_16_1000, OLIVIA_32_1000)
    },
    OPERA {
        override val subModes = listOf(OPERA_BEACON, OPERA_QSO)
    },
    PAC {
        override val subModes = listOf(PAC2, PAC3, PAC4)
    },
    PAX {
        override val subModes = listOf(PAX2)
    },
    PSK {
        override val subModes = listOf(FSK31, PSK10, PSK31, PSK63, PSK63F, PSK125, PSK250, PSK500, PSK1000, PSKAM10, PSKAM31, PSKAM50, PSKFEC31, QPSK31, QPSK63, QPSK125, QPSK250, QPSK500, SIM31)
    },
    QRA64 {
        override val subModes = listOf(QRA64A, QRA64B, QRA64C, QRA64D, QRA64E)
    },
    ROS {
        override val subModes = listOf(ROS_EME, ROS_HF, ROS_MF)
    },
    RTTY {
        override val subModes = listOf(ASCI)
    },
    RTTYM,
    SSB {
        override val subModes = listOf(LSB, USB)
    },
    THRB {
        override val subModes = listOf(THRBX)
    },
    TOR {
        override val subModes = listOf(AMTORFEC, GTOR)
    };

    open val subModes: List<Modes> = listOf()
    open val mainMode: Modes? = values().find { it.subModes.contains(this) }
}

enum class PropagationModes(val abbreviation: String) {
    AircraftScatter("AS"),
    AuroraE("AUE"),
    Aurora("AUR"),
    BackScatter("BS"),
    EchoLink("ECH"),
    EarthMoonEarth("EME"),
    SporadicE("ES"),
    F2Reflection("F2"),
    FieldAlignedIrregularities("FAI"),
    InternetAssisted("INTERNET"),
    Ionoscatter("ION"),
    IRLP("IRL"),
    MeteorScatter("MS"),
    TerrestrialOrAtmosphericRepeaterOrTransponder("RPT"),
    RainScatter("RS"),
    Satellite("SAT"),
    TransEquatorial("TEP"),
    TroposphericDucting("TR");
    companion object {
        fun findByAbbreviation(abbreviation: String): PropagationModes? =
                values().groupBy { it.abbreviation.toUpperCase() }[abbreviation.toUpperCase()]?.get(0)
    }
}