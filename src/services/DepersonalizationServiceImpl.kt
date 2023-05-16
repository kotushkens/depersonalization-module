package services

import config.Config
import db.entities.PrivateAccountEntity
import db.entities.PrivateCustomerEntity
import kotlinx.coroutines.runBlocking
import repositories.HibernateRepository
import java.time.LocalDate
import kotlin.reflect.full.declaredMemberProperties

class DepersonalizationServiceImpl(
    private val config: Config,
    private val destinationRepository: HibernateRepository,
    private val depersonalizationRulesService: DepersonalizationRulesService,
) {

    var sourceValues = HashMap<String, ArrayList<String>>()
    var depersonalizedValues = HashMap<String, String>()

    val excludedColumns = setOf("gender", "middleName")
    val genderConnectedColumns = setOf("firstName", "middleName", "lastName")

    fun depersonalizePrivateAccounts(data: Collection<PrivateAccountEntity>) = runBlocking {
        data.forEach { entity ->
            PrivateAccountEntity::class.declaredMemberProperties.forEach {
                if (!depersonalizedValues.containsKey(it.getter.call(entity)?.toString())) {
                    it.getter.call(entity)?.toString()
                        ?.let { value ->
                            sourceValues.getOrPut(it.name, ::arrayListOf).add(value)

                            if (config.specificFormats.contains(it.name)) {
                                if (!genderConnectedColumns.contains(it.name)) {
                                    depersonalizedValues[value] =
                                        depersonalizationRulesService.depersonalize(it.name, value)
                                } else depersonalizedValues[value] = depersonalizationRulesService.depersonalize(
                                    it.name,
                                    sourceValues.getValue("gender").last()
                                )
                            }
                        }
                }
            }
            val depersonalizedEntity = PrivateAccountEntity(
                id = entity.id,
                dboId = if (isDepersonalized(entity.dboId.toString())) depersonalizedValues.getValue(entity.dboId.toString())
                    .toLong()
                else entity.dboId,
                accountNumber = entity.accountNumber,
                bic = entity.bic,
                status = entity.status,
                balance = entity.balance,
                updatedAt = if (isDepersonalized(entity.updatedAt.toString())) LocalDate.parse(
                    depersonalizedValues.getValue(
                        entity.updatedAt.toString()
                    )
                )
                else entity.updatedAt,
            )
            destinationRepository.withTransaction {
                destinationRepository.persist(depersonalizedEntity)
            }
        }
    }

    fun depersonalizePrivateCustomers(data: Collection<PrivateCustomerEntity>) = runBlocking {
        data.forEach { entity ->
            PrivateCustomerEntity::class.declaredMemberProperties.forEach {
                if (!depersonalizedValues.containsKey(it.getter.call(entity)?.toString())) {
                    it.getter.call(entity)?.toString()?.let { value ->
                        sourceValues.getOrPut(it.name, ::arrayListOf).add(value)

                        if (it.returnType == LocalDate::class.java) {
                            depersonalizedValues[value] = depersonalizationRulesService.depersonalize(
                                "date", value
                            )
                        }

                        if (config.specificFormats.contains(it.name)) {
                            if (!genderConnectedColumns.contains(it.name)) {
                                depersonalizedValues[value] =
                                    depersonalizationRulesService.depersonalize(it.name, value)
                            } else depersonalizedValues[value] = depersonalizationRulesService.depersonalize(
                                it.name,
                                entity.gender
                            )
                        }
                    }
                }
            }
            val depersonalizedEntity = PrivateCustomerEntity(
                dboId = if (isDepersonalized(entity.dboId.toString())) depersonalizedValues.getValue(entity.dboId.toString())
                    .toLong()
                else entity.dboId,
                dateOfBirth = if (isDepersonalized(entity.dateOfBirth.toString())) LocalDate.parse(
                    depersonalizedValues.getValue(
                        entity.dboId.toString()
                    )
                )
                else entity.dateOfBirth,
                gender = entity.gender,
                firstName = if (isDepersonalized(entity.firstName)) depersonalizedValues.getValue(entity.firstName)
                else entity.firstName,
                middleName = entity.middleName?.let {
                    if (isDepersonalized(entity.middleName)) depersonalizedValues.getValue(entity.middleName) else
                        entity.middleName
                },
                lastName = if (isDepersonalized(entity.lastName)) depersonalizedValues.getValue(entity.lastName)
                else entity.lastName,
                mainPhoneNumber = if (isDepersonalized(entity.mainPhoneNumber)) depersonalizedValues.getValue(entity.mainPhoneNumber)
                else entity.mainPhoneNumber,
                optionalPhoneNumber = if (isDepersonalized(entity.optionalPhoneNumber)) depersonalizedValues.getValue(
                    entity.optionalPhoneNumber.toString()
                )
                else entity.optionalPhoneNumber,
                inn = if (isDepersonalized(entity.inn)) depersonalizedValues.getValue(entity.inn.toString())
                else entity.inn,
                passport = if (isDepersonalized(entity.passport)) depersonalizedValues.getValue(entity.passport.toString())
                else entity.passport,
            )
            destinationRepository.withTransaction {
                destinationRepository.persist(depersonalizedEntity)
            }
        }
    }

    private fun isDepersonalized(value: String?): Boolean {
        return depersonalizedValues.containsKey(value)
    }
}