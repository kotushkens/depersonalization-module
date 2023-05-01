package repositories

import db.entities.PrivateAccountEntity
import kotlinx.coroutines.runBlocking

interface DepersonalisationRepository {
}
class PrivateAccountRepositoryImpl(
    private val repository: HibernateRepository,
) : DepersonalisationRepository {

    fun getAll(): Collection<PrivateAccountEntity> = runBlocking {
        repository.withTransaction {
            repository.selectAll(PrivateAccountEntity::class.java)
        }
    }
}