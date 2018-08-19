package an.droids.coroutines

import kotlinx.coroutines.experimental.cancelAndJoin
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before
import org.junit.Test

class CoroutinedUserImageTest {

    @Before
    fun setUp() {
        System.setProperty("kotlinx.coroutines.debug", "on")
    }

    @Test
    fun testImageLoading() = runBlocking {
        CoroutinedUserImage().displayUserImage("Satyarth").join()
        delay(2000)
    }

    @Test
    fun testCancellation() = runBlocking {
        val display = CoroutinedUserImage().displayUserImage("Satyarth")
        delay(2500)
        println("cancelling")
        val cancelled=display.cancel()
        delay(4000)
//        display.cancelAndJoin()
    }



    @Test
    fun testError() = runBlocking {
        val display = CoroutinedUserImage().displayUserImageButError("Satyarth")
        display.join()
    }
}