package quickfix.dao

import quickfix.models.GlobalIdGenerator
import quickfix.utils.exceptions.BusinessException
import quickfix.models.Id
import quickfix.utils.SearchParameters


abstract class Repository<T : Id> {
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
    return elements.find { it.id == id }
  }
  private fun throwErrorIfIdDoesNotExist(id : Long){
    if(elements.all{it.id != id})
      throw BusinessException("There is no element associated with the id:: $id")
  }
  private fun throwErrorIfIdIsAssigned(element: T){
    if(!element.isNew())
      throw BusinessException("Already exists an object with id ${element.id}.")
  }

  open fun searchByParameters(id: Long, parameters: SearchParameters<T>): List<T> = elements.filter { parameters.matches(it) }

  fun clearAll() {
    elements.clear()
    currentId = 0
  }

}
