import config.Config
import config.ConfigImpl
import org.koin.dsl.module
import repositories.HibernateRepository
import repositories.JPA.createSessionFactory
import repositories.PrivateAccountRepositoryImpl
import services.AccountServiceDepersonalization
import services.DepersonalizationService
import services.DepersonalizationServiceImpl

val appModule = module {
    single<Config> {
        ConfigImpl(getEnv = System::getenv)
    }

    single { ConnectionPool(get<Config>().accountServiceDbConfig.connectionConfig).dataSource }
    single { createSessionFactory(get(), "db.entities") } //todo: create datasource
    single { HibernateRepository(get()) }

    single { DepersonalizationServiceImpl() }
    single { AccountServiceDepersonalization(get(), get()) }


    single { PrivateAccountRepositoryImpl(get()) }

}