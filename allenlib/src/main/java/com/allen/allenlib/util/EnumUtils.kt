package com.allen.allenlib.util

object EnumUtils {
    enum class Gender(val type: Int) {
        NONE(0), MALE(1), FEMALE(2)
    }

    // post_way:1:self or 3:others
    enum class PostType(val type: String) {
        MYSELF("1"), OTHERS("3")
    }

    // Sign_up_filter
    enum class FilterType(val type: String) {
        NEW("new"), OLD("old")
    }
}