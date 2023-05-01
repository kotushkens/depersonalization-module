package config

import org.slf4j.LoggerFactory

interface Config {
    val name: String
    val accountServiceDbConfig: DatabaseConfig
}

class ConfigImpl(
    getEnv: (String) -> String?
) : Config {
    private val logger = LoggerFactory.getLogger(javaClass)

    override val name: String = "depersonalization-module"

    override val accountServiceDbConfig = DatabaseConfigImpl("ACCOUNT_SERVICE", getEnv)
}