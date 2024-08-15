package com.leomarkpaway.kotlin_coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ViewModel
    private val myScope = CoroutineScope(CoroutineName("my_scope"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = MyViewModel()

        GlobalScope.launch {
            Log.d("GlobalScope", this.coroutineContext.toString())
        }

        lifecycleScope.launch {
            // lifecycleScope it aware on view life cycle
            Log.d("lifecycleScope", this.coroutineContext.toString())
        }

        myScope.launch {
            // custom scope use only if no lifecycleScope or viewModelScope
            // note: it should cancelable when it note use
            Log.d("myScope", this.coroutineContext.toString())
        }

        runBlocking {
            // only use for testing
            Log.d("runBlocking", this.coroutineContext.toString())
        }

        demoWithContext()
        demoCoroutineJob()
    }

    private fun demoWithContext() {
        GlobalScope.launch(Dispatchers.IO) {
            Log.d("demoWithContext", this.coroutineContext.toString())
            withContext(Dispatchers.Main) {
                Log.d("demoWithContext", this.coroutineContext.toString())
            }
        }
    }

    private fun demoCoroutineJob() {
        myScope.launch {
            val job1 = launch {
                while (isActive) {
                    Log.d("job1", "Job 1 Running ... ")

                }
            }
            Log.d("job1", "Canceling ... ")
            job1.cancel()
            job1.join()
            Log.d("job1", "Job 1 CANCELED!")

            val job2 = launch {
                while (true) {
                    ensureActive()
                    Log.d("job2", "Job 2 Running ... ")

                }
            }
            delay(2000L)
            Log.d("job2", "Canceling ... ")
            job2.cancel()
            job2.join()
            Log.d("job2", "Job 2 CANCELED!")

            val job3 = launch {
                while (true) {
                    Log.d("job3", "Job 3 Running ... ")
                    delay(1000L)
                }
            }
            delay(5000L)
            Log.d("job3", "Canceling ... ")
            job3.cancelAndJoin()
            Log.d("job3", "Job 3 CANCELED!")
        }
    }

}