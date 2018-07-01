package an.droids.coroutines.actors

import an.droids.coroutines.R
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.launch

class ActorsForEdits : AppCompatActivity() {

    private val clickListener: View.OnClickListener = View.OnClickListener { v ->
        val msg = when {
            v.id == R.id.plus_one -> Inc1()
            v.id == R.id.plus_two -> Inc2()
            v.id == R.id.plus_three -> Inc3()
            else -> Inc1()
        }
        launch(CommonPool) { counterActor.send(msg) }
    }

    private val counterActor = actor<Counter> {
        var counter = 0;
        for (msg in channel) {
            counter += when (msg) {
                is Inc1 -> msg.inc
                is Inc2 -> msg.inc
                is Inc3 -> msg.inc
            }
            updateView(counter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actors_for_edits)
        findViewById<TextView>(R.id.plus_one).setOnClickListener(clickListener)
        findViewById<TextView>(R.id.plus_two).setOnClickListener(clickListener)
        findViewById<TextView>(R.id.plus_three).setOnClickListener(clickListener)
    }

    private fun updateView(count: Int) {
        launch(UI) { findViewById<TextView>(R.id.summer).text = count.toString() }
    }

}

sealed class Counter(val inc: Int)
class Inc1 : Counter(1)
class Inc2 : Counter(2)
class Inc3 : Counter(3)
