package com.allen.allenlib.util

import java.text.DecimalFormat

object CommonUtils {

    fun stringSpaceFilter(str: String): String {
        val newStr = str.replace(Regex("\\n{3,}"), "\n\n")
        return newStr
    }

    fun splitColon(str: String): Int {
        val firstColon: Int = str.indexOf(":")
        var result: Int = 0
        if (firstColon != -1) { //-1 if none found
            result = Integer.parseInt(str.subSequence(0, firstColon).toString())
        }
        return result

    }

    /**
     * 顯示千分位數字 字串
     * @param amount 金額數字
     * @return ex: 帶入12345 回傳12,345
     */
    fun formatAmount(amount: Int): String {
        val decimalFormat = DecimalFormat("#,###")
        var result = String()
        try {
            result = decimalFormat.format(amount)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            loge("decimalFormat Error: ${e.message}")
        }
        return result
    }
}