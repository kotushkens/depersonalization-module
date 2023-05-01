import services.AccountServiceDepersonalization

class Application(
    private val accountServiceDepersonalization: AccountServiceDepersonalization,
) {

    fun start() {
        accountServiceDepersonalization.depersonalizePrivateAccounts()
    }
}