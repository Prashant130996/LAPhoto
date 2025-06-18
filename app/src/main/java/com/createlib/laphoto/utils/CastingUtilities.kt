package com.createlib.laphoto.utils

import com.createlib.laphoto.utils.Constants.EMPTY_STRING
import com.createlib.laphoto.utils.GsonUtilities.parseToJson
import java.math.BigDecimal

/**
 * Utilities class for casting types
 */
internal object CastingUtilities {

    /**
     * Function to parse [Any] into [String]
     * @return [String] parsed from [Any]
     * @receiver [Any]
     */
    fun Any.parseToString(): String = when (this) {
        String::class -> toString()
        Int::class -> toString()
        Boolean::class -> toString()
        Long::class -> toString()
        Float::class -> toString()
        Double::class -> toString()
        BigDecimal::class -> (this as BigDecimal).toPlainString()
        else -> try {
            parseToJson(this)
        } catch (t: Throwable) {
            EMPTY_STRING
        }
    }
}