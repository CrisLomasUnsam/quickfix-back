package quickfix.dao

import quickfix.models.GlobalIdGenerator
import quickfix.utils.exceptions.BusinessException
import quickfix.models.Id


abstract class Repository<T : Id> {
  protected val elements: MutableSet<T> = mutableSetOf()

  fun create(element: T) {
    throwErrorIfIdIsAssigned(element)
    element.validate()
    element.id = GlobalIdGenerator.nextId()

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

}
