package info.skyblond.jinn.extension

import com.google.gson.Gson
import me.liuwj.ktorm.schema.BaseTable
import me.liuwj.ktorm.schema.Column
import me.liuwj.ktorm.schema.SqlType
import me.liuwj.ktorm.schema.TypeReference
import java.lang.reflect.Type
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

class JsonbSqlType<T : Any>(private val type: Type) : SqlType<T>(Types.JAVA_OBJECT, typeName = "jsonb") {
    private val gson = Gson()

    override fun doSetParameter(ps: PreparedStatement, index: Int, parameter: T) {
        ps.setString(index, gson.toJson(parameter))
    }

    override fun doGetResult(rs: ResultSet, index: Int): T? {
        val json = rs.getString(index)
        return if (json.isNullOrBlank()) {
            null
        } else {
            gson.fromJson(json, type)
        }
    }
}

fun <C : Any> BaseTable<*>.jsonb(
    name: String,
    typeReference: TypeReference<C>
): Column<C> {
    return registerColumn(name, JsonbSqlType(typeReference.referencedType))
}

internal class JsonSqlType<T : Any>(private val type: Type) : SqlType<T>(Types.JAVA_OBJECT, typeName = "json") {
    private val gson = Gson()

    override fun doSetParameter(ps: PreparedStatement, index: Int, parameter: T) {
        ps.setString(index, gson.toJson(parameter))
    }

    override fun doGetResult(rs: ResultSet, index: Int): T? {
        val json = rs.getString(index)
        return if (json.isNullOrBlank()) {
            null
        } else {
            gson.fromJson(json, type)
        }
    }
}

fun <C : Any> BaseTable<*>.json(
    name: String,
    typeReference: TypeReference<C>
): Column<C> {
    return registerColumn(name, JsonSqlType(typeReference.referencedType))
}
