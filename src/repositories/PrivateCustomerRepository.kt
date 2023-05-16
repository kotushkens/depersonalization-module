package repositories

import db.entities.PrivateCustomerEntity
import kotlinx.coroutines.runBlocking

class PrivateCustomerRepository(
    private val repository: HibernateRepository,
) {

    fun getAll(): Collection<PrivateCustomerEntity> = runBlocking {
        repository.withTransaction {
            repository.selectAll(PrivateCustomerEntity::class.java)
        }
    }
}