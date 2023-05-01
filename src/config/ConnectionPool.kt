import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import config.ConnectionPoolConfig
import org.slf4j.LoggerFactory
import javax.sql.DataSource

class ConnectionPool(
    private val config: ConnectionPoolConfig,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    val dataSource: DataSource by lazy { createDataSource(config) }

    private fun createDataSource(config: ConnectionPoolConfig): HikariDataSource {
        logger.info("Creating ${config.name} connection pool")
        val ds = HikariConfig()
        ds.poolName = config.name
        ds.driverClassName = config.driverClassName
        ds.jdbcUrl = config.url
        ds.minimumIdle = config.poolSize.first
        ds.maximumPoolSize = config.poolSize.last
        ds.username = config.user
        ds.password = config.password
        ds.validate()
        logger.info(config.name + " JDBC connection pool created: " + config.url)
        return HikariDataSource(ds)
    }
}