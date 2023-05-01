package config

data class ConnectionPoolConfig(
    val name: String,
    val url: String,
    val driverClassName: String,
    val user: String,
    val password: String,
    val poolSize: IntRange
)