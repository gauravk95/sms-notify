package com.github.sms.utils

import com.github.sms.data.models.local.SmsItem
import java.util.*

object DataGenerator {

    fun getDummySmsList(number: Int): List<SmsItem> {
        val list: MutableList<SmsItem> = mutableListOf()

        for (i in 1..number) {
            val random = Random()
            list.add(
                    SmsItem(random.nextInt(),
                            "TM-ER" + (("0000$i").substring(i.toString().length)),
                            generateRandomString(30),
                            random.nextLong(),
                            random.nextLong(),
                            random.nextInt(2)))
        }

        return list
    }

    private fun generateRandomString(length: Int): String {
        val generator = Random()
        val randomStringBuilder = StringBuilder()
        val randomLength = generator.nextInt(length)
        var tempChar: Char
        for (i in 0 until randomLength) {
            tempChar = (generator.nextInt(96) + 32).toChar()
            randomStringBuilder.append(tempChar)
        }
        return randomStringBuilder.toString()
    }
}