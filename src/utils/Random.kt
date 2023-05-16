package utils

import kotlin.random.Random.Default.nextInt

class Random {
    val alphabet: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    fun randomString(length: Int): String = List(length) { alphabet.random() }.joinToString("")

    fun randomId(boundAbove: Int, boundBelow: Int) = List(nextInt(boundAbove, boundBelow)) { nextInt(0, 9) }.joinToString("")
}