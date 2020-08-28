package info.skyblond.jinn.utils

import java.security.Permission

internal class DisallowExitSecurityManager(private val securityManager: SecurityManager?) : SecurityManager() {
    /**
     * This is the one method we truly override in this class, all others are delegated.
     *
     * @param statusCode the exit status
     */
    override fun checkExit(statusCode: Int) {
        throw SystemExitPreventedException(statusCode)
    }

    // All other methods implemented and delegate to delegatedSecurityManager, if it is present.
    // Otherwise, they do nothing and allow the check to pass.
    override fun checkPermission(perm: Permission?) {
        securityManager?.checkPermission(perm)
    }
}