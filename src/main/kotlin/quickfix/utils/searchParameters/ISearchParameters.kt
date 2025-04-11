package quickfix.utils.searchParameters

interface ISearchParameters<T> {
    fun matches(element: T): Boolean
}