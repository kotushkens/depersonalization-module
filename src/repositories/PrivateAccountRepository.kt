package repositories

import db.entities.PrivateAccountEntity
import kotlinx.coroutines.runBlocking

class PrivateAccountRepository(
    private val repository: HibernateRepository,
) {

    fun getAll(): Collection<PrivateAccountEntity> = runBlocking {
        repository.withTransaction {
            repository.selectAll(PrivateAccountEntity::class.java)
        }
    }
}