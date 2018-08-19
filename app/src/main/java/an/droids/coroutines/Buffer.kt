package an.droids.coroutines

import android.graphics.Bitmap
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import kotlinx.coroutines.experimental.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.experimental.coroutineContext

class CoroutinedUserImage {

    val diskIO = newFixedThreadPoolContext(2, "diskIO")
    val networkIO = newFixedThreadPoolContext(2, "networkIO")
    val mockedUi = newFixedThreadPoolContext(1, "Mock-UI-Thread")

    fun displayUserImage(userName: String) = launch(mockedUi) {
        log("initiating displaying image")
        val userImageUrl = getUserImageUrl(userName)
        val image = getImage(userImageUrl)
        saveImageToDisk(image)
//        saveImageToDiskAsync(image, coroutineContext[Job]!!)
//        setUserProfileImage(image)
//        log("done displaying")

        getImageFromDisk()
    }

    private suspend fun saveImageToDisk(image: String) {
        delay(1000)
    }

    fun displayUserImageButError(userName: String) = launch(mockedUi) {
        try {
            val userImageUrl = getUserImageUrl(userName)
            val image = getImage(userImageUrl)
            (saveImageToDiskAsync(image, coroutineContext[Job]!!).await())
            trySettingUserProfileImageButError(image)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun saveImageToDiskAsync(image: String, parent: Job) = async(coroutineContext,
            parent = parent, onCompletion = { e -> if (e != null) log("I got cancelled") }) {
        while (isActive) {
            delay(100)
            log("Saving image to disk")
        }
    }

    suspend fun trySettingUserProfileImageButError(image: String): Nothing = withContext(mockedUi) {
        delay(1000)
        throw Exception("Could not save")
    }

    private suspend fun getImage(userImageUrl: String) = withContext(networkIO) {
        log("Fetching image")
        delay(1000)
        return@withContext "ImageSubstitiute"
    }

    suspend fun setUserProfileImage(userImageUrl: String) = withContext(mockedUi) {
        log("Setting user image")
    }

    suspend fun getUserImageUrl(userName: String) = withContext(networkIO) {
        log("Getting user image")
        delay(1000)
        return@withContext "https://randomuser.me/api/portraits/lego/8.jpg"

    }

    suspend fun saveToken(accessToken: String) {
        delay(100)
    }

    suspend fun requestAccessToken(userName: String, password: String): String {
        delay(100)
        return "AccessToken"
    }
}

class CallbackedUserImage {

    fun displayUserImage(userName: String) {
        getUserImageUrl(userName) { imageUrl ->
            getImage(imageUrl) { image ->
                setImageToProfile(image)
            }
        }
    }

    private fun getImage(imageUrl: String, imageCb: (Bitmap) -> Unit) {

    }

    private fun getUserImageUrl(userName: String, imageUrlCb: (String) -> Unit) {

    }

    private fun requestAccessToken(userName: String, password: String,
            tokenCallback: (String) -> Unit) {

    }

    private fun requestUserImage(userName: String, token: String,
            tokenCallback: (String) -> Unit): Unit {

    }

    private fun setImageToProfile(imageUrl: Bitmap) {
        runBlocking(CommonPool) { }
    }
}

suspend fun <T> cancellableTask(): T = suspendCancellableCoroutine<T> { cont ->
    while (cont.isActive) {
        //perform task
    }
    cont.invokeOnCancellation { log("Cancelled!!") }
}

suspend fun <T> Call<T>.await() = suspendCancellableCoroutine<T> { cont ->
    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>?, t: Throwable?) {
            cont.resumeWithException(t!!)
        }

        override fun onResponse(call: Call<T>?, response: Response<T>?) {
            if (cont.isActive) cont.resume(response!!.body()!!)
        }
    })
    cont.invokeOnCancellation { this@await.cancel() }
}

