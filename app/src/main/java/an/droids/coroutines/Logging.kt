package an.droids.coroutines

fun log(log: String) {
    System.setProperty("kotlinx.coroutines.debug", "on")
    println("Thread:${Thread.currentThread().name}: $log")
}

