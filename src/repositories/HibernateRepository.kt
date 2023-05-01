package repositories

import db.entities.PrivateAccountEntity
import exceptions.NotFoundException
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.hibernate.LockMode
import org.hibernate.LockOptions
import org.hibernate.Session
import org.hibernate.Session.LockRequest.PESSIMISTIC_NO_WAIT
import org.hibernate.SessionFactory
import kotlin.coroutines.CoroutineContext.Element
import kotlin.coroutines.CoroutineContext.Key
import kotlin.coroutines.coroutineContext

interface Repository {
    suspend fun <T> withTransaction(block: suspend () -> T): T
    suspend fun <T> query(queryStr: String, clazz: Class<T>, parameters: Map<String, Any>): Collection<T>
    suspend fun <T> query(queryStr: String, clazz: Class<T>, parameters: Map<String, Any>, limit: Int?): Collection<T>
    suspend fun update(queryStr: String, parameters: Map<String, Any>): Int
    suspend fun <T> selectAll(clazz: Class<T>): Collection<T>
    suspend fun <T> selectById(id: Any, clazz: Class<T>): T?
    suspend fun persist(entity: Any)
    suspend fun delete(entity: Any)
    suspend fun <T> lock(id: Any, clazz: Class<T>): T
}

class HibernateRepository(
    private val sessionFactory: SessionFactory
) : Repository {
    private val contextKey = object : Key<SessionContext> {}

    private suspend fun session() =
        coroutineContext[contextKey]?.session ?: throw IllegalStateException("Session not found")

    private interface SessionContext : Element {
        val session: Session
    }

    override suspend fun <T> withTransaction(block: suspend () -> T): T {
        return withContext(IO) {
            if (coroutineContext[contextKey] != null) {
                block()
            } else {
                sessionFactory.openSession().use {
                    val tx = it.beginTransaction()

                    try {
                        val context = object : SessionContext {
                            override val session: Session = it
                            override val key: Key<*> = contextKey
                        }
                        val result = withContext(context) {
                            block()
                        }
                        tx.commit()
                        return@withContext result
                    } catch (e: Exception) {
                        tx.rollback()
                        throw e
                    }
                }
            }
        }
    }

    override suspend fun <T> selectById(id: Any, clazz: Class<T>): T? {
        return session().find(clazz, id)
    }

    override suspend fun <T> selectAll(clazz: Class<T>): Collection<T> {
        return session().createQuery("FROM ${clazz.name}", clazz).resultList
    }

    override suspend fun <T> query(queryStr: String, clazz: Class<T>, parameters: Map<String, Any>): Collection<T> {
        return query(queryStr, clazz, parameters, null)
    }

    override suspend fun <T> query(queryStr: String, clazz: Class<T>, parameters: Map<String, Any>, limit: Int?): Collection<T> {
        val query = session().createQuery(queryStr, clazz)
        parameters.forEach {
            query.setParameter(it.key, it.value)
        }
        if (limit != null ) query.maxResults = limit
        return query.resultList
    }

    override suspend fun update(queryStr: String, parameters: Map<String, Any>): Int {
        val query = session().createQuery(queryStr)
        parameters.forEach {
            query.setParameter(it.key, it.value)
        }
        return query.executeUpdate()
    }

    override suspend fun persist(entity: Any) {
        session().persist(entity)
    }

    override suspend fun delete(entity: Any) {
        session().delete(entity)
    }

    override suspend fun <T> lock(id: Any, clazz: Class<T>): T {
        val entity = session().find(clazz, id) ?: throw NotFoundException()

        session().buildLockRequest(LockOptions(LockMode.PESSIMISTIC_WRITE).setTimeOut(PESSIMISTIC_NO_WAIT)).lock(entity)

        return entity
    }
}