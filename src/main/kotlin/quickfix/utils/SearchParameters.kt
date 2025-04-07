package quickfix.utils

interface SearchParameters<T> {
    fun matches(element: T): Boolean
}