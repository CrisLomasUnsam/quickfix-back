package quickfix.models

interface Id {
  var id: Long

  fun validate()

  fun isNew () = id.toInt() == -1
}