package services

import db.entities.PrivateAccountEntity
import kotlin.reflect.full.declaredMemberProperties

//interface DepersonalizationService {
//    fun depersonalizeAccs(data: Collection<PrivateAccountEntity>): Map<String, String>
//}

class DepersonalizationServiceImpl() {
    //    override fun <T> depersonalizeData(collection: Collection<T>) =
//        for (property in T::class.memberProperties)
//        {
//
//    }


    var sourceValues = HashMap<String, MutableList<String>>()
    var depersonalizedValues = HashMap<String, MutableList<String>>()



     fun depersonalizePrivateAccounts(data: Collection<PrivateAccountEntity>): MutableMap<String, MutableList<String>> {
            data.forEach { entity ->
               PrivateAccountEntity::class.declaredMemberProperties.forEach {
                   sourceValues.getOrPut(it.name, ::mutableListOf).add(it.getter.call(entity).toString())
                   depersonalizedValues.getOrPut(it.name, ::mutableListOf).add(it.getter.call(entity).toString() + "00")
               }
            }
        return depersonalizedValues
    }

  //  fun depersonalizePrivateAccountsV2()

//    fun <T> depersonalizeDataV2(field: T): String {
//        return field.
//    }
}