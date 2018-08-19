package an.droids.coroutines.animations

import an.droids.coroutines.CoroutinedUserImage
import an.droids.coroutines.R
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class MainActivity : AppCompatActivity() {

    lateinit var avatar: View
    lateinit var kotlin: View
    lateinit var coroutine: View
    lateinit var follow: View
    lateinit var loader: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        kotlin = findViewById<View>(R.id.kotlin)
        coroutine = findViewById<View>(R.id.coroutine)
        avatar = findViewById<View>(R.id.avatar)
        follow = findViewById<View>(R.id.follow)
        loader = findViewById(R.id.loader)
        loader.setOnClickListener { loadApi() }
        follow.setOnClickListener { performAnimation() }

    }

    private fun loadApi() {
        CoroutinedUserImage().displayUserImage("name")
        launch(UI, CoroutineStart.UNDISPATCHED) {
            showProgress()
            withContext(coroutineContext) { doApiCall() }
            hideProgress()
        }

    }

    private suspend fun doApiCall() {
        delay(5000)
    }

    private fun hideProgress() {
        loader.text = "Done"
    }

    private fun showProgress() {
        loader.text = "..."
    }

    fun performAnimation() = launch(UI) {
        animation(avatar) {
            alpha = 0.5f
        }

        animation(avatar, startDelay = 1000L) { top = 0f }.join()

        //wait until animation finished
        floatAnimation(avatar, 0.5f, 1f) { view, value ->
            view.alpha = value
        }.join()

        //wait until all of these animation finished
        mutableListOf(
                animation(follow) {
                    top = avatar.y + avatar.height + 16f
                },
                animation(kotlin) {
                    left = avatar.x - kotlin.width - 16f
                    centerY = avatar.centerY()
                },
                animation(coroutine) {
                    left = avatar.x + avatar.width + 16f
                    centerY = avatar.centerY()
                }
        ).forEach { it.join() } // wait for all animations to complete
    }

}
