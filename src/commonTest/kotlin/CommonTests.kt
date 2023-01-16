import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe

class CommonTests : FunSpec({
    test("lifecycles registered") {
        lifecycles.size shouldBeGreaterThan 0
        printlnln("lifecycles: $lifecycles")
    }

    lifecycles.filter { it != Lifecycle.TEST_CASE }.forEach { lifecycle ->
        test("lifecycles $lifecycle entered") {
            withClue("$lifecycle – ${Phase.ENTRY}") {
                lifecycle.count(Phase.ENTRY).value shouldBe 1
            }
            withClue("$lifecycle – ${Phase.EXIT}") {
                lifecycle.count(Phase.EXIT).value shouldBe 0
            }
        }
    }

    test("lifecycles ${Lifecycle.TEST_CASE} entered") {
        val lifecycle = Lifecycle.TEST_CASE
        withClue("$lifecycle – ${Phase.ENTRY}") {
            lifecycle.count(Phase.ENTRY).value shouldBeGreaterThan 0
        }
        withClue("$lifecycle – ${Phase.EXIT}") {
            lifecycle.count(Phase.EXIT).value shouldBe (lifecycle.count(Phase.ENTRY).value - 1)
        }
    }
})
