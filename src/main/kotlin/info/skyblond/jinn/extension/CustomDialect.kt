package info.skyblond.jinn.extension

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.expression.SqlExpression
import me.liuwj.ktorm.expression.SqlFormatter
import me.liuwj.ktorm.support.postgresql.PostgreSqlDialect
import me.liuwj.ktorm.support.postgresql.PostgreSqlFormatter

open class MyPostgreSqlDialect : PostgreSqlDialect() {

    override fun createSqlFormatter(database: Database, beautifySql: Boolean, indentSize: Int): SqlFormatter {
        return MyPostgreSqlFormatter(database, beautifySql, indentSize)
    }
}

class MyPostgreSqlFormatter(database: Database, beautifySql: Boolean, indentSize: Int)
    : PostgreSqlFormatter(database, beautifySql, indentSize) {

    override fun visitUnknown(expr: SqlExpression): SqlExpression {
        return when (expr) {
            is AsJsonExpression -> {
                if (expr.left.removeBrackets) {
                    visit(expr.left)
                } else {
                    write("(")
                    visit(expr.left)
                    removeLastBlank()
                    write(") ")
                }

                // if already json, then do nothing, just make compiler happy
                if (!expr.alreadyJson) {
                    removeLastBlank()
                    write("::json ")
                    // shouldn't visit right tree
                }
                expr
            }
            is JsonAccessAsTextExpression<*> -> {
                // Json only, no need to add brackets
                visit(expr.left)

                write("->> ")

                visit(expr.right)

                expr
            }
            is JsonAccessExpression<*> -> {
                // Json only, no need to add brackets
                visit(expr.left)

                write("-> ")

                visit(expr.right)

                expr
            }
            else -> {
                super.visitUnknown(expr)
            }
        }
    }
}