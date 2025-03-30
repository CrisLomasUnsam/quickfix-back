package models

interface Id {
  var id: Int

  fun validate()

  fun isNew () = id == -1
}