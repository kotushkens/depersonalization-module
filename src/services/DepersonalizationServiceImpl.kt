package services

import db.entities.PrivateAccountEntity
import db.entities.PrivateCustomerEntity
import kotlinx.coroutines.runBlocking
import repositories.HibernateRepository
import java.time.LocalDate
import kotlin.reflect.full.declaredMemberProperties

class DepersonalizationServiceImpl(
    private val destinationRepository: HibernateRepository,
    ) {

    var sourceValues = HashMap<String, ArrayList<String>>()
    var depersonalizedValues = HashMap<String, ArrayList<String>>()

    //val date  = "2016-08-16"



     fun depersonalizePrivateAccounts(data: Collection<PrivateAccountEntity>) = runBlocking {
            data.forEach { entity ->
               PrivateAccountEntity::class.declaredMemberProperties.forEach {
                   if (it.returnType != LocalDate::class.java) {
                       it.getter.call(entity)?.toString()
                           ?.let { value -> sourceValues.getOrPut(it.name, ::arrayListOf).add(value)
                               depersonalizedValues.getOrPut(value, ::arrayListOf).add(value.reversed())
                           }
                   }
               }
                val depersonalizedEntity = PrivateAccountEntity(
                  id = depersonalizedValues.getValue(entity.id.toString()).first().toLong(),
                  dboId = depersonalizedValues.getValue(entity.dboId.toString()).first().toLong(),
                  accountNumber = depersonalizedValues.getValue(entity.accountNumber.toString()).first().toLong(),
                    bic = depersonalizedValues.getValue(entity.bic.toString()).first().toLong(),
                    status = depersonalizedValues.getValue(entity.status).first(),
                    balance = depersonalizedValues.getValue(entity.balance.toString()).first().toDouble(),
                    updatedAt = entity.updatedAt,
                )
                destinationRepository.withTransaction {
                    destinationRepository.persist(depersonalizedEntity)
                }
            }
        }

    fun depersonalizePrivateCustomers(data: Collection<PrivateCustomerEntity>) = runBlocking {
        data.forEach { entity ->
            PrivateCustomerEntity::class.declaredMemberProperties.forEach {
                if (it.returnType != LocalDate::class.java) {
                    it.getter.call(entity)?.toString()
                        ?.let { value -> sourceValues.getOrPut(it.name, ::arrayListOf).add(value)
                            depersonalizedValues.getOrPut(value, ::arrayListOf).add(value.reversed())
                        }
                }
            }
            val depersonalizedEntity = PrivateCustomerEntity(
                dboId = depersonalizedValues.getValue(entity.dboId.toString()).first().toLong(),
                firstName = depersonalizedValues.getValue(entity.firstName).first(),
                middleName = entity.middleName?.let { depersonalizedValues.getValue(entity.middleName).first() },
                lastName = depersonalizedValues.getValue(entity.lastName).first(),
                mainPhoneNumber = depersonalizedValues.getValue(entity.mainPhoneNumber).first(),
                optionalPhoneNumber = entity.optionalPhoneNumber?.let { depersonalizedValues.getValue(entity.optionalPhoneNumber).first() },
                inn = entity.inn?.let { depersonalizedValues.getValue(entity.inn).first() },
                passport = entity.passport?.let { depersonalizedValues.getValue(entity.passport).first() },
            )
            destinationRepository.withTransaction {
                destinationRepository.persist(depersonalizedEntity)
            }
        }
    }

}