package info.skyblond.jinn.utils

internal class SystemExitPreventedException(
    val exitStatusCode: Int
) : SecurityException()