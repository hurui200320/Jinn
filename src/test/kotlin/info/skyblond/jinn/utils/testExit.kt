package info.skyblond.jinn.utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.assertThrows

fun assertExit(code: Int, op: () -> Unit) {
    val disallowExitSecurityManager = DisallowExitSecurityManager(System.getSecurityManager())
    val originalSecurityManager = System.getSecurityManager()
    // Disable really exit jvm
    System.setSecurityManager(disallowExitSecurityManager)
    val exception = assertThrows<SystemExitPreventedException>({ "Should throw an Exception" }) {
        op()
    }
    System.setSecurityManager(originalSecurityManager)
    Assertions.assertEquals(exception.exitStatusCode, code)
}

fun assertExit(op: () -> Unit) {
    val disallowExitSecurityManager = DisallowExitSecurityManager(System.getSecurityManager())
    val originalSecurityManager = System.getSecurityManager()
    // Disable really exit jvm
    System.setSecurityManager(disallowExitSecurityManager)
    val exception = assertThrows<SystemExitPreventedException>({ "Should throw an Exception" }) {
        op()
    }

    System.setSecurityManager(originalSecurityManager)
}