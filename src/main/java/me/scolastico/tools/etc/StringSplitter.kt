package me.scolastico.tools.etc

/**
 * Split strings by length into an array.
 */
class StringSplitter private constructor() {
    companion object {

        /**
         * Split strings by length into an array.
         * @param string The string to split.
         * @param length The max length after which to split the string.
         * @return An array of the strings.
         */
        fun splitStringByLength(string: String, length: Int):Array<String> {
            var remaining = string
            val list = ArrayList<String>()
            while (remaining.length > length) {
                list.add(remaining.substring(0, length))
                remaining = remaining.substring(length)
            }
            if (remaining.isNotEmpty()) list.add(remaining)
            return list.toTypedArray()
        }

    }
}
