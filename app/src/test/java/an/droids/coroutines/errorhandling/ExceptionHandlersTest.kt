package an.droids.coroutines.errorhandling

import an.droids.coroutines.log
import kotlinx.coroutines.experimental.joinAll
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test

class ExceptionHandlersTest {
    @Test
    fun `test exception handling on coroutines`() = runBlocking {
        val runWithoutException = launch(fallibleThreadPool) {
            log("Without Exception")
        }
        val runWithException = launch(fallibleThreadPool) {
            throw IllegalAccessException("Throws an error")
        }
        joinAll(runWithException, runWithoutException)
    }
}