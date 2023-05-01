package services

import db.entities.PrivateAccountEntity
import repositories.PrivateAccountRepositoryImpl

class AccountServiceDepersonalization(
    private val privateAccountRepository: PrivateAccountRepositoryImpl,
    private val depersonalizationService: DepersonalizationService,
    ) {


    fun depersonalizePrivateAccounts(): Collection<PrivateAccountEntity> {
        depersonalizationService.depersonalizeAccs(privateAccountRepository.getAll())
        return privateAccountRepository.getAll()
    }

}