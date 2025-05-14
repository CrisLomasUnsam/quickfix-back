package quickfix.models

interface Identifier {
  var id: Long
  fun validate()
}