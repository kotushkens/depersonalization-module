package config

import java.lang.Exception

interface DatabaseConfig {
    val connectionConfig: ConnectionPoolConfig

    class UnsupportedDatabase(msg: String) : RuntimeException(msg)
}

class DatabaseConfigImpl(poolName: String, private val getEnv: (String) -> String?) : DatabaseConfig {
    private val name = poolName.uppercase()

    private val jdbcUrl = ("DB_${name}_URL").asEnv()
    private val user = "DB_${name}_USER".asEnv()
    private val password = "DB_${name}_PASSWORD".asEnv()
    private val poolMin = getEnv("DB_${name}_POOL_MIN")?.toInt() ?: 1
    private val poolMax = getEnv("DB_${name}_POOL_MAX")?.toInt() ?: 2

    override val connectionConfig: ConnectionPoolConfig = ConnectionPoolConfig(
        name = poolName, url = jdbcUrl,
        driverClassName = selectDriver(jdbcUrl),
        user = user, password = password,
        poolSize = poolMin..poolMax,
    )

    private fun selectDriver(jdbcUrl: String): String = "org.postgresql.Driver" //todo check


    private fun String.asEnv() = getEnv(this) ?: throw Exception("WTF")
}