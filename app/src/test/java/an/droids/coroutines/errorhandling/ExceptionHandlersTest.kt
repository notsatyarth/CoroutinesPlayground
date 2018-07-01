package an.droids.coroutines.errorhandling

import an.droids.coroutines.log
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test

class ExceptionHandlersTest {
    @Test
    fun `default exception handler handles uncaught exceptions`() = runBlocking {
        val runWithoutException = List(100) { idx ->
            launch(fallibleThreadPool) {
                when {
                    idx % 2 == 0 -> 1.div(0) //divide by zero
                    idx % 3 == 0 -> listOf(1)[2] // Index out of bounds
                    idx % 4 == 0 -> emptyMap<String, String>()["InvalidKey"]!!.contains("a") //NPE
                    idx % 5 == 0 -> throw Throwable()
                    else -> log("Success")
                }
            }
        }
        runWithoutException.forEach { it.join() }
    }

    @Test
    fun `DSL for tracking`() = runBlocking {
        val jobs =
                List(100) { idx ->
                    track {
                        when {
                            idx % 2 == 0 -> 1.div(0) //divide by zero
                            idx % 3 == 0 -> listOf(1)[2] // Index out of bounds
                            idx % 4 == 0 -> emptyMap<String, String>()["InvalidKey"]!!.contains(
                                    "a") //NPE
                            idx % 5 == 0 -> throw Throwable()
                            else -> log("Success")
                        }
                    }
                }
        jobs.forEach { it.join() }
    }



}