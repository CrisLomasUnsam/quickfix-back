package quickfix.utils

interface ISearchParameters<T> {
    fun matches(element: T): Boolean
}