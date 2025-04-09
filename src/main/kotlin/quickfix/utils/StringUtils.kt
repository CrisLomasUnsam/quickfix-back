package quickfix.utils

fun hasMatchingStart(a: String, b: String, limit: Int = 5): Boolean {
    val prefixLength = a.zip(b).takeWhile { it.first == it.second }.count()
    return prefixLength >= limit
}