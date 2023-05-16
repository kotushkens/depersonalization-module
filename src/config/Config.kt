package config

import org.slf4j.LoggerFactory
import java.util.*

interface Config {
    val name: String
    val specificFormats: Collection<String>
    val accountServiceDbConfig: DatabaseConfig
    val destinationDbConfig: DatabaseConfig
}

class ConfigImpl(
    getEnv: (String) -> String?
) : Config {
    private val logger = LoggerFactory.getLogger(javaClass)

    override val name: String = "depersonalization-module"

    override val specificFormats: Collection<String> = loadProperties("depersonalization").asMap.values

    override val accountServiceDbConfig = DatabaseConfigImpl("ACCOUNT_SERVICE", getEnv)

    override val destinationDbConfig = DatabaseConfigImpl("DESTINATION", getEnv)

    private val Properties.asMap get() = stringPropertyNames().associateWith { getProperty(it) }

    private fun loadProperties(fileName: String) = Properties().also { properties ->
        javaClass.getResourceAsStream("/${fileName}.properties").use { properties.load(it) }
    }
}