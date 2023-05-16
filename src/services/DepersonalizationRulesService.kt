package services

import utils.Random
import java.time.LocalDate
import kotlin.random.Random.Default.nextInt

class DepersonalizationRulesService(
    private val random: Random,
) {

    private val regionCodes = RussianRegionCodes()
    private val russianFemaleFirstNames = RussianFemaleFirstNames()
    private val russianMaleFirstNames = RussianMaleFirstNames()
    private val russianFemaleLastNames = RussianFemaleLastNames()
    private val russianMaleLastNames = RussianMaleLastNames()

    fun depersonalize(column: String, value: String): String = when (column) {
        "dboId" -> random.randomId(12, 14)
        "passport" -> depersonalizePassport()
        "inn" -> depersonalizeInn()
        "firstName" -> depersonalizeFirstName(value)
        "middleName" -> ""
        "lastName" -> depersonalizeLastName(value)
        "mainPhoneNumber" -> depersonalizePhoneNumber()
        "optionalPhoneNumber" -> depersonalizePhoneNumber()
        "date" -> depersonalizeDate(value)
        else -> ""
    }

    private fun depersonalizePassport(): String {
        return regionCodes.codes.random() + nextInt(10, 99) + " " + nextInt(100000, 999999)
    }

    private fun depersonalizeInn(): String {
        return regionCodes.codes.random() + nextInt(100000000, 999999999) + nextInt(100, 999)
    }

    private fun depersonalizePhoneNumber(): String {
        return "+7" + nextInt(100000000, 999999999) + nextInt(0, 9)
    }

    private fun depersonalizeDate(date: String): String {
        return LocalDate.parse(date).minusDays(2).toString()
    }

    private fun depersonalizeFirstName(gender: String): String = when (gender) {
        "Женский" -> russianFemaleFirstNames.names.random()
        "Мужской" -> russianMaleFirstNames.names.random()
        else -> ""
    }

    private fun depersonalizeLastName(gender: String): String = when (gender) {
        "Женский" -> russianFemaleLastNames.names.random()
        "Мужской" -> russianMaleLastNames.names.random()
        else -> ""
    }
}

data class RussianRegionCodes(
    val codes: Set<String> = setOf("01", "48", "02", "10", "49", "03", "50", "90", "15")
)

data class RussianFemaleFirstNames(
    val names: Set<String> = setOf("Елена", "Анна", "Мария")
)

data class RussianFemaleLastNames(
    val names: Set<String> = setOf("Синицына", "Орлова", "Петрова")
)

data class RussianMaleFirstNames(
    val names: Set<String> = setOf("Даниил", "Антон", "Олег")
)

data class RussianMaleLastNames(
    val names: Set<String> = setOf("Егоров", "Краснов", "Рублев")
)

