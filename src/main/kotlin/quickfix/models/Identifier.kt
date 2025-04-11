package quickfix.models

interface Identifier {

  var id: Long
  fun validate()
  fun isNew () = id == (-1).toLong()

}