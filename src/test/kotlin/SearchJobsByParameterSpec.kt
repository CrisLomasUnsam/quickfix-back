import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import quickfix.models.Profession
import quickfix.utils.SearchParameters
import quickfix.utils.hasMatchingStart

class SearchJobsByParameterSpec: DescribeSpec ({
    isolationMode = IsolationMode.InstancePerTest

    /* Mockeo dominios, metodos y algunas interfases por si cambian las clases en el futuro, sólo quiero testear la lógica de filtrado */

    data class MockedCustomer(val id: Long)

    data class Professional(val professions: List<Profession>)

    data class MockedJob(
        val done: Boolean,
        val inProgress: Boolean,
        val professional: Professional,
        val mockedCustomer: MockedCustomer
    )

    val mockedJob1 = MockedJob(
        done = true,
        inProgress = false,
        professional = Professional(listOf(Profession.JARDINERO)),
        mockedCustomer = MockedCustomer(id = 1)
    )

    val mockedJob2 = MockedJob(
        done = false,
        inProgress = true,
        professional = Professional(listOf(Profession.JARDINERO)),
        mockedCustomer = MockedCustomer(id = 1)
    )

    val mockedJob3 = MockedJob(
        done = false,
        inProgress = false,
        professional = Professional(listOf(Profession.ELECTRICISTA)),
        mockedCustomer = MockedCustomer(id = 2)
    )

    val listOfJobs = listOf(mockedJob1, mockedJob2, mockedJob3)

    fun searchByParameters(id: Long, parameters: SearchParameters<MockedJob>): List<MockedJob> {
        val jobsFilteredByCustomer = listOfJobs.filter { it.mockedCustomer.id == id }
        return jobsFilteredByCustomer.filter { parameters.matches(it) }
    }

    class MockedJobSearchParameters(val parameter: String) : SearchParameters<MockedJob> {
        override fun matches(element: MockedJob): Boolean {
            return when (parameter.trim().lowercase()) {
                "finalizado" -> element.done
                "finalizados" -> element.done
                "pendiente" -> element.inProgress
                "pendientes" -> element.inProgress
                else -> {
                    val parameter = parameter.trim().lowercase()
                    return element.professional.professions.any {
                        val name = it.name.lowercase()
                        name.contains(parameter) ||
                        name.startsWith(parameter) ||
                        parameter.startsWith(name) ||
                        hasMatchingStart(name, parameter)

                    }
                }
            }
        }
    }


    describe("given a string as a search parameter") {
        describe("for customer with id=1"){

            it("should return done jobs for 'finalizado'"){
                val jobList = searchByParameters(1, MockedJobSearchParameters("finalizado"))
                jobList.size shouldBe 1
            }

            it("should return pending jobs for 'pendientes'"){
                val jobList = searchByParameters(1, MockedJobSearchParameters("PenDieNtEs"))
                jobList.size shouldBe 1
            }

            it("should return no jobs for unrecognized string 'favoritos'"){
                val jobList = searchByParameters(1, MockedJobSearchParameters("favoritos"))
                jobList.size shouldBe 0
            }

            it("should return jobs for 'jardinero' even when parameter is incomplete"){
                val jobList = searchByParameters(1, MockedJobSearchParameters("jardineri"))
                jobList.size shouldBe 2
            }
        }
    }
})