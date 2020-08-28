package info.skyblond.jinn

import info.skyblond.jinn.cli.Application

fun main(args: Array<String>) {
    Application().getAllSubcommands().main(args)
}
