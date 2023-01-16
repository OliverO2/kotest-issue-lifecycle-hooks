import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.core.extensions.ProjectExtension
import io.kotest.core.extensions.SpecExtension
import io.kotest.core.extensions.TestCaseExtension
import io.kotest.core.project.ProjectContext
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult

fun printlnln(message: String) {
    println("$message\n") // WORKAROUND: KT-48292: KJS / IR: `println` doesn't move to a new line in tests
}

class Count(var value: Int = 0) {
    fun inc() {
        value++
    }

    override fun toString(): String = "$value"
}

enum class Lifecycle {
    PROJECT_CONFIG,
    PROJECT,
    SPEC,
    TEST_CASE;

    private val counts = mapOf(*Phase.values().map { it to Count() }.toTypedArray())

    fun count(phase: Phase) = counts.getValue(phase)

    fun enter() = apply {
        count(Phase.ENTRY).inc()
        printlnln("$this – enter: ${count(Phase.ENTRY)}")
    }

    fun exit() = apply {
        count(Phase.EXIT).inc()
        printlnln("$this – exit: ${count(Phase.EXIT)}")
    }
}

enum class Phase {
    ENTRY,
    EXIT
}

val lifecycles = setOf(*Lifecycle.values())

object TestConfiguration : AbstractProjectConfig() {
    override fun extensions(): List<Extension> = listOf(LifecycleExtension)

    override suspend fun beforeProject() {
        Lifecycle.PROJECT_CONFIG.enter()
    }

    override suspend fun afterProject() {
        Lifecycle.PROJECT_CONFIG.exit()
    }
}

private object LifecycleExtension : ProjectExtension, SpecExtension, TestCaseExtension {
    override suspend fun interceptProject(context: ProjectContext, callback: suspend (ProjectContext) -> Unit) {
        Lifecycle.PROJECT.enter()
        callback(context)
        Lifecycle.PROJECT.exit()
    }

    override suspend fun intercept(spec: Spec, execute: suspend (Spec) -> Unit) {
        Lifecycle.SPEC.enter()
        execute(spec)
        Lifecycle.SPEC.exit()
    }

    override suspend fun intercept(testCase: TestCase, execute: suspend (TestCase) -> TestResult): TestResult {
        Lifecycle.TEST_CASE.enter()
        try {
            return execute(testCase)
        } finally {
            Lifecycle.TEST_CASE.exit()
        }
    }
}
