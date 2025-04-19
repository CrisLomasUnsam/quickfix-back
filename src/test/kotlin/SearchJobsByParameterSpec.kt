import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import quickfix.utils.enums.ProfessionType
import quickfix.utils.hasMatchingStart
import quickfix.utils.searchParameters.ISearchParameters

enum class MockedJobStatus { DONE, PENDING }

data class MockedCustomer(val id: Long)

data class Professional(val professions: List<ProfessionType>)

data class MockedJob(
    val done: Boolean,
    val inProgress: Boolean,
    val professional: Professional,
    val mockedCustomer: MockedCustomer
)

val mockedJob1 = MockedJob(
    done = true,
    inProgress = false,
    professional = Professional(listOf((ProfessionType.JARDINERO))),
    mockedCustomer = MockedCustomer(id = 1)
)

val mockedJob2 = MockedJob(
    done = false,
    inProgress = true,
    professional = Professional(listOf((ProfessionType.JARDINERO))),
    mockedCustomer = MockedCustomer(id = 1)
)

val mockedJob3 = MockedJob(
    done = false,
    inProgress = false,
    professional = Professional(listOf((ProfessionType.ELECTRICISTA))),
    mockedCustomer = MockedCustomer(id = 2)
)

val listOfJobs = listOf(mockedJob1, mockedJob2, mockedJob3)

val professionMapping = mapOf(
    "gasista" to ProfessionType.GASISTA,
    "jardinero" to ProfessionType.JARDINERO,
)

class MockedJobSearchParameters(private val parameter: String) : ISearchParameters<MockedJob> {
    override fun matches(element: MockedJob): Boolean =
        matchMockedJobStatusFromString(parameter)?.let { status ->
            when (status) {
                MockedJobStatus.DONE -> element.done
                MockedJobStatus.PENDING -> element.inProgress
            }
        } ?: matchProfessionFromString(parameter, element)
}

fun searchByParameters(id: Long, parameters: ISearchParameters<MockedJob>): List<MockedJob> {
    val jobsFilteredByCustomer = listOfJobs.filter { it.mockedCustomer.id == id }
    return jobsFilteredByCustomer.filter { parameters.matches(it) }
}

fun matchMockedJobStatusFromString(param: String): MockedJobStatus? {
    val cleaned = param.trim().lowercase()
    return when {
        cleaned.contains("finalizado") || cleaned.contains("finalizados") -> MockedJobStatus.DONE
        cleaned.contains("pendiente") || cleaned.contains("pendientes") -> MockedJobStatus.PENDING
        else -> null
    }
}
fun matchProfessionFromString(param: String, element: MockedJob): Boolean {
    val cleanedParam = param.trim().lowercase()
    val matchedProfession = professionMapping.entries.find { (key, _) ->
        cleanedParam.contains(key) ||
                key.contains(cleanedParam) ||
                hasMatchingStart(cleanedParam, key) ||
                hasMatchingStart(key, cleanedParam)
    }?.value
    return matchedProfession != null &&
            element.professional.professions.any {
                it == matchedProfession
            }
}

class SearchJobsByParameterSpec : DescribeSpec({

    isolationMode = IsolationMode.InstancePerTest

    describe("given a string as a search parameter") {
        describe("for customer with id=1") {

            it("should return done jobs for 'finalizado'") {
                val jobList = searchByParameters(1, MockedJobSearchParameters("finalizado"))
                jobList.size shouldBe 1
            }

            it("should return pending jobs for 'pendientes'") {
                val jobList = searchByParameters(1, MockedJobSearchParameters("PenDieNtEs"))
                jobList.size shouldBe 1
            }

            it("should return no jobs for unrecognized string 'favoritos'") {
                val jobList = searchByParameters(1, MockedJobSearchParameters("favoritos"))
                jobList.size shouldBe 0
            }

            it("should return jobs for 'jardinero' even when parameter is incomplete") {
                val jobList = searchByParameters(1, MockedJobSearchParameters("jardineri"))
                jobList.size shouldBe 2
            }
        }
    }
})
