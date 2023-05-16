
import config.Config
import config.ConfigImpl
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import repositories.HibernateRepository
import repositories.JPA.createSessionFactory
import repositories.PrivateAccountRepository
import repositories.PrivateCustomerRepository
import services.AccountServiceDepersonalization
import services.DepersonalizationRulesService
import services.DepersonalizationServiceImpl
import utils.Random

private val DEPERSONALISED = StringQualifier("depersonalised")

val appModule = module {
    single<Config> {
        ConfigImpl(getEnv = System::getenv)
    }


    single { HibernateRepository(createSessionFactory(ConnectionPool(get<Config>().accountServiceDbConfig.connectionConfig).dataSource , "db.entities")) }

    single(DEPERSONALISED) { HibernateRepository(createSessionFactory(ConnectionPool(get<Config>().destinationDbConfig.connectionConfig).dataSource, "db.entities")) }

    single { DepersonalizationRulesService(get()) }
    single { DepersonalizationServiceImpl(get(), get(DEPERSONALISED), get()) }


    single { PrivateAccountRepository(get()) }
    single { PrivateCustomerRepository(get()) }
    single { AccountServiceDepersonalization(get(), get(), get()) }

    single { Random() }
}