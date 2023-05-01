import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin


private object Launcher : KoinComponent {

    fun run() {


        startKoin {
            modules(appModule)
        }

        val application = Application(accountServiceDepersonalization = get())

        application.start()
    }
}

fun main() {
    Launcher.run()
}