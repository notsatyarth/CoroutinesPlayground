package an.droids.coroutines.errorhandling

import an.droids.coroutines.log
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import java.io.ByteArrayOutputStream
import java.io.PrintStream

val fallibleThreadPool = newFixedThreadPoolContext(2,
        "FallibleThreadPool") + CoroutineExceptionHandler { coroutineContext, t ->
    val out = ByteArrayOutputStream()
    t.printStackTrace(PrintStream(out))
    log("Exception occurred $coroutineContext : ${String(out.toByteArray())}")
}

inline fun track(noinline block: () -> Unit): Job {
    return launch(fallibleThreadPool) {
        block.invoke()
    }
}






