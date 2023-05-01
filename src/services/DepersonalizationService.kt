package services

import db.entities.PrivateAccountEntity
import kotlin.reflect.full.memberProperties

interface DepersonalizationService {
    fun depersonalizeAccs(data: Collection<PrivateAccountEntity>): PrivateAccountEntity
}

class DepersonalizationServiceImpl() : DepersonalizationService {
    //    override fun <T> depersonalizeData(collection: Collection<T>) =
//        for (property in T::class.memberProperties)
//        {
//
//    }

    override fun depersonalizeAccs(data: Collection<PrivateAccountEntity>): PrivateAccountEntity {
        return data.forEach(it.membersOf())
    }

    inline fun <reified T> T.membersOf() = T::class.members

    inline fun <reified T> depersonalizeDataV2(data: T): T {
        return (data.membersOf())
    }
}