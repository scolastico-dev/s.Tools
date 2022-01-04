package me.scolastico.tools.etc

class StringSplitter private constructor() {
    companion object {

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
