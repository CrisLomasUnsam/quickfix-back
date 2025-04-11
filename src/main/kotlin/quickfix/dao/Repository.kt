package quickfix.dao

import quickfix.utils.exceptions.BusinessException
import quickfix.models.Identifier
import quickfix.utils.ISearchParameters


abstract class Repository<T : Identifier> {
  protected val elements: MutableSet<T> = mutableSetOf()
  private var currentId: Long = 0

  fun create(element: T) {
    throwErrorIfIdIsAssigned(element)
    element.validate()
    currentId++
    element.id = currentId

    addElement(element)
  }

  fun delete(element: T) {
    elements.remove(getById(element.id))
  }

  fun update(element: T){
    element.validate()
    delete(element)
    addElement(element)
  }

  private fun addElement(element: T) {
    elements.add(element)
  }

  fun getById(id : Long) : T? {
    throwErrorIfIdDoesNotExist(id)
    return elements.find { it.id == id }
  }

  private fun throwErrorIfIdDoesNotExist(id : Long){
    if(elements.all{it.id != id})
      throw BusinessException("No existe un elemento asociado al id: $id")
  }

  private fun throwErrorIfIdIsAssigned(element: T){
    if(!element.isNew())
      throw BusinessException("Ya existe un elemento con el id: ${element.id}.")
  }

  open fun searchByParameters(id: Long, parameters: ISearchParameters<T>): List<T> = elements.filter { parameters.matches(it) }

  fun clearAll() {
    elements.clear()
    currentId = 0
  }

}
