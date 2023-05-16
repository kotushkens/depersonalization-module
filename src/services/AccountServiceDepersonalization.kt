package services

import repositories.PrivateAccountRepository
import repositories.PrivateCustomerRepository

class AccountServiceDepersonalization(
    private val privateAccountRepository: PrivateAccountRepository,
    private val privateCustomerRepository: PrivateCustomerRepository,
    private val depersonalizationService: DepersonalizationServiceImpl,
    ) {

    fun depersonalizePrivateAccounts() {
      depersonalizationService.depersonalizePrivateAccounts(privateAccountRepository.getAll())
    }

    fun depersonalizePrivateCustomers() {
      depersonalizationService.depersonalizePrivateCustomers(privateCustomerRepository.getAll())
    }

}