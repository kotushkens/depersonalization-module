package services

import repositories.PrivateAccountRepositoryImpl

class AccountServiceDepersonalization(
    private val privateAccountRepository: PrivateAccountRepositoryImpl,
    private val depersonalizationService: DepersonalizationServiceImpl,
    ) {

    fun depersonalizePrivateAccounts(): MutableMap<String, MutableList<String>> {
        return depersonalizationService.depersonalizePrivateAccounts(privateAccountRepository.getAll())
    }

}